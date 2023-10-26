package dev.nelson.mot.main.presentations.screen.paymentdetails

import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.nelson.mot.db.utils.SortingOrder
import dev.nelson.mot.main.data.model.Category
import dev.nelson.mot.main.data.model.Payment
import dev.nelson.mot.main.domain.usecase.base.execute
import dev.nelson.mot.main.domain.usecase.category.GetCategoriesOrderedByNameFavoriteFirstUseCase
import dev.nelson.mot.main.domain.usecase.category.GetCategoryByIdUseCase
import dev.nelson.mot.main.domain.usecase.date.GetStartOfCurrentMonthTimeUseCase
import dev.nelson.mot.main.domain.usecase.payment.GetPaymentByIdUseCase
import dev.nelson.mot.main.domain.usecase.payment.ModifyPaymentAction
import dev.nelson.mot.main.domain.usecase.payment.ModifyPaymentParams
import dev.nelson.mot.main.domain.usecase.payment.ModifyPaymentUseCase
import dev.nelson.mot.main.presentations.base.BaseViewModel
import dev.nelson.mot.main.presentations.sharedviewstate.DateViewState
import dev.nelson.mot.main.util.constant.Constants
import dev.nelson.mot.main.util.extention.formatMillsToDateText
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class PaymentDetailsViewModel @Inject constructor(
    getCategoriesOrderedByName: GetCategoriesOrderedByNameFavoriteFirstUseCase,
    getStartOfCurrentMonthTimeUseCase: GetStartOfCurrentMonthTimeUseCase,
    handle: SavedStateHandle,
    private val getPaymentByIdUseCase: GetPaymentByIdUseCase,
    private val getCategoryByIdUseCase: GetCategoryByIdUseCase,
    private val modifyPaymentUseCase: ModifyPaymentUseCase
) : BaseViewModel() {

    // states
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

    val dateViewState
        get() = _dateViewState.asStateFlow()
    private val _dateViewState = MutableStateFlow(DateViewState())

    val categoriesState: Flow<List<Category>>
        get() = _categories
    private val _categories: Flow<List<Category>> =
        getCategoriesOrderedByName.execute(SortingOrder.Ascending)

    // actions
    val finishAction
        get() = _finishAction.asSharedFlow()
    private val _finishAction = MutableSharedFlow<Unit>()

    val showDatePickerDialogState
        get() = _showDatePickerDialogState.asStateFlow()
    private val _showDatePickerDialogState = MutableStateFlow(false)

    // private data
    private val paymentId: Int? = handle.get<Int>(Constants.PAYMENT_ID_KEY)
    private val categoryId: Int? = handle.get<Int>(Constants.CATEGORY_ID_KEY)
    private val mode: SavePaymentMode =
        paymentId?.let { SavePaymentMode.Edit } ?: SavePaymentMode.Add

    private var initialPayment: Payment? = null

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
        _showDatePickerDialogState.value = true
    }

    fun onPaymentNameChanged(textFieldValue: TextFieldValue) {
        _paymentName.value = textFieldValue
    }

    fun onMessageChanged(textFieldValue: TextFieldValue) {
        _message.value = textFieldValue
    }

    fun onCostChange(textFieldValue: TextFieldValue) {
        val formattedPrice = formatAmountOrMessage(textFieldValue.text)
        _cost.value = textFieldValue.copy(
            text = formattedPrice,
            selection = TextRange(formattedPrice.length)
        )
    }

    fun onCategorySelected(category: Category) {
        _selectedCategoryState.value = category
    }

    fun onDismissDatePickerDialog() {
        _showDatePickerDialogState.value = false
    }

    fun onDateSelected(selectedTime: Long) {
        _dateViewState.update {
            it.copy(
                mills = selectedTime,
                text = selectedTime.formatMillsToDateText(Constants.DAY_SHORT_MONTH_YEAR_DATE_PATTERN)
            )
        }
        onDismissDatePickerDialog()
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
                    onDateSelected(it.dateInMills)
                    _selectedCategoryState.value = it.category
                }
        } ?: run {
            categoryId?.let { categoryId ->
                getCategoryByIdUseCase.execute(categoryId)
                    .catch { exception -> handleThrowable(exception) }
                    .collect { _selectedCategoryState.value = it }
            }
        }
    }

    private fun addNewPayment() = launch {
        val priceToSave = formatPriceToSave(_cost.value.text)
        val payment = Payment(
            _paymentName.value.text,
            cost = priceToSave,
            dateInMills = dateViewState.value.mills,
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
            dateInMills = dateViewState.value.mills,
            category = selectedCategoryState.value
        )
        if (initialPayment != payment) {
            val params = ModifyPaymentParams(payment, ModifyPaymentAction.Edit)
            modifyPaymentUseCase.execute(params)
        }
        Timber.e("updated payment $payment")
        _finishAction.emit(Unit)
    }

    private sealed class SavePaymentMode {
        data object Add : SavePaymentMode()
        data object Edit : SavePaymentMode()
    }

    /**
     * Returns true if text contains maximum 6 digits
     */
    private val String.isValidFormattableAmount
        get(): Boolean = isNotBlank() &&
//        && isDigitsOnly()
                length <= 7

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
