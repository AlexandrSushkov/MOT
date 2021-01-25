package dev.nelson.mot.main.presentations.paymentlist

import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableBoolean
import androidx.hilt.lifecycle.ViewModelInject
import com.jakewharton.rxrelay2.PublishRelay
import com.jakewharton.rxrelay2.Relay
import dev.nelson.mot.main.data.room.model.payment.Payment
import dev.nelson.mot.main.domain.PaymentUseCase
import dev.nelson.mot.main.presentations.base.BaseViewModel
import dev.nelson.mot.main.util.SingleLiveEvent
import dev.nelson.mot.main.util.extention.applyThrottling
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import timber.log.Timber

class PaymentListViewModel @ViewModelInject constructor(paymentUseCase: PaymentUseCase) : BaseViewModel() {

    val payments = ObservableArrayList<Payment>()
    val isLoading = ObservableBoolean()
    val isShowEmptyPlaceholder = ObservableBoolean()

    val onPaymentItemClickPublisher: Relay<Payment> = PublishRelay.create()
    val onPaymentItemEvent: SingleLiveEvent<Payment> = SingleLiveEvent()


    init {
        paymentUseCase.getAllPayments()
            .doOnSubscribe { isLoading.set(true) }
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext {
                isLoading.set(false)
                payments.clear()
                payments.addAll(it)
                isShowEmptyPlaceholder.set(it.isEmpty())
            }
            .subscribe()
            .addToDisposables()


        onPaymentItemClickPublisher
            .applyThrottling()
            .doOnNext {
                Timber.d("on payment $it click")
                onPaymentItemEvent.postValue(it)
            }
            .subscribe()
            .addToDisposables()
    }

}