package dev.nelson.mot.main.presentations.screen.payment_details

import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.nelson.mot.main.data.model.Category
import dev.nelson.mot.main.data.model.Payment
import dev.nelson.mot.main.domain.use_case.category.GetCategoriesOrderedByNameFavoriteFirstUseCase
import dev.nelson.mot.main.domain.use_case.date_and_time.GetStartOfCurrentMonthTimeUseCase
import dev.nelson.mot.main.domain.use_case.base.execute
import dev.nelson.mot.main.domain.use_case.payment.GetPaymentByIdUseCase
import dev.nelson.mot.main.domain.use_case.payment.ModifyPaymentAction
import dev.nelson.mot.main.domain.use_case.payment.ModifyPaymentParams
import dev.nelson.mot.main.domain.use_case.payment.ModifyPaymentUseCase
import dev.nelson.mot.main.presentations.base.BaseViewModel
import dev.nelson.mot.main.util.DateUtils
import dev.nelson.mot.db.utils.SortingOrder
import dev.nelson.mot.main.util.constant.Constants
import dev.nelson.mot.main.util.extention.convertMillisecondsToDate
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*
import javax.inject.Inject

@HiltViewModel
class PaymentDetailsViewModel @Inject constructor(
    getCategoriesOrderedByName: GetCategoriesOrderedByNameFavoriteFirstUseCase,
    getStartOfCurrentMonthTimeUseCase: GetStartOfCurrentMonthTimeUseCase,
    handle: SavedStateHandle,
    private val getPaymentByIdUseCase: GetPaymentByIdUseCase,
    private val modifyPaymentUseCase: ModifyPaymentUseCase
) : BaseViewModel() {

    //states
    val paymentNameState
        get() = _paymentName.asStateFlow()
    private val _paymentName = MutableStateFlow(TextFieldValue()) // _ before name means mutable

    val costState
        get() = _cost.asStateFlow()
    private val _cost = MutableStateFlow(TextFieldValue())

    val messageState
        get() = _message.asStateFlow()
    private val _message = MutableStateFlow(TextFieldValue())

    val selectedCategoryState
        get() = _selectedCategoryState.asStateFlow()
    private val _selectedCategoryState = MutableStateFlow<Category?>(null)

    val dateState
        get() = _date.asStateFlow()
    private val _date = MutableStateFlow("")

    val categoriesState: Flow<List<Category>>
        get() = _categories
    private val _categories: Flow<List<Category>> =
        getCategoriesOrderedByName.execute(SortingOrder.Ascending)

    // actions
    val finishAction
        get() = _finishAction.asSharedFlow()
    private val _finishAction = MutableSharedFlow<Unit>()

    val onDateClickAction
        get() = _onDateClickAction.asSharedFlow()
    private val _onDateClickAction = MutableSharedFlow<Unit>()

    // private data
    private val paymentId: Int? = handle.get<Int>("id")
    private val mode: SavePaymentMode =
        paymentId?.let { SavePaymentMode.Edit } ?: SavePaymentMode.Add
//    private var selectedCategory: Category? = null
    private var initialPayment: Payment? = null
    private var dateInMills = 0L
    private val calendar: Calendar by lazy { Calendar.getInstance() }

    init {
        initializePaymentData()

        launch {
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
        val formattedPrice = formatAmountOrMessage(textFieldValue.text)
        _cost.value = textFieldValue.copy(
            text = formattedPrice,
            selection = TextRange(formattedPrice.length)
        )
    }

    fun onCategorySelected(category: Category) {
        _selectedCategoryState.value = category
    }

    private fun initializePaymentData() = launch {
        paymentId?.let { paymentId ->
            getPaymentByIdUseCase.execute(paymentId)
                .catch { exception -> handleThrowable(exception) }
                .collect {
                    initialPayment = it
                    _paymentName.value =
                        TextFieldValue(it.name, selection = TextRange(it.name.length))
                    val formattedPrice =
                        formatAmountOrMessage((it.cost.toDouble() / 100).toString())
                    _cost.value = TextFieldValue(
                        formatAmountOrMessage(formattedPrice),
                        selection = TextRange(formattedPrice.length)
                    )
                    _message.value =
                        TextFieldValue(it.message, selection = TextRange(it.message.length))
                    dateInMills = it.dateInMills ?: System.currentTimeMillis()
                    setDate(dateInMills)
                    _selectedCategoryState.value = it.category
//                    it.category?.name?.let { categoryName -> _categoryName.value = categoryName }
                }
        } ?: kotlin.run {
            setInitialDate()
        }
    }

    private fun addNewPayment() = launch {
        val priceToSave = formatPriceToSave(_cost.value.text)
        val payment = Payment(
            _paymentName.value.text,
            cost = priceToSave,
            dateInMills = dateInMills,
            category = selectedCategoryState.value,
            message = _message.value.text
        )
        val params = ModifyPaymentParams(payment, ModifyPaymentAction.Add)
        modifyPaymentUseCase.execute(params)
        Timber.e("payment $payment")
        _finishAction.emit(Unit)
    }

    private fun editPayment() = launch {
        val priceToSave = formatPriceToSave(_cost.value.text)
        val payment = Payment(
            name = _paymentName.value.text,
            cost = priceToSave,
            message = _message.value.text,
            id = initialPayment?.id,
            dateString = initialPayment?.dateString,
            dateInMills = dateInMills,
            category = selectedCategoryState.value
        )
        if (initialPayment != payment) {
            val params = ModifyPaymentParams(payment, ModifyPaymentAction.Edit)
            modifyPaymentUseCase.execute(params)
        }
        Timber.e("updated payment $payment")
        _finishAction.emit(Unit)
    }

    private fun setInitialDate() {
        dateInMills = DateUtils.getCurrentDate().time
        setDate(dateInMills)
    }

    private fun setDate(dateInMills: Long) {
        this.dateInMills = dateInMills
//        val dateFromMills = DateUtils.createDateFromMills(dateInMills)
//        val dateTextFormatted = dateFromMills.toFormattedDate(NetworkConstants.DATE_FORMAT)
        _date.value = dateInMills.convertMillisecondsToDate(Constants.DAY_SHORT_MONTH_YEAR_DATE_PATTERN)
    }

    private sealed class SavePaymentMode {
        object Add : SavePaymentMode()
        object Edit : SavePaymentMode()
    }

    /**
     * Returns true if text contains maximum 6 digits
     */
    private val String.isValidFormattableAmount
        get(): Boolean = isNotBlank()
//        && isDigitsOnly()
                && length <= 7

    /**
     * If [input] only include digits, it returns a formatted amount.
     * Otherwise returns plain input as it is
     */
    fun formatAmountOrMessage(input: String): String {
        // remove forbidden  symbols
        val textWithoutForbiddenSymbols = input.replace(Regex("[^\\d.,]"), "")
        // replace ',' with '.'
        val textWithDots = textWithoutForbiddenSymbols.replace(",", ".")
        val rawPriceTxt = if (textWithDots.contains(".")) {
            val firstDotIndex = textWithDots.indexOf(".")
            var digitsBeforeDot = textWithDots.substring(0, firstDotIndex)
            if (digitsBeforeDot.isEmpty()) digitsBeforeDot = "0"
            if (digitsBeforeDot.length > 7) {
                digitsBeforeDot = digitsBeforeDot.substring(0, 7)
            }
            val charactersAfterDot = textWithDots.substring(firstDotIndex)
            val digitsAfterDot = charactersAfterDot.replace(Regex("\\D"), "")
            val cents = when {
                digitsAfterDot.isEmpty() -> ""
                digitsAfterDot.length == 1 -> digitsAfterDot.first()
                else -> digitsAfterDot.substring(0, 2) // 2 and more
            }
            "$digitsBeforeDot.$cents"
        } else {
            var textWithoutDots = textWithDots
            if (textWithoutDots.length >= 2 && textWithoutDots.first() == '0' && textWithoutDots[1] == '0') {
                textWithoutDots = "0"
            }
            if (textWithoutDots.length >= 2 && textWithoutDots.first() == '0' && textWithoutDots[1] != '0') {
                textWithoutDots = textWithoutDots[1].toString()
            }
            if (textWithoutDots.length > 7) {
                textWithoutDots.substring(0, 7)
            } else {
                textWithoutDots
            }
        }
        return rawPriceTxt
    }

    fun formatPriceToSave(priceText: String): Int {
        if (priceText.isEmpty()) return 0
        val textWithDots = priceText.replace(",", ".")
        val rawPriceTxt = if (textWithDots.contains(".")) {
            val firstDotIndex = textWithDots.indexOf(".")
            var digitsBeforeDot = textWithDots.substring(0, firstDotIndex)
            if (digitsBeforeDot.isEmpty()) digitsBeforeDot = "0"
            if (digitsBeforeDot.length > 8) {
                digitsBeforeDot = digitsBeforeDot.substring(0, 8)
            }
            val charactersAfterDot = textWithDots.substring(firstDotIndex)
            val digitsAfterDot = charactersAfterDot.replace(Regex("\\D"), "")
            val cents = when {
                digitsAfterDot.isEmpty() -> "00"
                digitsAfterDot.length == 1 -> "${digitsAfterDot.first()}0"
                else -> digitsAfterDot.substring(0, 2) // 2 and more
            }
            "$digitsBeforeDot$cents"
        } else {
            "${textWithDots}00"
        }
        return rawPriceTxt.toInt()
    }

}
