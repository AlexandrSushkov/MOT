package dev.nelson.mot.main.presentations.payment

import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.nelson.mot.main.data.mapers.copyWith
import dev.nelson.mot.main.data.model.Payment
import dev.nelson.mot.main.domain.use_case.payment.AddNewPaymentUseCase
import dev.nelson.mot.main.domain.use_case.payment.EditPaymentUseCase
import dev.nelson.mot.main.domain.use_case.payment.PaymentUseCase
import dev.nelson.mot.main.presentations.base.BaseViewModel
import dev.nelson.mot.main.util.SingleLiveEvent
import dev.nelson.mot.main.util.constant.NetworkConstants
import kotlinx.coroutines.launch
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class PaymentDetailsViewModel @Inject constructor(
    private val paymentUseCase: PaymentUseCase,
    private val addNewPaymentUseCase: AddNewPaymentUseCase,
    private val editPaymentUseCase: EditPaymentUseCase,
    handle: SavedStateHandle
) : BaseViewModel() {

    private val payment: Payment? = handle.get<Payment>("payment")
    val paymentName = ObservableField(payment?.name)
    val paymentNameSelection = ObservableInt()
    val paymentCost = ObservableField(payment?.cost?.toString() ?: 0.toString())
    val finishAction = SingleLiveEvent<Unit>()

    fun onSaveClick() {
        if (payment == null) addNewPayment() else editPayment()
    }

    private fun addNewPayment() {
        viewModelScope.launch {
            val currentDateInMills = System.currentTimeMillis()
            val currentDateFormatted = getCurrentDateTime().toString(NetworkConstants.DATE_FORMAT)
            val payment = Payment(
                paymentName.get() ?: "",
                (paymentCost.get()?.toIntOrNull() ?: 0),
                date = currentDateFormatted,
                dateInMills = currentDateInMills
            )
            addNewPaymentUseCase.execute(payment)
            Timber.e("payment $payment")
            finishAction.call()
        }
    }

    private fun editPayment() {
        viewModelScope.launch {
            payment?.let {
                //todo, check if payment has been edited, if not, just close screen
                val updatedPayment = it.copyWith(paymentName.get() ?: "", (paymentCost.get()?.toIntOrNull() ?: 0))
                editPaymentUseCase.execute(updatedPayment)
                Timber.e("updated payment $payment")
                finishAction.call()
            }
        }
    }

    private fun getCurrentDateTime(): Date {
        return Calendar.getInstance().time
    }
}

fun Date.toString(format: String, locale: Locale = Locale.getDefault()): String {
    val formatter = SimpleDateFormat(format, locale)
    return formatter.format(this)
}
