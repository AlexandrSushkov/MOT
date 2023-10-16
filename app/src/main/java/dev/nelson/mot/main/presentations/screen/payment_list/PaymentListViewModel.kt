package dev.nelson.mot.main.presentations.screen.payment_list

import android.content.res.Resources
import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.nelson.mot.R
import dev.nelson.mot.core.ui.view_state.PriceViewState
import dev.nelson.mot.db.utils.SortingOrder
import dev.nelson.mot.main.data.mapers.copyWith
import dev.nelson.mot.main.data.model.Category
import dev.nelson.mot.main.data.model.MotPaymentListItemModel
import dev.nelson.mot.main.data.model.Payment
import dev.nelson.mot.main.domain.use_case.base.execute
import dev.nelson.mot.main.domain.use_case.category.GetCategoriesOrderedByNameFavoriteFirstUseCase
import dev.nelson.mot.main.domain.use_case.category.GetCategoryByIdUseCase
import dev.nelson.mot.main.domain.use_case.date_and_time.GetStartOfCurrentMonthTimeUseCase
import dev.nelson.mot.main.domain.use_case.date_and_time.GetStartOfPreviousMonthTimeUseCase
import dev.nelson.mot.main.domain.use_case.payment.GetPaymentListByFixedDateRangeUseCase
import dev.nelson.mot.main.domain.use_case.payment.GetPaymentListNoFixedDateRange
import dev.nelson.mot.main.domain.use_case.payment.ModifyPaymentsListAction
import dev.nelson.mot.main.domain.use_case.payment.ModifyPaymentsListParams
import dev.nelson.mot.main.domain.use_case.payment.ModifyPaymentsListUseCase
import dev.nelson.mot.main.domain.use_case.price.GetPriceViewStateUseCase
import dev.nelson.mot.main.presentations.base.BaseViewModel
import dev.nelson.mot.main.presentations.screen.payment_list.actions.OpenPaymentDetailsAction
import dev.nelson.mot.main.presentations.shared_view_state.DateViewState
import dev.nelson.mot.main.util.MotUiState
import dev.nelson.mot.main.util.StringUtils
import dev.nelson.mot.main.util.constant.Constants
import dev.nelson.mot.main.util.successOr
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class PaymentListViewModel @Inject constructor(
    extras: SavedStateHandle,
    getCategoriesOrderedByName: GetCategoriesOrderedByNameFavoriteFirstUseCase,
    getPriceViewStateUseCase: GetPriceViewStateUseCase,
    private val resources: Resources,
    private val modifyPaymentsListUseCase: ModifyPaymentsListUseCase,
    private val getPaymentListByFixedDateRangeUseCase: GetPaymentListByFixedDateRangeUseCase,
    private val getPaymentListNoFixedDateRange: GetPaymentListNoFixedDateRange,
    private val getStartOfCurrentMonthTimeUseCase: GetStartOfCurrentMonthTimeUseCase,
    private val getStartOfPreviousMonthTimeUseCase: GetStartOfPreviousMonthTimeUseCase,
    private val getCategoryByIdUseCase: GetCategoryByIdUseCase,
) : BaseViewModel() {

    private val categoryId: Int? = (extras.get<Int>(Constants.CATEGORY_ID_KEY))
    private val screenPaymentsScreenType = categoryId?.let {
        if (it == Constants.NO_CATEGORY_CATEGORY_ID) {
            PaymentsScreenType.PaymentsWithoutCategory
        } else {
            PaymentsScreenType.PaymentsForExistingCategory(it)
        }
    } ?: PaymentsScreenType.RecentPayments

    // states
    val toolBarTitleState
        get() = _toolBarTitleState.asStateFlow()
    private val _toolBarTitleState = MutableStateFlow(StringUtils.EMPTY)

    val snackBarVisibilityState
        get() = _snackBarVisibilityState.asStateFlow()
    private val _snackBarVisibilityState = MutableStateFlow(false)

    val isSelectedModeOnState
        get() = _isSelectedModeOnState.asStateFlow()
    private val _isSelectedModeOnState = MutableStateFlow(false)

    val deletedItemsCountState: Flow<Int>
        get() = _deletedItemsCount.asStateFlow()
    private val _deletedItemsCount = MutableStateFlow(0)

    val selectedItemsCountState: Flow<Int>
        get() = _selectedItemsCount.asStateFlow()
    private val _selectedItemsCount = MutableStateFlow(0)

    val paymentListResult: Flow<MotUiState<List<MotPaymentListItemModel>>>
        get() = _paymentListResult.asStateFlow()
    private val _paymentListResult =
        MutableStateFlow<MotUiState<List<MotPaymentListItemModel>>>(MotUiState.Loading)

    val priceViewState: Flow<PriceViewState>
        get() = _priceViewState.asStateFlow()
    private val _priceViewState = MutableStateFlow(PriceViewState())

    val categoriesState: Flow<List<Category>>
        get() = _categories.asStateFlow()
    private val _categories = MutableStateFlow<List<Category>>(emptyList())

    val showDatePickerDialogState
        get() = _showDatePickerDialogState.asStateFlow()
    private val _showDatePickerDialogState = MutableStateFlow(false)

    val dateViewState
        get() = _dateViewState.asStateFlow()
    private val _dateViewState = MutableStateFlow(DateViewState())

    // actions
    val openPaymentDetailsAction: Flow<OpenPaymentDetailsAction>
        get() = _openPaymentDetailsAction.asSharedFlow()
    private val _openPaymentDetailsAction = MutableSharedFlow<OpenPaymentDetailsAction>()

    // private data
    private val initialPaymentList = mutableListOf<MotPaymentListItemModel>()
    private val paymentsToDeleteList = mutableListOf<MotPaymentListItemModel.Item>()
    private val selectedItemsList = mutableListOf<MotPaymentListItemModel.Item>()
    private var deletePaymentJob: Job? = null

    init {
        launch {
            when (screenPaymentsScreenType) {
                is PaymentsScreenType.RecentPayments -> initRecentPaymentsList()
                is PaymentsScreenType.PaymentsForExistingCategory -> initPaymentsForCategoryList(
                    screenPaymentsScreenType.categoryId
                )

                is PaymentsScreenType.PaymentsWithoutCategory -> initPaymentsWithoutCategoryList()
            }
        }

        launch {
            getCategoriesOrderedByName.execute(SortingOrder.Ascending)
                .collect { _categories.value = it }
        }

        launch {
            getPriceViewStateUseCase.execute()
                .collect { _priceViewState.value = it }
        }
    }

    fun onPaymentSwiped(payment: MotPaymentListItemModel.Item) {
        // cancel previous jot if exist
        deletePaymentJob?.cancel()
        // create new one
        deletePaymentJob = launch {
            paymentsToDeleteList.add(payment)
            _deletedItemsCount.update { paymentsToDeleteList.size }
            showSnackBar()
            _paymentListResult.update { paymentList ->
                val updatedItems = paymentList.successOr(emptyList())
                    .map { item ->
                        when (item) {
                            is MotPaymentListItemModel.Item -> {
                                if (item.payment.id == payment.payment.id) {
                                    item.copy(isShow = false)
                                } else {
                                    item
                                }
                            }

                            else -> item
                        }
                    }
                MotUiState.Success(updatedItems)
            }

            _paymentListResult.update { paymentList ->
                val items = paymentList.successOr(emptyList())
                val updatedItems = items
                    .mapIndexed { index, item ->
                        when (item) {
                            is MotPaymentListItemModel.Header -> {
                                // loop thought items between this and next header.
                                // if all of them is hidden -> hide this header
                                var i = index + 1
                                var subItemsExist = false
                                while (i < items.size && items[i] is MotPaymentListItemModel.Item) {
                                    if (items[i].isShow) {
                                        subItemsExist = true
                                        break
                                    } else {
                                        i++
                                    }
                                }
                                if (subItemsExist) {
                                    item
                                } else {
                                    item.copy(isShow = false)
                                }
                            }

                            else -> item
                        }
                    }
                MotUiState.Success(updatedItems)
            }
            // ui updated, removed items is not visible on the screen
            // wait
            delay(SNAKE_BAR_UNDO_DELAY_MILLS)
            hideSnackBar()
            // remove payments from DB
            val params = ModifyPaymentsListParams(
                paymentsToDeleteList.toPaymentList(),
                ModifyPaymentsListAction.Delete
            )
            modifyPaymentsListUseCase.execute(params)
            Timber.e("Deleted: $paymentsToDeleteList")
            clearItemsToDeleteList()
        }
    }

    fun onUndoDeleteClick() {
        hideSnackBar()
        deletePaymentJob?.let {
            it.cancel()
            _paymentListResult.value = MotUiState.Success(initialPaymentList)
            clearItemsToDeleteList()
        }
    }

    fun onFabClick() = launch {
        cancelSelection()
        val action = when (screenPaymentsScreenType) {
            is PaymentsScreenType.RecentPayments -> OpenPaymentDetailsAction.NewPayment
            else -> OpenPaymentDetailsAction.NewPaymentForCategory(categoryId)
        }
        _openPaymentDetailsAction.emit(action)
    }

    fun onItemClick(payment: MotPaymentListItemModel.Item) {
        if (_isSelectedModeOnState.value) {
            // select mode is on. select/deselect this item
            if (selectedItemsList.contains(payment)) {
                deselectPayment(payment)
                if (selectedItemsList.isEmpty()) {
                    _isSelectedModeOnState.value = false
                }
            } else {
                selectPayment(payment)
            }
        } else {
            // select mode is off. open payment details
            launch {
                payment.payment.id?.let {
                    _openPaymentDetailsAction.emit(
                        OpenPaymentDetailsAction.ExistingPayment(it)
                    )
                }
            }
        }
    }

    fun onDeleteSelectedItemsClick() {
        // cancel previous jot if exist
        deletePaymentJob?.cancel()
        // create new one
        deletePaymentJob = launch {
            paymentsToDeleteList.addAll(selectedItemsList)
            onCancelSelectionClickEvent() // MUST be before apply list with deleted items. this method reset payment list to initial

            _paymentListResult.update { paymentList ->
                val updatedItems = paymentList.successOr(emptyList())
                    .map { item ->
                        when (item) {
                            is MotPaymentListItemModel.Item -> {
                                if (paymentsToDeleteList.any { selectedItem -> selectedItem.payment.id == item.payment.id }) {
                                    item.copy(isShow = false)
                                } else {
                                    item
                                }
                            }

                            else -> item
                        }
                    }
                MotUiState.Success(updatedItems)
            }

            _paymentListResult.update { paymentList ->
                val items = paymentList.successOr(emptyList())
                val updatedItems = items
                    .mapIndexed { index, item ->
                        when (item) {
                            is MotPaymentListItemModel.Header -> {
                                // loop thought items between this and next header.
                                // if all of them is hidden -> hide this header
                                var i = index + 1
                                var subItemsExist = false
                                while (i < items.size && items[i] is MotPaymentListItemModel.Item) {
                                    if (items[i].isShow) {
                                        subItemsExist = true
                                        break
                                    } else {
                                        i++
                                    }
                                }
                                if (subItemsExist) {
                                    item
                                } else {
                                    item.copy(isShow = false)
                                }
                            }

                            else -> item
                        }
                    }
                MotUiState.Success(updatedItems)
            }

            _deletedItemsCount.update { paymentsToDeleteList.size }
            showSnackBar()
            // ui updated, removed items is not visible on the screen
            // wait
            delay(SNAKE_BAR_UNDO_DELAY_MILLS)
            hideSnackBar()
            // remove payments from DB
            val params = ModifyPaymentsListParams(
                paymentsToDeleteList.toPaymentList(),
                ModifyPaymentsListAction.Delete
            )
            modifyPaymentsListUseCase.execute(params)
            Timber.e("Deleted: $paymentsToDeleteList")
            clearItemsToDeleteList()
        }
    }

    fun onCancelSelectionClickEvent() {
        cancelSelection()
    }

    fun onItemLongClick(payment: MotPaymentListItemModel.Item) {
        if (_isSelectedModeOnState.value.not()) {
            // turn on selection state
            _isSelectedModeOnState.value = true
            // find and select item
            selectPayment(payment)
        }
    }

    fun onDismissDatePickerDialog() {
        _showDatePickerDialogState.value = false
    }

    fun onDateSelected(selectedTime: Long) = launch {
        val newItems = selectedItemsList.map { it.payment.copyWith(dateInMills = selectedTime) }
        cancelSelection()
        val params = ModifyPaymentsListParams(newItems, ModifyPaymentsListAction.Edit)
        modifyPaymentsListUseCase.execute(params)
        onDismissDatePickerDialog()
    }

    fun onCategorySelected(category: Category) = launch {
        // workaround. for some reason if copy payments with category list isn't update
        category.id?.let { categoryId ->
            getCategoryByIdUseCase.execute(categoryId).collect { cat ->
                val newItems = selectedItemsList.map { it.payment.copyWith(category = cat) }
                cancelSelection()
                val params = ModifyPaymentsListParams(newItems, ModifyPaymentsListAction.Edit)
                modifyPaymentsListUseCase.execute(params)
            }
        }
    }

    fun onDateClick() {
        _showDatePickerDialogState.value = true
    }

    private suspend fun initRecentPaymentsList() {
        _toolBarTitleState.value = resources.getString(R.string.recent_payments)

        val startOfMonthTime = getStartOfCurrentMonthTimeUseCase.execute()
        val startOfPreviousMonth =
            getStartOfPreviousMonthTimeUseCase.execute(startOfMonthTime)
        // no end date. otherwise newly added payments won't be shown.
        getPaymentListNoFixedDateRange.execute(
            startOfPreviousMonth,
            order = SortingOrder.Descending
        )
            .collect {
                initialPaymentList.clear()
                initialPaymentList.addAll(it)
                _paymentListResult.value = MotUiState.Success(it)
            }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private suspend fun initPaymentsForCategoryList(categoryId: Int) {
        getCategoryByIdUseCase.execute(categoryId)
            .flatMapConcat {
                _toolBarTitleState.value = it.name
                getPaymentListByFixedDateRangeUseCase.execute(
                    category = it,
                    order = SortingOrder.Descending
                )
            }.collect {
                initialPaymentList.clear()
                initialPaymentList.addAll(it)
                _paymentListResult.value = MotUiState.Success(it)
            }
    }

    private suspend fun initPaymentsWithoutCategoryList() {
        _toolBarTitleState.value =
            resources.getString(R.string.category_payments_without_category)
        getPaymentListNoFixedDateRange.execute(
            order = SortingOrder.Descending,
            onlyPaymentsWithoutCategory = true
        )
            .collect {
                initialPaymentList.clear()
                initialPaymentList.addAll(it)
                _paymentListResult.value = MotUiState.Success(it)
            }
    }

    private fun deselectPayment(deselectPayment: MotPaymentListItemModel.Item) {
        updatePaymentItemWithSelection(deselectPayment, false)
    }

    private fun selectPayment(selectedPayment: MotPaymentListItemModel.Item) {
        updatePaymentItemWithSelection(selectedPayment, true)
    }

    private fun updatePaymentItemWithSelection(
        payment: MotPaymentListItemModel.Item,
        isSelected: Boolean
    ) {
        _paymentListResult.update { paymentsList ->
            val updatedItems = paymentsList.successOr(emptyList())
                .map { item ->
                    when (item) {
                        is MotPaymentListItemModel.Item -> {
                            if (item.payment.id == payment.payment.id) {
                                val newPayment = payment.payment.copy(isSelected = isSelected)
                                item.copy(payment = newPayment).also {
                                    if (isSelected) {
                                        selectedItemsList.add(it)
                                    } else {
                                        selectedItemsList.remove(payment)
                                    }
                                }
                            } else {
                                item
                            }
                        }

                        else -> item
                    }
                }
            MotUiState.Success(updatedItems)
        }

        updateSelectedItemsCount()
    }

    private fun updateSelectedItemsCount() {
        _selectedItemsCount.update { selectedItemsList.size }
    }

    private fun cancelSelection() {
        _isSelectedModeOnState.value = false
        selectedItemsList.clear()
        _selectedItemsCount.value = selectedItemsList.size
        _paymentListResult.value = MotUiState.Success(initialPaymentList)
    }

    private fun clearItemsToDeleteList() {
        paymentsToDeleteList.clear()
        _deletedItemsCount.value = paymentsToDeleteList.size
    }

    private fun showSnackBar() {
        _snackBarVisibilityState.value = true
    }

    private fun hideSnackBar() {
        _snackBarVisibilityState.value = false
    }

    private fun MutableList<MotPaymentListItemModel.Item>.toPaymentList(): List<Payment> {
        return this.map { it.payment }
    }

    companion object {
        const val SNAKE_BAR_UNDO_DELAY_MILLS = 4000L
    }

    private sealed class PaymentsScreenType {
        /**
         * Load payments for certain date range (current month)
         */
        data object RecentPayments : PaymentsScreenType()

        /**
         * Load payments for a category
         */
        class PaymentsForExistingCategory(val categoryId: Int) : PaymentsScreenType()

        /**
         * Load payments without a category
         */
        data object PaymentsWithoutCategory : PaymentsScreenType()
    }
}
