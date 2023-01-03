package dev.nelson.mot.main.presentations.screen.payment_details

import android.icu.text.DecimalFormat
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.nelson.mot.main.data.model.Category
import dev.nelson.mot.main.data.model.Payment
import dev.nelson.mot.main.domain.use_case.category.GetCategoriesOrderedByNameFavoriteFirstUseCase
import dev.nelson.mot.main.domain.use_case.date_and_time.GetStartOfCurrentMonthTimeUseCase
import dev.nelson.mot.main.domain.use_case.payment.GetPaymentUseCase
import dev.nelson.mot.main.domain.use_case.payment.ModifyPaymentAction
import dev.nelson.mot.main.domain.use_case.payment.ModifyPaymentUseCase
import dev.nelson.mot.main.presentations.base.BaseViewModel
import dev.nelson.mot.main.util.DateUtils
import dev.nelson.mot.main.util.constant.NetworkConstants
import dev.nelson.mot.main.util.extention.leaveOnlyDigits
import dev.nelson.mot.main.util.toFormattedDate
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class PaymentDetailsViewModel @Inject constructor(
    private val modifyPaymentUseCase: ModifyPaymentUseCase,
    getPaymentUseCase: GetPaymentUseCase,
    getCategoriesOrderedByName: GetCategoriesOrderedByNameFavoriteFirstUseCase,
    getStartOfCurrentMonthTimeUseCase: GetStartOfCurrentMonthTimeUseCase,
    handle: SavedStateHandle
) : BaseViewModel() {

    //states
    val paymentNameState
        get() = _paymentName.asStateFlow()
    val costState
        get() = _cost.asStateFlow()
    val messageState
        get() = _message.asStateFlow()
    val categoryNameState
        get() = _categoryName.asStateFlow()
    val dateState
        get() = _date.asStateFlow()
    val categoriesState: Flow<List<Category>>
        get() = _categories

    // actions
    val finishAction
        get() = _finishAction.asSharedFlow()
    private val _finishAction = MutableSharedFlow<Unit>()
    val onDateClickAction
        get() = _onDateClickAction.asSharedFlow()
    private val _onDateClickAction = MutableSharedFlow<Unit>()

    // data
    private val paymentId: Int? = handle.get<Int>("id")
    private val mode: SavePaymentMode = paymentId?.let { SavePaymentMode.Edit } ?: SavePaymentMode.Add
    private val _categories: Flow<List<Category>> = getCategoriesOrderedByName.execute()
    private val _paymentName = MutableStateFlow(TextFieldValue()) // _ before name means mutable
    private val _cost = MutableStateFlow(TextFieldValue())
    private val _message = MutableStateFlow(TextFieldValue())
    private val _categoryName = MutableStateFlow("Category")
    private val _date = MutableStateFlow("")
    private var selectedCategory: Category? = null
    private var initialPayment: Payment? = null
    private var dateInMills = 0L
    private val calendar: Calendar by lazy { Calendar.getInstance() }

    init {
        viewModelScope.launch {
            paymentId?.let { paymentId ->
                getPaymentUseCase.execute(paymentId)
                    .catch { exception -> handleThrowable(exception) }
                    .collect {
                        initialPayment = it
                        _paymentName.value = TextFieldValue(it.name, selection = TextRange(it.name.length))
                        _cost.value = TextFieldValue(it.cost.toString(), selection = TextRange(it.cost.toString().length))
                        _message.value = TextFieldValue(it.message, selection = TextRange(it.message.length))
                        selectedCategory = it.category
                        dateInMills = it.dateInMills ?: DateUtils.getCurrentDate().time
                        setDate(dateInMills)
                        it.category?.name?.let { categoryName -> _categoryName.value = categoryName }
                    }
            } ?: kotlin.run {
                setInitialDate()
            }
        }

        viewModelScope.launch {
            val startOfTheMonth = getStartOfCurrentMonthTimeUseCase.execute()
            Timber.d("startOfTheMonth:$startOfTheMonth")
        }
    }

    fun onSaveClick() {
        when (mode) {
            SavePaymentMode.Add -> addNewPayment()
            SavePaymentMode.Edit -> editPayment()
        }
    }

    fun onDateClick() {
        Timber.e("on Date click")
        onDateClickAction.launchIn(viewModelScope)
    }

    fun onDateSet(selectedYear: Int, monthOfYear: Int, dayOfMonth: Int) {
        val selectedDateCalendar = calendar.apply { set(selectedYear, monthOfYear, dayOfMonth) }
        val selectedDate = selectedDateCalendar.time
        setDate(selectedDate.time)
    }

    fun onPaymentNameChanged(textFieldValue: TextFieldValue) {
        _paymentName.value = textFieldValue
    }

    fun onMessageChanged(textFieldValue: TextFieldValue) {
        _message.value = textFieldValue
    }

    fun onCostChange(textFieldValue: TextFieldValue) {
//        cost.value = cost.value?.copy(text = formatAmountOrMessage(textFieldValue.text))
        _cost.value = textFieldValue
    }

    fun onCategorySelected(category: Category) {
        selectedCategory = category
        _categoryName.value = category.name
    }

    private fun addNewPayment() {
        viewModelScope.launch {
            val currentDateInMills = System.currentTimeMillis()
            val payment = Payment(
                _paymentName.value.text,
                (_cost.value.text.leaveOnlyDigits().toIntOrNull() ?: 0),
                dateInMills = currentDateInMills,
                category = selectedCategory,
                message = _message.value.text
            )
            modifyPaymentUseCase.execute(payment, ModifyPaymentAction.Add)
            Timber.e("payment $payment")
            _finishAction.emit(Unit)
        }
    }

    private fun editPayment() {
        viewModelScope.launch {
            val payment = Payment(
                name = _paymentName.value.text,
                cost = _cost.value.text.leaveOnlyDigits().toIntOrNull() ?: 0,
                message = _message.value.text,
                id = initialPayment?.id,
                date = initialPayment?.date,
                dateInMills = dateInMills,
                category = selectedCategory
            )
            if (initialPayment != payment) {
                modifyPaymentUseCase.execute(payment, ModifyPaymentAction.Edit)
            }
            Timber.e("updated payment $payment")
            _finishAction.emit(Unit)
        }
    }

    private fun setInitialDate() {
        dateInMills = DateUtils.getCurrentDate().time
        setDate(dateInMills)
    }

    private fun setDate(dateInMills: Long) {
        this.dateInMills = dateInMills
        val dateFromMills = DateUtils.createDateFromMills(dateInMills)
        val dateTextFormatted = dateFromMills.toFormattedDate(NetworkConstants.DATE_FORMAT)
        _date.value = dateTextFormatted
    }

    private sealed class SavePaymentMode {
        object Add : SavePaymentMode()
        object Edit : SavePaymentMode()
    }

    /**
     * Returns true if text contains maximum 6 digits
     */
    val String.isValidFormattableAmount
        get(): Boolean = isNotBlank()
//        && isDigitsOnly()
            && length <= 7

    /**
     * If [input] only include digits, it returns a formatted amount.
     * Otherwise returns plain input as it is
     */
    fun formatAmountOrMessage(
        input: String
    ): String = if (input.isValidFormattableAmount) {
        DecimalFormat("## ###.##").format(input.toInt())
    } else {
        input
    }
}
