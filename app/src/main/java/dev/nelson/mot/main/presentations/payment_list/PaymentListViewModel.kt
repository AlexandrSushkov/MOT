package dev.nelson.mot.main.presentations.payment_list

import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
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
    var paymentListTitle = ObservableField(category.name.ifEmpty { "Recent Payments" })
    val onPaymentEntityItemClickAction: Relay<Payment> = PublishRelay.create()
    val onSwipeToDeleteAction: Relay<Payment> = PublishRelay.create()
    val onPaymentEntityItemClickEvent: SingleLiveEvent<Payment> = SingleLiveEvent()
    val swipeToDeleteAction: SingleLiveEvent<Unit> = SingleLiveEvent()
    val onScrollChanged: Relay<Int> = PublishRelay.create()

    val expandedLiveData: MutableLiveData<Boolean> = MutableLiveData()

    val paymentListLivaData = MutableLiveData<List<Payment>>()

    private var _paymentList: Flow<List<Payment>> = paymentUseCase
        .getAllPaymentsWithCategoryOrderDateDescFlow()

//        .asLiveData(viewModelScope.coroutineContext)

//    private var _paymentListFlow = paymentUseCase
//        .getAllPaymentsWithCategoryOrderDateDescFlow()
//        .collect { paymentAdapter.get()?.setData(it) }

    val paymentList: Flow<List<Payment>>
        get() = _paymentList
//        get() = flowOf(p)


    init {

        onPaymentEntityItemClickAction
            .applyThrottling()
            .doOnNext {
//                Timber.d("on payment $it click")
//                onPaymentEntityItemClickEvent.postValue(it)

//                val tempPayment = it.copy(isExpanded = !it.isExpanded)
//                val tempPaymentLIst =
//                _paymentList.
//                    _paymentList.map { it.toMutableList() }
//                        .map {
//                            val temp = it.first().copy(isExpanded = !it.first().isExpanded)
//                            it.set(0, temp)
//                        }

//                val e = expandedLiveData.value ?: false
//                expandedLiveData.value = !e
            }
            .subscribe()
            .addToDisposables()

        onSwipeToDeleteAction
            .doOnNext {
                deletePayment(it)
//                p.remove(it)
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

    fun onItemClick(payment: Payment) {
        Timber.d("on payment $payment click")
        onPaymentEntityItemClickEvent.postValue(payment)
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
