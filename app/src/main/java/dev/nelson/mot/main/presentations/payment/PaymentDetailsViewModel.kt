package dev.nelson.mot.main.presentations.payment

import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.nelson.mot.main.data.mapers.copyWith
import dev.nelson.mot.main.data.mapers.toCategory
import dev.nelson.mot.main.data.model.Payment
import dev.nelson.mot.main.data.room.model.category.CategoryEntity
import dev.nelson.mot.main.domain.use_case.category.GetCategoriesOrderedByName
import dev.nelson.mot.main.domain.use_case.payment.AddNewPaymentUseCase
import dev.nelson.mot.main.domain.use_case.payment.EditPaymentUseCase
import dev.nelson.mot.main.domain.use_case.payment.PaymentUseCase
import dev.nelson.mot.main.presentations.base.BaseViewModel
import dev.nelson.mot.main.util.DateUtils
import dev.nelson.mot.main.util.DateUtils.getCurrentDate
import dev.nelson.mot.main.util.SingleLiveEvent
import dev.nelson.mot.main.util.constant.NetworkConstants
import dev.nelson.mot.main.util.toFormattedDate
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class PaymentDetailsViewModel @Inject constructor(
    private val paymentUseCase: PaymentUseCase,
    private val addNewPaymentUseCase: AddNewPaymentUseCase,
    private val editPaymentUseCase: EditPaymentUseCase,
    private val getCategoriesOrderedByName: GetCategoriesOrderedByName,
    handle: SavedStateHandle
) : BaseViewModel() {

    private val payment: Payment? = handle.get<Payment>("payment")
    val paymentName = ObservableField(payment?.name)
    val categoryName = ObservableField(payment?.category?.name ?: "category")
    val date = ObservableField("")
    val paymentNameSelection = ObservableInt()
    val paymentCost = ObservableField(payment?.cost?.toString() ?: 0.toString())
    val finishAction = SingleLiveEvent<Unit>()
    val categories = SingleLiveEvent<List<CategoryEntity>>()
    var selectedCategory: CategoryEntity? = null
    var dateInMills = 0L
    val requestTitleFieldFocusAction = SingleLiveEvent<Unit>()
//    val mFieldFocusAction = SingleLiveEvent<Unit>()


    init {
        setDate()
        viewModelScope.launch {
            getCategoriesOrderedByName.execute()
                .collect { categories.value = it }
        }
        requestTitleFieldFocusAction.call()
    }

    fun onSaveClick() {
        if (payment == null) addNewPayment() else editPayment()
    }

    fun onCategoryItemClick(categoryEntity: CategoryEntity) {
        selectedCategory = categoryEntity
        categoryName.set(categoryEntity.name)
    }

    private fun addNewPayment() {
        viewModelScope.launch {
            val currentDateInMills = System.currentTimeMillis()
            val payment = Payment(
                paymentName.get() ?: "",
                (paymentCost.get()?.toIntOrNull() ?: 0),
                dateInMills = currentDateInMills,
                category = selectedCategory?.toCategory()
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
                val updatedPayment = it.copyWith(
                    paymentName.get() ?: "",
                    (paymentCost.get()?.toIntOrNull() ?: 0),
                    dateInMills,
                    selectedCategory?.toCategory()
                )
                editPaymentUseCase.execute(updatedPayment)
                Timber.e("updated payment $payment")
                finishAction.call()
            }
        }
    }

    private fun setDate() {
        dateInMills = payment?.dateInMills ?: getCurrentDate().time
        val dateFromMills = DateUtils.createDateFromMills(dateInMills)
        val dateTextFormatted = dateFromMills.toFormattedDate(NetworkConstants.DATE_FORMAT)
        date.set(dateTextFormatted)
    }

//    private fun getCurrentDateFormatted(): String {
//        val currentDate = getCurrentDate()
//        dateInMils = currentDate.time
//        return currentDate
//            .toFormattedDate(NetworkConstants.DATE_FORMAT)
//    }
}

//fun Date.toFormattedDate(format: String, locale: Locale = Locale.getDefault()): String {
//    val formatter = SimpleDateFormat(format, locale)
//    return formatter.format(this)
//}
