package dev.nelson.mot.main.presentations.paymentlist

import android.database.Observable
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableBoolean
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.jakewharton.rxrelay2.PublishRelay
import com.jakewharton.rxrelay2.Relay
import dev.nelson.mot.main.data.model.Payment
import dev.nelson.mot.main.data.room.model.payment.PaymentEntity
import dev.nelson.mot.main.data.room.model.paymentjoin.PaymentWithCategory
import dev.nelson.mot.main.domain.PaymentUseCase
import dev.nelson.mot.main.presentations.base.BaseViewModel
import dev.nelson.mot.main.util.SingleLiveEvent
import dev.nelson.mot.main.util.extention.applyThrottling
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.coroutines.launch
import timber.log.Timber

class PaymentListViewModel @ViewModelInject constructor(paymentUseCase: PaymentUseCase) : BaseViewModel() {

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

    val onPaymentEntityItemClickPublisher: Relay<PaymentEntity> = PublishRelay.create()
    val onPaymentEntityItemEvent: SingleLiveEvent<PaymentEntity> = SingleLiveEvent()


    init {
//        paymentUseCase.getAllPayments()
//            .doOnSubscribe { isLoading.set(true) }
//            .observeOn(AndroidSchedulers.mainThread())
//            .doOnNext {
//                isLoading.set(false)
//                payments.clear()
//                payments.addAll(it)
//                isShowEmptyPlaceholder.set(it.isEmpty())
//            }
//            .subscribe()
//            .addToDisposables()


        onPaymentEntityItemClickPublisher
            .applyThrottling()
            .doOnNext {
                Timber.d("on payment $it click")
                onPaymentEntityItemEvent.postValue(it)
            }
            .subscribe()
            .addToDisposables()

        viewModelScope.launch {
            isLoading.set(true)
            val paymentList = paymentUseCase.getAllPaymentsCor()
            isLoading.set(false)
            payments.clear()
            payments.addAll(paymentList)
            isShowEmptyPlaceholder.set(paymentList.isEmpty())
        }
    }

//    suspend fun loadPeyments(): List<Payment> {
//        return getSuperheroList()
//    }

}