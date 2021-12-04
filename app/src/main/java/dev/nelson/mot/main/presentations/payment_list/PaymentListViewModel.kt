package dev.nelson.mot.main.presentations.payment_list

import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.*
import androidx.recyclerview.widget.ItemTouchHelper
import com.jakewharton.rxrelay2.PublishRelay
import com.jakewharton.rxrelay2.Relay
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.nelson.mot.main.data.model.Category
import dev.nelson.mot.main.data.model.Payment
import dev.nelson.mot.main.domain.use_case.payment.DeletePaymentUseCase
import dev.nelson.mot.main.domain.use_case.payment.PaymentUseCase
import dev.nelson.mot.main.presentations.base.BaseViewModel
import dev.nelson.mot.main.util.Constants
import dev.nelson.mot.main.util.SingleLiveEvent
import dev.nelson.mot.main.util.StringUtils
import dev.nelson.mot.main.util.extention.applyThrottling
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class PaymentListViewModel @Inject constructor(
    extras: SavedStateHandle,
    private val paymentUseCase: PaymentUseCase,
    private val deletePaymentUseCase: DeletePaymentUseCase
) : BaseViewModel() {

    private val mode = if ((extras.get<Category>(Constants.CATEGORY_KEY)) == null) Mode.RecentPayments else Mode.PaymentsForCategory
    private val category: Category = extras[Constants.CATEGORY_KEY] ?: Category(StringUtils.EMPTY)

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
    var paymentListTitle = ObservableField(if (category.name.isEmpty()) "Recent Payments" else category.name)
    val onPaymentEntityItemClickAction: Relay<Payment> = PublishRelay.create()
    val onSwipeToDeleteAction: Relay<Payment> = PublishRelay.create()
    val onPaymentEntityItemClickEvent: SingleLiveEvent<Payment> = SingleLiveEvent()
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
            getPaymentList(mode)
                .map { paymentList -> paymentList.map { PaymentListItemModel.PaymentItemModel(it) } }
                .collect { _paymentListAdapter.setData(it) }
        }

        onPaymentEntityItemClickAction
            .applyThrottling()
            .doOnNext {
                Timber.d("on payment $it click")
                onPaymentEntityItemClickEvent.postValue(it)
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

    private fun getPaymentList(mode: Mode): Flow<List<Payment>> {
        return when (mode) {
            is Mode.PaymentsForCategory -> category.id?.let { paymentUseCase.getAllPaymentsWithCategoryByCategoryOrderDateDescFlow(it) }
                ?: paymentUseCase.getAllPaymentsWithoutCategory()
            is Mode.RecentPayments -> paymentUseCase.getAllPaymentsWithCategoryOrderDateDescFlow()
        }
    }

    private fun deletePayment(payment: Payment) {
        viewModelScope.launch {
            deletePaymentUseCase.execute(payment)
        }
    }

    fun onFabClick() {
        onPaymentEntityItemClickEvent.call()
    }

    //    suspend fun loadPeyments(): List<Payment> {
//        return getSuperheroList()
//    }
    private sealed class Mode {
        object RecentPayments : Mode()
        object PaymentsForCategory : Mode()
    }

}
