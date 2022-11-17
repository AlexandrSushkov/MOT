package dev.nelson.mot.main.presentations.screen.payment_list

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.DismissValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Snackbar
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Filter
import androidx.compose.material.rememberDismissState
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.nelson.mot.main.data.model.Payment
import dev.nelson.mot.main.presentations.widgets.ListPlaceholder
import dev.nelson.mot.main.presentations.widgets.MotDismissibleListItem
import dev.nelson.mot.main.presentations.widgets.TopAppBarMot
import dev.nelson.mot.main.util.MotResult
import dev.nelson.mot.main.util.MotResult.Error
import dev.nelson.mot.main.util.MotResult.Loading
import dev.nelson.mot.main.util.MotResult.Success
import dev.nelson.mot.main.util.compose.PreviewData
import dev.nelson.mot.main.util.successOr

@Composable
fun PaymentListScreen(
    openDrawer: () -> Unit,
    onActionIconClick: () -> Unit,
    openPaymentDetails: (Int?) -> Unit,
    viewModel: PaymentListViewModel
) {
    val paymentListResult by viewModel.paymentListResult.collectAsState(Loading)
    val snackbarVisibilityState by viewModel.snackBarVisibilityState.collectAsState()
    val deletedItemsCount by viewModel.deletedItemsCount.collectAsState(0)

    PaymentListLayout(
        openDrawer = openDrawer,
        paymentListResult = paymentListResult,
        onItemClick = { openPaymentDetails.invoke(it.id?.toInt()) },
        openPaymentDetails = { openPaymentDetails.invoke(null) },
        onActionIconClick = onActionIconClick,
        snackbarVisibleState = snackbarVisibilityState,
        onUndoButtonClick = { viewModel.onUndoDeleteClick() },
        deletedItemsCount = deletedItemsCount,
        onSwipeToDeleteItem = { viewModel.onSwipeToDelete(it) }
    )
}

@Composable
fun PaymentListLayout(
    openDrawer: () -> Unit,
    paymentListResult: MotResult<List<Payment>>,
    onItemClick: (Payment) -> Unit,
    openPaymentDetails: (Int?) -> Unit,
    onActionIconClick: () -> Unit,
    snackbarVisibleState: Boolean,
    onUndoButtonClick: () -> Unit,
    deletedItemsCount: Int,
    onSwipeToDeleteItem: (Payment) -> Unit
) {

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
                        TextButton(
                            onClick = onUndoButtonClick,
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
            PaymentList(paymentListResult, onItemClick, onSwipeToDeleteItem)
        }
    }

}

@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
@Composable
fun PaymentList(
    paymentListResult: MotResult<List<Payment>>,
    onItemClick: (Payment) -> Unit,
    onSwipeToDeleteItem: (Payment) -> Unit
) {
    when (paymentListResult) {
        is Loading -> {
            Box(modifier = Modifier.fillMaxSize()) {
                CircularProgressIndicator(Modifier.align(Alignment.Center))
            }
        }
        is Success -> {
            val paymentList = paymentListResult.successOr(emptyList())
            if (paymentList.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize()) {
                    ListPlaceholder(
                        Modifier.align(Alignment.Center),
                        Icons.Default.Filter,
                        "Empty"
                    )
                }
            } else {
                Column() {
                    Row(modifier = Modifier
                        .padding(horizontal = 8.dp, vertical = 16.dp)
                        .clickable { }) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = paymentList.first().date.orEmpty(), modifier = Modifier.align(Alignment.Start))
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = paymentList.last().date.orEmpty(), modifier = Modifier.align(Alignment.End))
                        }
                    }
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                    ) {
                        item {

                        }
                        paymentList.forEach { payment ->
                            item(payment.id ?: 0) {
                                val dismissState = rememberDismissState(
                                    confirmStateChange = { dismissValue ->
                                        if (dismissValue == DismissValue.DismissedToStart) {
                                            onSwipeToDeleteItem.invoke(payment)
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
                            stickyHeader {
//                            PaymentListDateItem(date = "12/21/23")
                            }
                        }
                    }
                }
            }
        }
        is Error -> {
            Box(modifier = Modifier.fillMaxSize()) {
                ListPlaceholder(
                    Modifier.align(Alignment.Center),
                    Icons.Default.Error,
                    "error"
                )
            }
        }
    }


}

@Preview(showBackground = true)
@Composable
private fun PaymentListScreenPreview() {
    PaymentListLayout(
        openDrawer = {},
        paymentListResult = Success(PreviewData.paymentListPreview),
//        paymentListResult = Error(IllegalStateException("my error")),
        onItemClick = {},
        openPaymentDetails = {},
        onActionIconClick = {},
        onSwipeToDeleteItem = {},
        onUndoButtonClick = {},
        snackbarVisibleState = false,
        deletedItemsCount = 0
    )
}
