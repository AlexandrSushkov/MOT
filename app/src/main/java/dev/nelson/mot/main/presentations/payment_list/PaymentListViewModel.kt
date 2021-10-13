package dev.nelson.mot.main.presentations.payment_list

import android.view.View
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.*
import androidx.recyclerview.widget.ItemTouchHelper
import com.jakewharton.rxrelay2.PublishRelay
import com.jakewharton.rxrelay2.Relay
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.nelson.mot.main.data.model.Payment
import dev.nelson.mot.main.domain.PaymentUseCase
import dev.nelson.mot.main.presentations.base.BaseViewModel
import dev.nelson.mot.main.util.SingleLiveEvent
import dev.nelson.mot.main.util.extention.applyThrottling
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class PaymentListViewModel @Inject constructor(
    private val paymentUseCase: PaymentUseCase
) : BaseViewModel() {

    //    val payments = ObservableArrayList<Payment>()
    val payments = ObservableArrayList<Payment>()
    val paymentListLiveData: LiveData<List<Payment>> = liveData {
//        val superheroList = loadSuperheroes()
//        kotlinx.coroutines.delay(1500)
        val paymentList = paymentUseCase.getAllPaymentsCor()

        emit(paymentList)
    }
    val isLoading = ObservableBoolean()
    val isShowEmptyPlaceholder = ObservableBoolean()

    val toolbarElevation = ObservableField<Int>()
//    val paymentAdapter = ObservableField<PaymentListAdapter2>()

    val onPaymentEntityItemClickAction: Relay<Payment> = PublishRelay.create()
    val onSwipeToDeleteAction: Relay<Payment> = PublishRelay.create()
    val onPaymentEntityItemEvent: SingleLiveEvent<Payment> = SingleLiveEvent()
    val swipeToDeleteCallbackLiveData: MutableLiveData<PaymentSwipeToDeleteCallback> = MutableLiveData()
    val swipeToDeleteAction: SingleLiveEvent<Unit> = SingleLiveEvent()
    val onScrollChanged: Relay<Int> = PublishRelay.create()

    val paymentListLivaData = MutableLiveData<List<Payment>>()

    private var _paymentList = paymentUseCase
        .getAllPaymentsWithCategoryOrderDateDescFlow()
        .asLiveData(viewModelScope.coroutineContext)

//    private var _paymentListFlow = paymentUseCase
//        .getAllPaymentsWithCategoryOrderDateDescFlow()
//        .collect { paymentAdapter.get()?.setData(it) }

    val paymentList: LiveData<List<Payment>>
        get() = _paymentList

    private val _paymentListAdapter = PaymentListAdapter(onPaymentEntityItemClickAction, onSwipeToDeleteAction)
    val paymentAdapter = ObservableField(_paymentListAdapter)

    init {
        val swipeToDeleteCallback =
            PaymentSwipeToDeleteCallback(_paymentListAdapter, ItemTouchHelper.ACTION_STATE_IDLE, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT)
        swipeToDeleteCallbackLiveData.value = swipeToDeleteCallback

        viewModelScope.launch {
            paymentUseCase.getAllPaymentsWithCategoryOrderDateDescFlow()
                .map { paymentList -> paymentList.map { PaymentListItemModel.PaymentItemModel(it) } }
                .collect { _paymentListAdapter.setData(it) }
        }

        onPaymentEntityItemClickAction
            .applyThrottling()
            .doOnNext {
                Timber.d("on payment $it click")
                onPaymentEntityItemEvent.postValue(it)
            }
            .subscribe()
            .addToDisposables()

        onSwipeToDeleteAction
            .doOnNext {
                deletePayment(it)
                swipeToDeleteAction.call()
            }
            .subscribe()
            .addToDisposables()

        onScrollChanged
            .distinctUntilChanged()
            .doOnNext { if (it == 0) toolbarElevation.set(0) else toolbarElevation.set(20) }
            .subscribe()
            .addToDisposables()

    }

    private fun deletePayment(payment: Payment) {
        viewModelScope.launch {
            paymentUseCase.deletePayment(payment)
        }
    }

    fun onFabClick(view: View) {
        onPaymentEntityItemEvent.postValue(null)
    }

//    suspend fun loadPeyments(): List<Payment> {
//        return getSuperheroList()
//    }

}
