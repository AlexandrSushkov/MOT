package dev.nelson.mot.main.presentations.payment_list.compose

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.tooling.preview.Preview
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import com.jakewharton.rxrelay2.PublishRelay
import com.jakewharton.rxrelay2.Relay
import dagger.hilt.android.AndroidEntryPoint
import dev.nelson.mot.main.data.model.Payment
import dev.nelson.mot.main.presentations.base.BaseFragment
import dev.nelson.mot.main.presentations.payment_list.PaymentListViewModel
import dev.nelson.mot.main.presentations.payment_list.compose.widgets.DismissiblePaymentListItem
import dev.nelson.mot.main.presentations.payment_list.compose.widgets.TopAppBarMot
import dev.nelson.mot.main.util.compose.PreviewData

@AndroidEntryPoint
class PaymentListComposeFragment : BaseFragment() {

    private val viewModel: PaymentListViewModel by viewModels()
    private val navController by lazy { findNavController() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return ComposeView(requireContext()).apply {
            setContent {
                val payments by viewModel.paymentList.collectAsState(emptyList())
                val onSwipeToDelete = viewModel.onSwipeToDeleteAction
                val isExpanded = viewModel.expandedLiveData
                PaymentListComposeFragmentLayout(
                    payments,
                    onItemClick = { viewModel.onItemClick(it) },
                    onSwipeToDelete,
                    isExpanded
                )
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
    }

    private fun initListeners() {
        viewModel.onPaymentEntityItemClickEvent.observe(viewLifecycleOwner) {
            val openPaymentDetailsAction = PaymentListComposeFragmentDirections.goToPaymentDetailsCompose()
                .apply { payment = it }
            navController.navigate(openPaymentDetailsAction)
        }
    }
}

@Composable
fun PaymentListComposeFragmentLayout(
    paymentList: List<Payment>,
    onItemClick: (Payment) -> Unit,
    onSwipeToDelete: Relay<Payment>,
    isExpanded: MutableLiveData<Boolean>
) {
    Column() {
        TopAppBarMot(title = "payments list")
        PaymentList(paymentList, onItemClick, onSwipeToDelete, isExpanded)
    }
}

@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
@Composable
fun PaymentList(
    paymentList: List<Payment>,
    onItemClick: (Payment) -> Unit,
    onSwipeToDelete: Relay<Payment>,
    isExpanded: MutableLiveData<Boolean>
) {
    LazyColumn(
        Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
    ) {
        items(paymentList,
            key = { it.id ?: 0 }
        ) {
            DismissiblePaymentListItem(
                payment = it,
                onClick = { payment -> onItemClick.invoke(payment) },
                onSwipeToDelete = { payment -> onSwipeToDelete.accept(payment) },
                isExpanded = isExpanded
            )
        }
    }
}

@Preview(name = "PaymentListComposeFragment layout", showBackground = true)
@Composable
fun PaymentListComposeFragmentPreview() {
    PaymentListComposeFragmentLayout(
        PreviewData.paymentListPreview,
        {},
        PublishRelay.create(),
        MutableLiveData(false)
    )
}