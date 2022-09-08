package dev.nelson.mot.main.presentations.screen.payment_list

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.nelson.mot.main.data.model.Category
import dev.nelson.mot.main.data.model.Payment
import dev.nelson.mot.main.domain.use_case.payment.DeletePaymentsUseCase
import dev.nelson.mot.main.domain.use_case.payment.GetPaymentListUseCase
import dev.nelson.mot.main.presentations.base.BaseViewModel
import dev.nelson.mot.main.util.Constants
import dev.nelson.mot.main.util.SingleLiveEvent
import dev.nelson.mot.main.util.StringUtils
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class PaymentListViewModel @Inject constructor(
    extras: SavedStateHandle,
    private val getPaymentListUseCase: GetPaymentListUseCase,
    private val deletePaymentsUseCase: DeletePaymentsUseCase,
) : BaseViewModel() {

    private val mode = if ((extras.get<Category>(Constants.CATEGORY_KEY)) == null) Mode.RecentPayments else Mode.PaymentsForCategory
    private val category: Category = extras[Constants.CATEGORY_KEY] ?: Category(StringUtils.EMPTY)

    val toolbarElevation = ObservableField<Int>()

    val snackBarVisibilityState
        get() = _snackBarVisibilityState.asStateFlow()
    private val _snackBarVisibilityState = MutableStateFlow(false)

    val deletedItemsCount: Flow<Int>
        get() = _deletedItemsCount.asStateFlow()
    private val _deletedItemsCount = MutableStateFlow(0)

    val paymentList: Flow<List<Payment>>
        get() = _paymentList.asStateFlow()
    private val _paymentList = MutableStateFlow(listOf<Payment>())
    private var initialPaymentList = mutableListOf<Payment>()
    private val paymentsToDeleteList = mutableListOf<Payment>()
    private var deletePaymentJob: Job? = null

    init {
        viewModelScope.launch {
            getPaymentListUseCase.execute()
                .collect {
                    initialPaymentList.addAll(it)
                    _paymentList.value = it
                }
        }
    }

    fun onSwipeToDelete(payment: Payment) {
        // cancel previous jot if exist
        deletePaymentJob?.cancel()
        // create new one
        deletePaymentJob = viewModelScope.launch {
            paymentsToDeleteList.add(payment)
            _deletedItemsCount.value = paymentsToDeleteList.size
            showSnackBar()
            val temp = mutableListOf<Payment>().apply {
                addAll(_paymentList.value)
                remove(payment)
            }
            _paymentList.value = temp
            // ui updated, removed items is not visible on the screen
            // wait
            delay(4000)
            hideSnackBar()
            // remove payments from DB
            deletePaymentsUseCase.execute(paymentsToDeleteList)
            Timber.e("Deleted: $paymentsToDeleteList")
            clearItemsToDeleteList()
        }
    }

    fun onUndoDeleteClick() {
        hideSnackBar()
        deletePaymentJob?.let {
            it.cancel()
            _paymentList.value = initialPaymentList
            clearItemsToDeleteList()
        }
    }

    private fun clearItemsToDeleteList(){
        paymentsToDeleteList.clear()
        _deletedItemsCount.value = paymentsToDeleteList.size
    }

    private fun showSnackBar() {
        _snackBarVisibilityState.value = true
    }

    private fun hideSnackBar() {
        _snackBarVisibilityState.value = false
    }


//    private fun getPaymentList(mode: Mode): Flow<List<Payment>> {
//
//        return when (mode) {
//            is Mode.PaymentsForCategory -> category.id?.let { paymentUseCase.getAllPaymentsWithCategoryByCategoryOrderDateDescFlow(it) }
//                ?: paymentUseCase.getAllPaymentsWithoutCategory()
//            is Mode.RecentPayments -> paymentUseCase.getAllPaymentsWithCategoryOrderDateDescFlow()
//        }
//    }

    private sealed class Mode {
        object RecentPayments : Mode()
        object PaymentsForCategory : Mode()
    }

}
