package dev.nelson.mot.main.presentations.screen.payment_list

import android.content.res.Resources
import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.nelson.mot.core.ui.view_state.PriceViewState
import dev.nelson.mot.main.data.mapers.copyWith
import dev.nelson.mot.main.data.model.Category
import dev.nelson.mot.main.data.model.Payment
import dev.nelson.mot.main.data.model.PaymentListItemModel
import dev.nelson.mot.main.domain.use_case.category.GetCategoriesOrderedByNameFavoriteFirstUseCase
import dev.nelson.mot.main.domain.use_case.category.GetCategoryByIdUseCase
import dev.nelson.mot.main.domain.use_case.date_and_time.GetStartOfCurrentMonthTimeUseCase
import dev.nelson.mot.main.domain.use_case.date_and_time.GetStartOfPreviousMonthTimeUseCase
import dev.nelson.mot.main.domain.use_case.base.execute
import dev.nelson.mot.main.domain.use_case.payment.GetPaymentListByFixedDateRangeUseCase
import dev.nelson.mot.main.domain.use_case.payment.ModifyPaymentsListAction
import dev.nelson.mot.main.domain.use_case.payment.ModifyPaymentsListParams
import dev.nelson.mot.main.domain.use_case.payment.ModifyPaymentsListUseCase
import dev.nelson.mot.main.domain.use_case.price.GetPriceViewStateUseCase
import dev.nelson.mot.main.presentations.base.BaseViewModel
import dev.nelson.mot.main.presentations.screen.payment_list.actions.OpenPaymentDetailsAction
import dev.nelson.mot.main.util.constant.Constants
import dev.nelson.mot.main.util.MotUiState
import dev.nelson.mot.db.utils.SortingOrder
import dev.nelson.mot.main.domain.use_case.payment.GetPaymentListNoFixedDateRange
import dev.nelson.mot.main.util.StringUtils
import dev.nelson.mot.main.util.successOr
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*
import javax.inject.Inject
import dev.nelson.mot.R
import dev.nelson.mot.main.presentations.shared_view_state.DateViewState

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
    private val screenScreenType = categoryId?.let {
        if (it == Constants.NO_CATEGORY_CATEGORY_ID) {
            ScreenType.PaymentsWithoutCategory
        } else {
            ScreenType.PaymentsForExistingCategory(it)
        }
    } ?: ScreenType.RecentPayments

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

    val paymentListResult: Flow<MotUiState<List<PaymentListItemModel>>>
        get() = _paymentListResult.asStateFlow()
    private val _paymentListResult =
        MutableStateFlow<MotUiState<List<PaymentListItemModel>>>(MotUiState.Loading)

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
    private val initialPaymentList = mutableListOf<PaymentListItemModel>()
    private val paymentsToDeleteList = mutableListOf<PaymentListItemModel.PaymentItemModel>()
    private val selectedItemsList = mutableListOf<PaymentListItemModel.PaymentItemModel>()
    private var deletePaymentJob: Job? = null
    private val calendar: Calendar by lazy { Calendar.getInstance() }

    init {
        launch {
            when (screenScreenType) {
                is ScreenType.RecentPayments -> initRecentPaymentsList()
                is ScreenType.PaymentsForExistingCategory -> initPaymentsForCategoryList(
                    screenScreenType.categoryId
                )

                is ScreenType.PaymentsWithoutCategory -> initPaymentsWithoutCategoryList()
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

    fun onSwipeToDelete(payment: PaymentListItemModel.PaymentItemModel) {
        // cancel previous jot if exist
        deletePaymentJob?.cancel()
        // create new one
        deletePaymentJob = launch {
            paymentsToDeleteList.add(payment)
            _deletedItemsCount.value = paymentsToDeleteList.size
            showSnackBar()
            val temp = mutableListOf<PaymentListItemModel>().apply {
                addAll(_paymentListResult.value.successOr(emptyList()))
                val positionOfThePayment = indexOf(payment)
                val previousElement = this[positionOfThePayment - 1]
                if (positionOfThePayment + 1 == this.size) {
                    // this it the last element in the list
                    if (previousElement is PaymentListItemModel.Header) {
                        // remove date item if there is only one payment fo this date
                        remove(previousElement)
                    }
                } else {
                    // this element is NOT last in the list
                    val nextElement = this[positionOfThePayment + 1]
                    if (previousElement is PaymentListItemModel.Header && nextElement is PaymentListItemModel.Header) {
                        // remove date item if there is only one payment fo this date
                        remove(previousElement)
                    }
                }
                remove(payment)
            }
            _paymentListResult.value = MotUiState.Success(temp)
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
        _openPaymentDetailsAction.emit(OpenPaymentDetailsAction.NewPayment)
    }

    fun onItemClick(payment: PaymentListItemModel.PaymentItemModel) {
        if (_isSelectedModeOnState.value) {
            // select mode is on. select/deselect this item
            if (selectedItemsList.contains(payment)) {
                deselectItem(payment)
                if (selectedItemsList.isEmpty()) {
                    _isSelectedModeOnState.value = false
                }
            } else {
                selectItem(payment)
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
            val temp = mutableListOf<PaymentListItemModel>().apply {
                addAll(_paymentListResult.value.successOr(emptyList()))
            }
            paymentsToDeleteList.forEach { paymentItemModel ->
                // paymentItemModel is not the save in paymentsToDeleteList and temp list, as onCancelSelectionClick() reset list. make a copy from initial list. this is two different objects.
                val positionOfThePayment =
                    temp.indexOf(temp.find { item -> item.key == paymentItemModel.key })
                val paymentElement = temp[positionOfThePayment]
                // TODO:   should be easier to fond element by id and use it as in swipe to delete implementation. And not rely on the position of the element
                val previousElementPosition = positionOfThePayment - 1
                val previousElement = temp[previousElementPosition]
                if (positionOfThePayment + 1 == temp.size) {
                    // this it the last element in the list
                    if (previousElement is PaymentListItemModel.Header) {
                        // remove date item if there is only one payment fo this date
                        temp.remove(previousElement)
                    }
                } else {
                    // this element is NOT last in the list
                    val nextElement = temp[positionOfThePayment + 1]
                    if (previousElement is PaymentListItemModel.Header && nextElement is PaymentListItemModel.Header) {
                        // remove date item if there is only one payment fo this date
                        temp.remove(previousElement)
                    }
                }
                temp.remove(paymentElement)
            }
            _paymentListResult.value = MotUiState.Success(temp)
            _deletedItemsCount.value = paymentsToDeleteList.size
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

    fun onItemLongClick(payment: PaymentListItemModel.PaymentItemModel) {
        if (_isSelectedModeOnState.value.not()) {
            // turn on selection state
            _isSelectedModeOnState.value = true
            // find and select item
            selectItem(payment)
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

    private fun deselectItem(payment: PaymentListItemModel.PaymentItemModel) {
        selectedItemsList.remove(payment)
        val newPayment = payment.payment.copyWith(isSelected = false)
        val newPaymentItemModel =
            PaymentListItemModel.PaymentItemModel(newPayment, payment.showCategory, payment.key)
        val tempList = _paymentListResult.value.successOr(emptyList()).map { item ->
            if (item is PaymentListItemModel.PaymentItemModel) {
                if (item.payment.id == payment.payment.id) {
                    newPaymentItemModel
                } else {
                    item
                }
            } else {
                item
            }
        }
        _paymentListResult.value = MotUiState.Success(tempList)
        _selectedItemsCount.value = selectedItemsList.size
    }

    private fun selectItem(payment: PaymentListItemModel.PaymentItemModel) {
//        selectedItemsList.add(payment)
        val newPayment = payment.payment.copyWith(isSelected = true)
        val newPaymentItemModel =
            PaymentListItemModel.PaymentItemModel(newPayment, payment.showCategory, payment.key)
        selectedItemsList.add(newPaymentItemModel)
        val tempList = _paymentListResult.value.successOr(emptyList()).map { item ->
            if (item is PaymentListItemModel.PaymentItemModel) {
                if (item.payment.id == payment.payment.id) {
                    newPaymentItemModel
                } else {
                    item
                }
            } else {
                item
            }
        }
        _paymentListResult.value = MotUiState.Success(tempList)
        _selectedItemsCount.value = selectedItemsList.size
    }

    private fun cancelSelection() {
        _isSelectedModeOnState.value = false
        selectedItemsList.clear()
        _selectedItemsCount.value = selectedItemsList.size
        _paymentListResult.value = MotUiState.Success(initialPaymentList)
    }

    fun onDateRangeClick() {
        // open date picker
    }

    fun onChangeDateClick() {
        // open date picker
    }

    fun onChangeCategoryClick() {
        // open category modal
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

    private fun MutableList<PaymentListItemModel.PaymentItemModel>.toPaymentList(): List<Payment> {
        return this.map { it.payment }
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

    companion object {
        const val SNAKE_BAR_UNDO_DELAY_MILLS = 4000L
    }

    private sealed class ScreenType {
        /**
         * Load payments for current month
         */
        object RecentPayments : ScreenType()

        /**
         * Load payments for a category
         */
        class PaymentsForExistingCategory(val categoryId: Int) : ScreenType()

        /**
         * Load payments without a category
         */
        object PaymentsWithoutCategory : ScreenType()
    }
}
