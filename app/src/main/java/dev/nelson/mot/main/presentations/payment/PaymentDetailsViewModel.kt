package dev.nelson.mot.main.presentations.payment

import android.icu.text.DecimalFormat
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.nelson.mot.main.data.model.Category
import dev.nelson.mot.main.data.model.Payment
import dev.nelson.mot.main.domain.use_case.category.GetCategoriesOrderedByNameFavoriteFirstUseCase
import dev.nelson.mot.main.domain.use_case.payment.AddNewPaymentUseCase
import dev.nelson.mot.main.domain.use_case.payment.EditPaymentUseCase
import dev.nelson.mot.main.domain.use_case.payment.GetPaymentUseCase
import dev.nelson.mot.main.presentations.base.BaseViewModel
import dev.nelson.mot.main.util.DateUtils
import dev.nelson.mot.main.util.SingleLiveEvent
import dev.nelson.mot.main.util.constant.NetworkConstants
import dev.nelson.mot.main.util.toFormattedDate
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class PaymentDetailsViewModel @Inject constructor(
    private val addNewPaymentUseCase: AddNewPaymentUseCase,
    private val editPaymentUseCase: EditPaymentUseCase,
    getPaymentUseCase: GetPaymentUseCase,
    getCategoriesOrderedByName: GetCategoriesOrderedByNameFavoriteFirstUseCase,
    handle: SavedStateHandle
) : BaseViewModel() {

    //states
    val paymentNameState
        get() = _paymentName
    val costState
        get() = _cost
    val messageState
        get() = _message
    val categoryNameState
        get() = _categoryName
    val dateState
        get() = _date
    val categoriesState: Flow<List<Category>>
        get() = _categories

    // actions
    val finishAction = SingleLiveEvent<Unit>()
    val onDateClickAction = SingleLiveEvent<Unit>()

    // data
    private val _paymentId: Int? = handle.get<Int>("id")
    private val _mode: SavePaymentMode = _paymentId?.let { SavePaymentMode.Edit } ?: SavePaymentMode.Add
    private val _categories: Flow<List<Category>> = getCategoriesOrderedByName.execute()
    private val _paymentName = MutableLiveData(TextFieldValue())
    private val _cost = MutableLiveData(TextFieldValue())
    private val _message = MutableLiveData(TextFieldValue())
    private val _categoryName = MutableLiveData("Category")
    private val _date = MutableLiveData("")
    private var _selectedCategory: Category? = null
    private var _initialPayment: Payment? = null
    private var _dateInMills = 0L
    private val _calendar: Calendar by lazy { Calendar.getInstance() }

    init {
        viewModelScope.launch {
            _paymentId?.let { paymentId ->
                getPaymentUseCase.execute(paymentId).collect {
                    _initialPayment = it
                    _paymentName.value = TextFieldValue(it.name, selection = TextRange(it.name.length))
                    _cost.value = TextFieldValue(it.cost.toString(), selection = TextRange(it.cost.toString().length))
                    _message.value = TextFieldValue(it.message, selection = TextRange(it.message.length))
                    _selectedCategory = it.category
                    _dateInMills = it.dateInMills ?: DateUtils.getCurrentDate().time
                    setDate(_dateInMills)
                    it.category?.name?.let { categoryName -> _categoryName.value = categoryName }
                }
            } ?: kotlin.run {
                setInitialDate()
            }
        }
    }

    fun onSaveClick() {
        when (_mode) {
            SavePaymentMode.Add -> addNewPayment()
            SavePaymentMode.Edit -> editPayment()
        }
    }

    fun onDateClick() {
        Timber.e("on Date click")
        onDateClickAction.call()
    }

    fun onDateSet(selectedYear: Int, monthOfYear: Int, dayOfMonth: Int) {
        val selectedDateCalendar = _calendar.apply { set(selectedYear, monthOfYear, dayOfMonth) }
        val selectedDate = selectedDateCalendar.time
        setDate(selectedDate.time)
    }

    fun onCostChange(textFieldValue: TextFieldValue) {
//        cost.value = cost.value?.copy(text = formatAmountOrMessage(textFieldValue.text))
        _cost.value = textFieldValue
    }

    fun onCategorySelected(category: Category) {
        _selectedCategory = category
        _categoryName.value = category.name
    }

    private fun addNewPayment() {
        viewModelScope.launch {
            val currentDateInMills = System.currentTimeMillis()
            val payment = Payment(
                _paymentName.value?.text.orEmpty(),
                (_cost.value?.text?.toIntOrNull() ?: 0),
                dateInMills = currentDateInMills,
                category = _selectedCategory,
                message = _message.value?.text.orEmpty()
            )
            addNewPaymentUseCase.execute(payment)
            Timber.e("payment $payment")
            finishAction.call()
        }
    }

    private fun editPayment() {
        viewModelScope.launch {
            val payment = Payment(
                name = _paymentName.value?.text.orEmpty(),
                cost = _cost.value?.text?.toIntOrNull() ?: 0,
                message = _message.value?.text.orEmpty(),
                id = _initialPayment?.id,
                date = _initialPayment?.date,
                dateInMills = _dateInMills,
                category = _selectedCategory
            )
            //todo, check if payment has been edited, if not, just close screen
            if (_initialPayment != payment) {
                editPaymentUseCase.execute(payment)
            }
            Timber.e("updated payment $payment")
            finishAction.call()
        }
    }

    private fun setInitialDate() {
        _dateInMills = DateUtils.getCurrentDate().time
        setDate(_dateInMills)
    }

    private fun setDate(dateInMills: Long) {
        this._dateInMills = dateInMills
        val dateFromMills = DateUtils.createDateFromMills(dateInMills)
        val dateTextFormatted = dateFromMills.toFormattedDate(NetworkConstants.DATE_FORMAT)
        dateState.value = dateTextFormatted
    }

    private sealed class SavePaymentMode {
        object Add : SavePaymentMode()
        object Edit : SavePaymentMode()
    }

    /**
     * Transforms a [LiveData] into [MutableLiveData]
     *
     * @param T type
     * @return [MutableLiveData] emitting the same values
     */
    fun <T> LiveData<T>.toMutableLiveData(): MutableLiveData<T> {
        val mediatorLiveData = MediatorLiveData<T>()
        mediatorLiveData.addSource(this) {
            mediatorLiveData.value = it
        }
        return mediatorLiveData
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
