package dev.nelson.mot.main.presentations.payment_list

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.MutableLiveData
import dev.nelson.mot.main.data.model.Payment
import dev.nelson.mot.main.presentations.payment_list.compose.widgets.DismissiblePaymentListItem
import dev.nelson.mot.main.presentations.payment_list.compose.widgets.TopAppBarMot
import dev.nelson.mot.main.util.compose.PreviewData

@Preview(showBackground = true)
@Composable
fun PaymentListScreenPreview() {
    PaymentListLayout(
        openDrawer = {},
        paymentList = PreviewData.paymentListPreview,
        onItemClick = {},
        onSwipeToDelete = {},
        isExpanded = MutableLiveData(false),
        openPaymentDetails = {}
    )
}

@Composable
fun PaymentListScreen(openDrawer: () -> Unit, openPaymentDetails: (Int?) -> Unit) {
    val viewModel = hiltViewModel<PaymentListViewModel>()
    val payments by viewModel.paymentList.collectAsState(emptyList())
    val isExpanded = viewModel.expandedLiveData

    PaymentListLayout(
        openDrawer,
        payments,
        onItemClick = { openPaymentDetails.invoke(it.id?.toInt()) },
        onSwipeToDelete = { viewModel.deletePayment(it) },
        isExpanded,
        openPaymentDetails = { openPaymentDetails.invoke(null) }
    )
}

@Composable
fun PaymentListLayout(
    openDrawer: () -> Unit,
    paymentList: List<Payment>,
    onItemClick: (Payment) -> Unit,
    onSwipeToDelete: (Payment) -> Unit,
    isExpanded: MutableLiveData<Boolean>,
    openPaymentDetails: (Int?) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBarMot(
                title = "Payments list",
                onClick = openDrawer
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { openPaymentDetails.invoke(null) },
            ) {
                Icon(Icons.Default.Add, "categories fab")
            }
        },
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            PaymentList(paymentList, onItemClick, onSwipeToDelete, isExpanded)
        }
    }

}


@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
@Composable
fun PaymentList(
    paymentList: List<Payment>,
    onItemClick: (Payment) -> Unit,
    onSwipeToDelete: (Payment) -> Unit,
    isExpanded: MutableLiveData<Boolean>
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
    ) {
        items(paymentList,
            key = { it.id ?: 0 }
        ) {
            DismissiblePaymentListItem(
                payment = it,
                onClick = { payment -> onItemClick.invoke(payment) },
                onSwipeToDelete = { payment -> onSwipeToDelete.invoke(payment) },
                isExpanded = isExpanded
            )
        }
    }
}
