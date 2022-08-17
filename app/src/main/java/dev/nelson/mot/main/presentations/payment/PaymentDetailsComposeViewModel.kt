package dev.nelson.mot.main.presentations.payment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.nelson.mot.main.data.mapers.copyWith
import dev.nelson.mot.main.data.mapers.toCategory
import dev.nelson.mot.main.data.model.Category
import dev.nelson.mot.main.data.model.Payment
import dev.nelson.mot.main.data.room.model.category.CategoryEntity
import dev.nelson.mot.main.domain.use_case.category.GetCategoriesOrderedByName
import dev.nelson.mot.main.domain.use_case.payment.AddNewPaymentUseCase
import dev.nelson.mot.main.domain.use_case.payment.EditPaymentUseCase
import dev.nelson.mot.main.presentations.base.BaseViewModel
import dev.nelson.mot.main.util.DateUtils
import dev.nelson.mot.main.util.SingleLiveEvent
import dev.nelson.mot.main.util.constant.NetworkConstants
import dev.nelson.mot.main.util.toFormattedDate
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class PaymentDetailsComposeViewModel @Inject constructor(
    private val addNewPaymentUseCase: AddNewPaymentUseCase,
    private val editPaymentUseCase: EditPaymentUseCase,
    private val getCategoriesOrderedByName: GetCategoriesOrderedByName,
    handle: SavedStateHandle
) : BaseViewModel() {

    // data
    private val payment: Payment? = handle.get<Payment>("payment")
    val paymentName = MutableLiveData(payment?.name.orEmpty())
    val categoryName = MutableLiveData(payment?.category?.name ?: "category")
    val date = MutableLiveData("")
    val cost = MutableLiveData(payment?.cost?.toString().orEmpty())
    val message = MutableLiveData(payment?.message.orEmpty())

    // actions
    val finishAction = SingleLiveEvent<Unit>()
    val onDateClickAction = SingleLiveEvent<Unit>()
    val categories = SingleLiveEvent<List<CategoryEntity>>()

    //var
    var selectedCategory: CategoryEntity? = null
    var dateInMills = 0L
    private val calendar: Calendar by lazy { Calendar.getInstance() }

    init {
        setInitialDate()
        viewModelScope.launch {
            getCategoriesOrderedByName.execute()
                .collect { categories.value = it }
        }
    }

    fun onSaveClick() {
//        if (payment == null) addNewPayment() else editPayment()
        payment?.let { editPayment() } ?: addNewPayment()
    }

    fun onDateClick() {
        Timber.e("on Date click")
        onDateClickAction.call()
    }

    fun onDateSet(selectedYear: Int, monthOfYear: Int, dayOfMonth: Int) {
        val selectedDateCalendar = calendar.apply { set(selectedYear, monthOfYear, dayOfMonth) }
        val selectedDate = selectedDateCalendar.time
        setDate(selectedDate.time)
    }

    fun onCategoryClick() {
    }

    fun onCategorySelected(category: Category) {
    }

    private fun addNewPayment() {
        viewModelScope.launch {
            val currentDateInMills = System.currentTimeMillis()
            val payment = Payment(
                paymentName.value.orEmpty(),
                (cost.value?.toIntOrNull() ?: 0),
                dateInMills = currentDateInMills,
                category = selectedCategory?.toCategory(),
                message = message.value.orEmpty()
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
                    paymentName.value.orEmpty(),
                    (cost.value?.toIntOrNull() ?: 0),
                    dateInMills,
                    selectedCategory?.toCategory(),
                    message.value.orEmpty()
                )
                editPaymentUseCase.execute(updatedPayment)
                Timber.e("updated payment $payment")
                finishAction.call()
            }
        }
    }

    private fun setInitialDate() {
        dateInMills = payment?.dateInMills ?: DateUtils.getCurrentDate().time
        setDate(dateInMills)
    }

    private fun setDate(dateInMills: Long) {
        this.dateInMills = dateInMills
        val dateFromMills = DateUtils.createDateFromMills(dateInMills)
        val dateTextFormatted = dateFromMills.toFormattedDate(NetworkConstants.DATE_FORMAT)
        date.value = dateTextFormatted
    }

}
