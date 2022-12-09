package dev.nelson.mot.main.presentations.screen.payment_list

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.nelson.mot.main.data.mapers.copyWith
import dev.nelson.mot.main.data.model.Category
import dev.nelson.mot.main.data.model.Payment
import dev.nelson.mot.main.data.model.PaymentListItemModel
import dev.nelson.mot.main.domain.use_case.category.GetCategoriesOrderedByNameFavoriteFirstUseCase
import dev.nelson.mot.main.domain.use_case.category.GetCategoryUseCase
import dev.nelson.mot.main.domain.use_case.date_and_time.GetStartOfCurrentMonthTimeUseCase
import dev.nelson.mot.main.domain.use_case.date_and_time.GetStartOfPreviousMonthTimeUseCase
import dev.nelson.mot.main.domain.use_case.payment.GetPaymentListByDateRange
import dev.nelson.mot.main.domain.use_case.payment.ModifyListOfPaymentsAction
import dev.nelson.mot.main.domain.use_case.payment.ModifyListOfPaymentsUseCase
import dev.nelson.mot.main.presentations.base.BaseViewModel
import dev.nelson.mot.main.presentations.screen.payment_list.actions.OpenPaymentDetailsAction
import dev.nelson.mot.main.util.Constants
import dev.nelson.mot.main.util.MotResult
import dev.nelson.mot.main.util.SortingOrder
import dev.nelson.mot.main.util.successOr
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.Calendar
import java.util.Date
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class PaymentListViewModel @Inject constructor(
    extras: SavedStateHandle,
    private val modifyListOfPaymentsUseCase: ModifyListOfPaymentsUseCase,
    private val getPaymentListByDateRange: GetPaymentListByDateRange,
    private val getStartOfCurrentMonthTimeUseCase: GetStartOfCurrentMonthTimeUseCase,
    private val getStartOfPreviousMonthTimeUseCase: GetStartOfPreviousMonthTimeUseCase,
    private val getCategoryUseCase: GetCategoryUseCase,
    getCategoriesOrderedByName: GetCategoriesOrderedByNameFavoriteFirstUseCase,
) : BaseViewModel() {

    private val categoryId: Int? = (extras.get<Int>(Constants.CATEGORY_ID_KEY))
    private val screenScreenType = categoryId?.let { ScreenType.PaymentsForCategory(it) } ?: ScreenType.RecentPayments

    // states
    val toolBarTitleState
        get() = _toolBarTitleState.asStateFlow()
    private val _toolBarTitleState = MutableStateFlow("")

    val toolbarElevation
        get() = _toolbarElevation.asStateFlow()
    private val _toolbarElevation = MutableStateFlow(0)

    val snackBarVisibilityState
        get() = _snackBarVisibilityState.asStateFlow()
    private val _snackBarVisibilityState = MutableStateFlow(false)

    val isSelectedState
        get() = _isSelectedState.asStateFlow()
    private val _isSelectedState = MutableStateFlow(false)

    val deletedItemsCountState: Flow<Int>
        get() = _deletedItemsCount.asStateFlow()
    private val _deletedItemsCount = MutableStateFlow(0)

    val selectedItemsCountState: Flow<Int>
        get() = _selectedItemsCount.asStateFlow()
    private val _selectedItemsCount = MutableStateFlow(0)

    val paymentListState: Flow<MotResult<List<PaymentListItemModel>>>
        get() = _paymentList.asStateFlow()
    private val _paymentList = MutableStateFlow<MotResult<List<PaymentListItemModel>>>(MotResult.Loading)

    val categoriesState: Flow<List<Category>>
        get() = _categories.asStateFlow()
    private val _categories = MutableStateFlow<List<Category>>(emptyList())

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
        viewModelScope.launch {
            when (screenScreenType) {
                is ScreenType.RecentPayments -> {
                    _toolBarTitleState.value = "recent payments"

                    val startOfMonthTime = getStartOfCurrentMonthTimeUseCase.execute()
                    val startOfPreviousMonth = getStartOfPreviousMonthTimeUseCase.execute(startOfMonthTime)
                    // no end date. otherwise newly added payments won't be shown.
                    getPaymentListByDateRange.execute(startOfPreviousMonth, order = SortingOrder.Descending)
                        .collect {
                            initialPaymentList.clear()
                            initialPaymentList.addAll(it)
                            _paymentList.value = MotResult.Success(it)
                        }
                }
                is ScreenType.PaymentsForCategory -> {
                    getCategoryUseCase.execute(screenScreenType.categoryId)
                        .flatMapConcat {
                            _toolBarTitleState.value = it.name
                            getPaymentListByDateRange.execute(category = it, order = SortingOrder.Descending)
                        }.collect {
                            initialPaymentList.clear()
                            initialPaymentList.addAll(it)
                            _paymentList.value = MotResult.Success(it)
                        }
                }
            }
        }

        viewModelScope.launch {

        }

        viewModelScope.launch {
            getCategoriesOrderedByName.execute().collect {
                _categories.value = it
            }
        }
    }

    fun onSwipeToDelete(payment: PaymentListItemModel.PaymentItemModel) {
        // cancel previous jot if exist
        deletePaymentJob?.cancel()
        // create new one
        deletePaymentJob = viewModelScope.launch {
            paymentsToDeleteList.add(payment)
            _deletedItemsCount.value = paymentsToDeleteList.size
            showSnackBar()
            val temp = mutableListOf<PaymentListItemModel>().apply {
                addAll(_paymentList.value.successOr(emptyList()))
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
            _paymentList.value = MotResult.Success(temp)
            // ui updated, removed items is not visible on the screen
            // wait
            delay(SNAKE_BAR_UNDO_DELAY_MILLS)
            hideSnackBar()
            // remove payments from DB
            modifyListOfPaymentsUseCase.execute(paymentsToDeleteList.toPaymentList(), ModifyListOfPaymentsAction.Delete)
            Timber.e("Deleted: $paymentsToDeleteList")
            clearItemsToDeleteList()
        }
    }

    fun onUndoDeleteClick() {
        hideSnackBar()
        deletePaymentJob?.let {
            it.cancel()
            _paymentList.value = MotResult.Success(initialPaymentList)
            clearItemsToDeleteList()
        }
    }

    fun onFabClick() {
        viewModelScope.launch {
            cancelSelection()
            _openPaymentDetailsAction.emit(OpenPaymentDetailsAction.NewPayment)
        }
    }

    fun onItemClick(payment: PaymentListItemModel.PaymentItemModel) {
        if (_isSelectedState.value) {
            // select mode is on. select/deselect this item
            if (selectedItemsList.contains(payment)) {
                deselectItem(payment)
                if (selectedItemsList.isEmpty()) {
                    _isSelectedState.value = false
                }
            } else {
                selectItem(payment)
            }
        } else {
            // select mode is off. open payment details
            viewModelScope.launch {
                payment.payment.id?.toInt()?.let { _openPaymentDetailsAction.emit(OpenPaymentDetailsAction.ExistingPayment(it)) }
            }
        }
    }

    fun onDeleteSelectedItemsClick() {
        // cancel previous jot if exist
        deletePaymentJob?.cancel()
        // create new one
        deletePaymentJob = viewModelScope.launch {
            paymentsToDeleteList.addAll(selectedItemsList)
            onCancelSelectionClick() // MUST be before apply list with deleted items. this method reset payment list to initial
            val temp = mutableListOf<PaymentListItemModel>().apply {
                addAll(_paymentList.value.successOr(emptyList()))
            }
            paymentsToDeleteList.forEach { paymentItemModel ->
                // paymentItemModel is not the save in paymentsToDeleteList and temp list, as onCancelSelectionClick() reset list. make a copy from initial list. this is two different objects.
                val positionOfThePayment = temp.indexOf(temp.find { item -> item.key == paymentItemModel.key })
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
            _paymentList.value = MotResult.Success(temp)
            _deletedItemsCount.value = paymentsToDeleteList.size
            showSnackBar()
            // ui updated, removed items is not visible on the screen
            // wait
            delay(SNAKE_BAR_UNDO_DELAY_MILLS)
            hideSnackBar()
            // remove payments from DB
            modifyListOfPaymentsUseCase.execute(paymentsToDeleteList.toPaymentList(), ModifyListOfPaymentsAction.Delete)
            Timber.e("Deleted: $paymentsToDeleteList")
            clearItemsToDeleteList()
        }
    }

    fun onCancelSelectionClick() {
        cancelSelection()
    }

    fun onItemLongClick(payment: PaymentListItemModel.PaymentItemModel) {
        if (_isSelectedState.value.not()) {
            // turn on selection state
            _isSelectedState.value = true
            // find and select item
            selectItem(payment)
        }
    }

    private fun deselectItem(payment: PaymentListItemModel.PaymentItemModel) {
        selectedItemsList.remove(payment)
        val newPayment = payment.payment.copyWith(isSelected = false)
        val newPaymentItemModel = PaymentListItemModel.PaymentItemModel(newPayment, payment.shotCategory, payment.key)
        val tempList = _paymentList.value.successOr(emptyList()).map { item ->
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
        _paymentList.value = MotResult.Success(tempList)
        _selectedItemsCount.value = selectedItemsList.size
    }

    private fun selectItem(payment: PaymentListItemModel.PaymentItemModel) {
//        selectedItemsList.add(payment)
        val newPayment = payment.payment.copyWith(isSelected = true)
        val newPaymentItemModel = PaymentListItemModel.PaymentItemModel(newPayment, payment.shotCategory, payment.key)
        selectedItemsList.add(newPaymentItemModel)
        val tempList = _paymentList.value.successOr(emptyList()).map { item ->
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
        _paymentList.value = MotResult.Success(tempList)
        _selectedItemsCount.value = selectedItemsList.size
    }

    private fun cancelSelection() {
        _isSelectedState.value = false
        selectedItemsList.clear()
        _selectedItemsCount.value = selectedItemsList.size
        _paymentList.value = MotResult.Success(initialPaymentList)
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

    fun onDateSet(selectedYear: Int, monthOfYear: Int, dayOfMonth: Int) {
        viewModelScope.launch {
            val selectedDateCalendar = calendar.apply { set(selectedYear, monthOfYear, dayOfMonth) }
            val selectedDate: Date = selectedDateCalendar.time
            val newItems = selectedItemsList.map { it.payment.copyWith(dateInMills = selectedDate.time) }
            cancelSelection()
            modifyListOfPaymentsUseCase.execute(newItems, ModifyListOfPaymentsAction.Edit)
        }
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

    fun onCategorySelected(category: Category) {
        viewModelScope.launch {
            // workaround. for some reason if copy payments with category list isn't update
            category.id?.let { categoryId ->
                getCategoryUseCase.execute(categoryId).collect { cat ->
                    val newItems = selectedItemsList.map { it.payment.copyWith(category = cat) }
                    cancelSelection()
                    modifyListOfPaymentsUseCase.execute(newItems, ModifyListOfPaymentsAction.Edit)
                }
            }
        }
    }

    companion object {
        const val SNAKE_BAR_UNDO_DELAY_MILLS = 4000L
    }

    private sealed class ScreenType {
        object RecentPayments : ScreenType()
        class PaymentsForCategory(val categoryId: Int) : ScreenType()
    }
}
