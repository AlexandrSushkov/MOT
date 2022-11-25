package dev.nelson.mot.main.presentations.screen.payment_list

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.DismissDirection
import androidx.compose.material.DismissValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Snackbar
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.EditCalendar
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Filter
import androidx.compose.material.rememberDismissState
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.nelson.mot.main.data.model.PaymentListItemModel
import dev.nelson.mot.main.presentations.screen.payment_list.actions.OpenPaymentDetailsAction
import dev.nelson.mot.main.presentations.widgets.ListPlaceholder
import dev.nelson.mot.main.presentations.widgets.MotDismissibleListItem
import dev.nelson.mot.main.presentations.widgets.MotSelectionTopAppBar
import dev.nelson.mot.main.presentations.widgets.TopAppBarMot
import dev.nelson.mot.main.util.MotResult
import dev.nelson.mot.main.util.MotResult.Error
import dev.nelson.mot.main.util.MotResult.Loading
import dev.nelson.mot.main.util.MotResult.Success
import dev.nelson.mot.main.util.compose.PreviewData
import dev.nelson.mot.main.util.successOr
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch

@Composable
fun PaymentListScreen(
    openDrawer: () -> Unit,
    onActionIconClick: () -> Unit,
    openPaymentDetails: (Int?) -> Unit,
    viewModel: PaymentListViewModel
) {
    val haptic = LocalHapticFeedback.current

    // listen states
    val paymentListResult by viewModel.paymentListResult.collectAsState(Loading)
    val snackbarVisibilityState by viewModel.snackBarVisibilityState.collectAsState()
    val deletedItemsCount by viewModel.deletedItemsCount.collectAsState(0)
    val isSelectedState by viewModel.isSelectedState.collectAsState(false)
    val selectedItemsCount by viewModel.selectedItemsCount.collectAsState(0)

    // listen actions
    LaunchedEffect(
        key1 = Unit,
        block = {
            viewModel.openPaymentDetailsAction.collect {
                when (it) {
                    is OpenPaymentDetailsAction.NewPayment -> openPaymentDetails.invoke(null)
                    is OpenPaymentDetailsAction.ExistingPayment -> openPaymentDetails.invoke(it.id)
                }
            }
        })

    BackHandler(
        enabled = isSelectedState,
        onBack = { viewModel.onCancelSelectionClick() }
    )

    PaymentListLayout(
        openDrawer = openDrawer,
        paymentListResult = paymentListResult,
//        onItemClick = { payment -> openPaymentDetails.invoke(payment.payment.id?.toInt()) },
        onItemClick = { paymentItemModel -> viewModel.onItemClick(paymentItemModel) },
        onItemLongClick = { paymentItemModel -> viewModel.onItemLongClick(paymentItemModel) },
//        openPaymentDetails = { openPaymentDetails.invoke(null) },
        onFabClick = { viewModel.onFabClick() },
        onActionIconClick = onActionIconClick,
        snackbarVisibleState = snackbarVisibilityState,
        onUndoButtonClick = {
            viewModel.onUndoDeleteClick()
            haptic.performHapticFeedback(HapticFeedbackType.LongPress)

        },
        deletedItemsCount = deletedItemsCount,
        onSwipeToDeleteItem = { paymentItemModel -> viewModel.onSwipeToDelete(paymentItemModel) },
        isSelectedState = isSelectedState,
        selectedItemsCount = selectedItemsCount,
        onCancelSelectionClick = {
            viewModel.onCancelSelectionClick()
            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
        },
        onDeleteSelectedItemsClick = { viewModel.onDeleteSelectedItemsClick() },
    )
}

