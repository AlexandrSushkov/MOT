package dev.nelson.mot.main.presentations.screen.payment_list

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.DismissValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Snackbar
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import dev.nelson.mot.main.data.model.Payment
import dev.nelson.mot.main.presentations.widgets.MotDismissibleListItem
import dev.nelson.mot.main.presentations.widgets.TopAppBarMot
import dev.nelson.mot.main.util.compose.PreviewData

@Composable
fun PaymentListScreen(
    openDrawer: () -> Unit,
    onActionIconClick: () -> Unit,
    openPaymentDetails: (Int?) -> Unit
) {
    val viewModel = hiltViewModel<PaymentListViewModel>()
    val payments by viewModel.paymentList.collectAsState(emptyList())

    PaymentListLayout(
        openDrawer,
        payments,
        onItemClick = { openPaymentDetails.invoke(it.id?.toInt()) },
        openPaymentDetails = { openPaymentDetails.invoke(null) },
        onActionIconClick = onActionIconClick
    )
}

@Composable
fun PaymentListLayout(
    openDrawer: () -> Unit,
    paymentList: List<Payment>,
    onItemClick: (Payment) -> Unit,
    openPaymentDetails: (Int?) -> Unit,
    onActionIconClick: () -> Unit,
) {
    val viewModel = hiltViewModel<PaymentListViewModel>()
    val snackbarVisibleState by viewModel.snackBarVisibilityState.collectAsState()
    val deletedItemsCount by viewModel.deletedItemsCount.collectAsState(0)

    Scaffold(
        topBar = {
            TopAppBarMot(
                title = "Payments list",
                onNavigationIconClick = openDrawer,
                onActionIconClick = onActionIconClick
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { openPaymentDetails.invoke(null) },
                content = { Icon(Icons.Default.Add, "categories fab") }
            )
        },
        snackbarHost = {
            if (snackbarVisibleState) {
                Snackbar(
                    action = {
                        androidx.compose.material3.TextButton(
                            onClick = { viewModel.onUndoDeleteClick() },
                            content = { Text("Undo") }
                        )
                    },
                    modifier = Modifier.padding(8.dp),
                    content = { Text(text = "$deletedItemsCount Deleted") }
                )
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            PaymentList(paymentList, onItemClick)
        }
    }

}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PaymentList(
    paymentList: List<Payment>,
    onItemClick: (Payment) -> Unit,
) {
    val viewModel = hiltViewModel<PaymentListViewModel>()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
    ) {
        items(paymentList,
            key = { it.id ?: 0 }
        ) { payment ->
            val dismissState = rememberDismissState(
                confirmStateChange = { dismissValue ->
                    if (dismissValue == DismissValue.DismissedToStart) {
                        viewModel.onSwipeToDelete(payment)
                        true
                    } else {
                        false
                    }
                }
            )
            MotDismissibleListItem(
                dismissState = dismissState,
                dismissContent = {
                    PaymentListItem(
                        payment,
                        onClick = { payment -> onItemClick.invoke(payment) },
                        dismissDirection = dismissState.dismissDirection
                    )
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PaymentListScreenPreview() {
    PaymentListLayout(
        openDrawer = {},
        paymentList = PreviewData.paymentListPreview,
        onItemClick = {},
        openPaymentDetails = {}
    ) {}
}
