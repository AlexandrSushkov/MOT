package dev.nelson.mot.main.presentations.screen.payment_list

import androidx.databinding.ObservableField
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.nelson.mot.main.data.mapers.copyWith
import dev.nelson.mot.main.data.model.Category
import dev.nelson.mot.main.data.model.Payment
import dev.nelson.mot.main.data.model.PaymentListItemModel
import dev.nelson.mot.main.domain.use_case.date_and_time.GetStartOfCurrentMonthTimeUseCase
import dev.nelson.mot.main.domain.use_case.date_and_time.GetStartOfPreviousMonthTimeUseCase
import dev.nelson.mot.main.domain.use_case.payment.GetPaymentListByDateRange
import dev.nelson.mot.main.domain.use_case.payment.ModifyListOfPaymentsAction
import dev.nelson.mot.main.domain.use_case.payment.ModifyListOfPaymentsUseCase
import dev.nelson.mot.main.presentations.base.BaseViewModel
import dev.nelson.mot.main.util.Constants
import dev.nelson.mot.main.util.MotResult
import dev.nelson.mot.main.util.SortingOrder
import dev.nelson.mot.main.util.StringUtils
import dev.nelson.mot.main.util.successOr
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class PaymentListViewModel @Inject constructor(
    extras: SavedStateHandle,
    private val modifyListOfPaymentsUseCase: ModifyListOfPaymentsUseCase,
    private val getPaymentListByDateRange: GetPaymentListByDateRange,
    private val getStartOfCurrentMonthTimeUseCase: GetStartOfCurrentMonthTimeUseCase,
    private val getStartOfPreviousMonthTimeUseCase: GetStartOfPreviousMonthTimeUseCase,
) : BaseViewModel() {

    private val mode = if ((extras.get<Category>(Constants.CATEGORY_KEY)) == null) Mode.RecentPayments else Mode.PaymentsForCategory
    private val category: Category = extras[Constants.CATEGORY_KEY] ?: Category(StringUtils.EMPTY)

    val toolbarElevation = ObservableField<Int>()

    val snackBarVisibilityState
        get() = _snackBarVisibilityState.asStateFlow()
    private val _snackBarVisibilityState = MutableStateFlow(false)

    val isSelectedState
        get() = _isSelectedState.asStateFlow()
    private val _isSelectedState = MutableStateFlow(false)

    val deletedItemsCount: Flow<Int>
        get() = _deletedItemsCount.asStateFlow()
    private val _deletedItemsCount = MutableStateFlow(0)

    val selectedItemsCount: Flow<Int>
        get() = _selectedItemsCount.asStateFlow()
    private val _selectedItemsCount = MutableStateFlow(0)

    val paymentListResult: Flow<MotResult<List<PaymentListItemModel>>>
        get() = _paymentList.asStateFlow()
    private val _paymentList = MutableStateFlow<MotResult<List<PaymentListItemModel>>>(MotResult.Loading)

    val openNewPayment: Flow<OpenPaymentDetailsState>
        get() = _openNewPayment.asSharedFlow()
    private val _openNewPayment = MutableStateFlow<OpenPaymentDetailsState>(OpenPaymentDetailsState.None)

    private val initialPaymentList = mutableListOf<PaymentListItemModel>()
    private val paymentsToDeleteList = mutableListOf<PaymentListItemModel.PaymentItemModel>()
    private val selectedItemsList = mutableListOf<PaymentListItemModel.PaymentItemModel>()
    private var deletePaymentJob: Job? = null

    init {
        viewModelScope.launch {
            val startOfMonthTime = getStartOfCurrentMonthTimeUseCase.execute()
            val startOfPreviousMonth = getStartOfPreviousMonthTimeUseCase.execute(startOfMonthTime)
            // no end date. otherwise newly added payments wont be shown.
            getPaymentListByDateRange.execute(startOfPreviousMonth, order = SortingOrder.Descending)
                .collect {
                    initialPaymentList.addAll(it)
                    _paymentList.value = MotResult.Success(it)
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
                val nextElement = this[positionOfThePayment + 1]
                if (previousElement is PaymentListItemModel.Header && nextElement is PaymentListItemModel.Header) {
                    // remove date if there is only one payment fo this date
                    remove(previousElement)
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
        _openNewPayment.value = OpenPaymentDetailsState.NewPayment
    }

    fun onItemClick(payment: PaymentListItemModel.PaymentItemModel) {
        if (_isSelectedState.value) {
            // select mode is on. select this item
            if (selectedItemsList.contains(payment)) {
                deselectItem(payment)
                if (selectedItemsList.isEmpty()) {
                    _isSelectedState.value = false
                }
            } else {
                selectItem(payment)
            }
            _selectedItemsCount.value = selectedItemsList.size
        } else {
            // select mode is off. open payment details
            payment.payment.id?.toInt()?.let {
                _openNewPayment.value = OpenPaymentDetailsState.ExistingPayment(it)
            }
        }
    }

    fun onCancelSelectionClick() {
        _isSelectedState.value = false
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
        val newPaymentItemModel = PaymentListItemModel.PaymentItemModel(newPayment, payment.key)
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
    }

    private fun selectItem(payment: PaymentListItemModel.PaymentItemModel) {
//        selectedItemsList.add(payment)
        val newPayment = payment.payment.copyWith(isSelected = true)
        val newPaymentItemModel = PaymentListItemModel.PaymentItemModel(newPayment, payment.key)
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
    }

    private fun cancelSelection() {
        _paymentList.value = MotResult.Success(initialPaymentList)
        selectedItemsList.clear()
        _selectedItemsCount.value = selectedItemsList.size

    }

    fun onDateRangeClick() {
        // open date picker
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


//    private fun getPaymentList(mode: Mode): Flow<List<Payment>> {
//
//        return when (mode) {
//            is Mode.PaymentsForCategory -> category.id?.let { paymentUseCase.getAllPaymentsWithCategoryByCategoryOrderDateDescFlow(it) }
//                ?: paymentUseCase.getAllPaymentsWithoutCategory()
//            is Mode.RecentPayments -> paymentUseCase.getAllPaymentsWithCategoryOrderDateDescFlow()
//        }
//    }

    companion object {
        const val SNAKE_BAR_UNDO_DELAY_MILLS = 4000L
    }

    private sealed class Mode {
        object RecentPayments : Mode()
        object PaymentsForCategory : Mode()
    }

    sealed class OpenPaymentDetailsState {
        class ExistingPayment(val id: Int) : OpenPaymentDetailsState()
        object NewPayment : OpenPaymentDetailsState()
        object None : OpenPaymentDetailsState()
    }
}