@Composable
fun PaymentListLayout(
    openDrawer: () -> Unit,
    paymentListResult: MotResult<List<PaymentListItemModel>>,
    onItemClick: (PaymentListItemModel.PaymentItemModel) -> Unit,
    onItemLongClick: (PaymentListItemModel.PaymentItemModel) -> Unit,
    onFabClick: () -> Unit,
    onActionIconClick: () -> Unit,
    snackbarVisibleState: Boolean,
    onUndoButtonClick: () -> Unit,
    deletedItemsCount: Int,
    onSwipeToDeleteItem: (PaymentListItemModel.PaymentItemModel) -> Unit,
    isSelectedState: Boolean,
    selectedItemsCount: Int,
    onCancelSelectionClick: () -> Unit,
    onDeleteSelectedItemsClick: () -> Unit,
) {

    Scaffold(
        topBar = {
            if (isSelectedState) {
                MotSelectionTopAppBar(
                    onNavigationIconClick = onCancelSelectionClick,
                    title = selectedItemsCount.toString(),
                    actions = {
                        IconButton(onClick = {}) {
                            Icon(Icons.Default.EditCalendar, contentDescription = "")
                        }
                        IconButton(onClick = {}) {
                            Icon(Icons.Default.Category, contentDescription = "")
                        }
                        IconButton(onClick = onDeleteSelectedItemsClick) {
                            Icon(Icons.Default.Delete, contentDescription = "")
                        }
                    }
                )
            } else {
                TopAppBarMot(
                    title = "Payments list",
                    onNavigationIconClick = openDrawer,
                    onActionIconClick = onActionIconClick
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onFabClick,
                content = { Icon(Icons.Default.Add, "new payment fab") }
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
            PaymentList(paymentListResult, onItemClick, onItemLongClick, onSwipeToDeleteItem, isSelectedState)
        }
    }

}

@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
@Composable
fun PaymentList(
    paymentListResult: MotResult<List<PaymentListItemModel>>,
    onItemClick: (PaymentListItemModel.PaymentItemModel) -> Unit,
    onItemLongClick: (PaymentListItemModel.PaymentItemModel) -> Unit,
    onSwipeToDeleteItem: (PaymentListItemModel.PaymentItemModel) -> Unit,
    isSelectedState: Boolean
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
                Column {
                    // date range widget
                    val startDate = paymentList.firstOrNull() { it is PaymentListItemModel.Header } as? PaymentListItemModel.Header
                    val endDate = paymentList.findLast { it is PaymentListItemModel.Header } as? PaymentListItemModel.Header
                    if (startDate != null && endDate != null) {
                        DateRangeWidget(startDate.date, endDate.date)
                    }
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                    ) {
                        paymentList.forEach { paymentListItemModel ->
                            if (paymentListItemModel is PaymentListItemModel.PaymentItemModel) {
                                item(key = paymentListItemModel.key) {
                                    val dismissState = rememberDismissState(
                                        confirmStateChange = { dismissValue ->
                                            if (dismissValue == DismissValue.DismissedToStart) {
                                                onSwipeToDeleteItem.invoke(paymentListItemModel)
                                                true
                                            } else {
                                                false
                                            }
                                        }
                                    )
                                    MotDismissibleListItem(
                                        dismissState = dismissState,
                                        directions = if (isSelectedState.not()) setOf(DismissDirection.EndToStart) else emptySet(),
                                        dismissContent = {
                                            PaymentListItem(
                                                paymentListItemModel,
                                                onClick = { payment -> onItemClick.invoke(payment) },
                                                dismissDirection = dismissState.dismissDirection,
                                                onLongClick = { payment -> onItemLongClick.invoke(payment) },
                                                isSelectedState = isSelectedState
                                            )
                                        }
                                    )
                                }
                            }
                            if (paymentListItemModel is PaymentListItemModel.Header) {
                                stickyHeader(key = paymentListItemModel.key) {
                                    PaymentListDateItem(date = paymentListItemModel.date)
                                }
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
        paymentListResult = Success(PreviewData.paymentListItemsPreview),
//        paymentListResult = Error(IllegalStateException("my error")),
        onItemClick = {},
        onItemLongClick = {},
        onFabClick = {},
        onActionIconClick = {},
        snackbarVisibleState = false,
        onUndoButtonClick = {},
        deletedItemsCount = 0,
        onSwipeToDeleteItem = {},
        isSelectedState = false,
        selectedItemsCount = 0,
        onCancelSelectionClick = {},
        onDeleteSelectedItemsClick = {},
    )
}
