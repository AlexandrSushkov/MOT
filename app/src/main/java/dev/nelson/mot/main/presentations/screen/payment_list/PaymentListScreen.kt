@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)

package dev.nelson.mot.main.presentations.screen.payment_list

import android.app.Activity
import android.app.DatePickerDialog
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.DismissDirection
import androidx.compose.material.DismissValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Snackbar
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.EditCalendar
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Filter
import androidx.compose.material.rememberDismissState
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.nelson.mot.R
import dev.nelson.mot.core.ui.MotCloseIcon
import dev.nelson.mot.core.ui.MotDismissibleListItem
import dev.nelson.mot.core.ui.MotMaterialTheme
import dev.nelson.mot.core.ui.MotNavDrawerIcon
import dev.nelson.mot.core.ui.MotSelectionTopAppBar
import dev.nelson.mot.core.ui.MotTopAppBar
import dev.nelson.mot.core.ui.view_state.PriceViewState
import dev.nelson.mot.main.data.model.Category
import dev.nelson.mot.main.data.model.PaymentListItemModel
import dev.nelson.mot.main.presentations.screen.payment_details.CategoriesListBottomSheet
import dev.nelson.mot.main.presentations.screen.payment_list.actions.OpenPaymentDetailsAction
import dev.nelson.mot.main.presentations.widgets.ListPlaceholder
import dev.nelson.mot.main.presentations.widgets.MotModalBottomSheetLayout
import dev.nelson.mot.main.util.MotUiState
import dev.nelson.mot.main.util.MotUiState.Error
import dev.nelson.mot.main.util.MotUiState.Loading
import dev.nelson.mot.main.util.MotUiState.Success
import dev.nelson.mot.main.util.StringUtils
import dev.nelson.mot.main.util.compose.PreviewData
import dev.nelson.mot.main.util.compose.ifNotNull
import dev.nelson.mot.main.util.successOr
import dev.utils.preview.MotPreviewScreen
import kotlinx.coroutines.launch
import java.util.Calendar

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PaymentListScreen(
    navigationIcon: @Composable () -> Unit,
    openPaymentDetails: (Int?) -> Unit,
    viewModel: PaymentListViewModel
) {
    val haptic = LocalHapticFeedback.current
    val coroutineScope = rememberCoroutineScope()
    val modalBottomSheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)

    // listen states
    val toolbarTitle by viewModel.toolBarTitleState.collectAsState(StringUtils.EMPTY)
    val paymentListResult by viewModel.paymentListResult.collectAsState(Loading)
    val snackbarVisibilityState by viewModel.snackBarVisibilityState.collectAsState()
    val deletedItemsCount by viewModel.deletedItemsCountState.collectAsState(0)
    val isSelectedStateOn by viewModel.isSelectedModeOnState.collectAsState(false)
    val selectedItemsCount by viewModel.selectedItemsCountState.collectAsState(0)
    val categories by viewModel.categoriesState.collectAsState(emptyList())
    val priceViewState by viewModel.priceViewState.collectAsState(PriceViewState())

    /**
     * Open payment details
     */
    LaunchedEffect(
        key1 = Unit,
        block = {
            viewModel.openPaymentDetailsAction.collect { action ->
                when (action) {
                    is OpenPaymentDetailsAction.NewPayment -> openPaymentDetails.invoke(null)
                    is OpenPaymentDetailsAction.ExistingPayment -> openPaymentDetails.invoke(action.id)
                }
            }
        })

    /**
     * Back handler to cancel selection
     */
    BackHandler(
        enabled = isSelectedStateOn && modalBottomSheetState.isVisible.not(),
        onBack = { viewModel.onCancelSelectionClickEvent() }
    )

    /**
     * Back handler to hide modal bottom sheet
     */
    BackHandler(
        enabled = modalBottomSheetState.isVisible,
        onBack = { coroutineScope.launch { modalBottomSheetState.hide() } }
    )

    // TODO: move to VM
    val cldr: Calendar = Calendar.getInstance()
    val day: Int = cldr.get(Calendar.DAY_OF_MONTH)
    val month: Int = cldr.get(Calendar.MONTH)
    val year: Int = cldr.get(Calendar.YEAR)
    val picker = DatePickerDialog(
        LocalContext.current,
        { _, selectedYear, monthOfYear, dayOfMonth ->
            run {
                viewModel.onDateSet(
                    selectedYear,
                    monthOfYear,
                    dayOfMonth
                )
            }
        },
        year,
        month,
        day
    )

    PaymentListLayout(
        navigationIcon = navigationIcon,
        toolbarTitle = toolbarTitle,
        paymentListResult = paymentListResult,
        onItemClick = { paymentItemModel -> viewModel.onItemClick(paymentItemModel) },
        onItemLongClick = { paymentItemModel -> viewModel.onItemLongClick(paymentItemModel) },
        onFabClick = { viewModel.onFabClick() },
        snackbarVisibleState = snackbarVisibilityState,
        onUndoButtonClickEvent = {
            viewModel.onUndoDeleteClick()
            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
        },
        deletedItemsCount = deletedItemsCount,
        onSwipeToDeleteItem = { paymentItemModel -> viewModel.onSwipeToDelete(paymentItemModel) },
        isSelectedStateOn = isSelectedStateOn,
        selectedItemsCount = selectedItemsCount,
        onCancelSelectionClick = {
            viewModel.onCancelSelectionClickEvent()
            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
        },
        onDeleteSelectedItemsClick = { viewModel.onDeleteSelectedItemsClick() },
        onChangeDateForSelectedItemsClick = { picker.show() },
        categories = categories,
        onCategoryClick = { category -> viewModel.onCategorySelected(category) },
        modalBottomSheetState = modalBottomSheetState,
        priceViewState = priceViewState
    )
}

@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun PaymentListLayout(
    navigationIcon: @Composable () -> Unit,
    toolbarTitle: String,
    paymentListResult: MotUiState<List<PaymentListItemModel>>,
    onItemClick: (PaymentListItemModel.PaymentItemModel) -> Unit,
    onItemLongClick: (PaymentListItemModel.PaymentItemModel) -> Unit,
    onFabClick: () -> Unit,
    snackbarVisibleState: Boolean,
    onUndoButtonClickEvent: () -> Unit,
    deletedItemsCount: Int,
    onSwipeToDeleteItem: (PaymentListItemModel.PaymentItemModel) -> Unit,
    isSelectedStateOn: Boolean,
    selectedItemsCount: Int,
    onCancelSelectionClick: () -> Unit,
    onDeleteSelectedItemsClick: () -> Unit,
    onChangeDateForSelectedItemsClick: () -> Unit,
    categories: List<Category>,
    onCategoryClick: (Category) -> Unit,
    modalBottomSheetState: ModalBottomSheetState,
    priceViewState: PriceViewState,
) {
    val appBarScrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val paymentsLitScrollingState = rememberLazyListState()

    MotModalBottomSheetLayout(
        sheetContent = {
            CategoriesListBottomSheet(
                categories,
                onCategoryClick,
                modalBottomSheetState
            )
        },
        sheetState = modalBottomSheetState
    ) {
        Scaffold(
            topBar = {
                if (isSelectedStateOn) {
                    MotSelectionTopAppBar(
                        navigationIcon = {
                            MotCloseIcon {
                                onCancelSelectionClick.invoke()
                            }
                        },
                        title = selectedItemsCount.toString(),
                        actions = {
                            val scope = rememberCoroutineScope()

                            IconButton(onClick = onChangeDateForSelectedItemsClick) {
                                Icon(Icons.Default.EditCalendar, contentDescription = "")
                            }
                            IconButton(onClick = {
                                scope.launch {
                                    modalBottomSheetState.show()
                                }
                            }) {
                                Icon(Icons.Default.Category, contentDescription = "")
                            }
                            IconButton(onClick = onDeleteSelectedItemsClick) {
                                Icon(Icons.Default.Delete, contentDescription = "")
                            }
                        },
                    ).also {
                        val view = LocalView.current
                        if (!view.isInEditMode) {
                            val window = (view.context as Activity).window
                            window.statusBarColor =
                                MaterialTheme.colorScheme.tertiaryContainer.toArgb()
                        }
                    }
                } else {
                    MotTopAppBar(
                        appBarTitle = toolbarTitle,
                        navigationIcon = navigationIcon,
                        scrollBehavior = appBarScrollBehavior
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
                                onClick = onUndoButtonClickEvent,
                                content = { Text(stringResource(R.string.text_undo)) }
                            )
                        },
                        modifier = Modifier.padding(8.dp),
                        content = {
                            val deletedItemText = if (deletedItemsCount == 1) {
                                stringResource(R.string.text_deleted_item_format, deletedItemsCount)
                            } else {
                                stringResource(
                                    R.string.text_deleted_items_format,
                                    deletedItemsCount
                                )
                            }
                            Text(text = deletedItemText)
                        }
                    )
                }
            }
        ) { innerPadding ->
            Box(modifier = Modifier.padding(innerPadding)) {
                PaymentList(
                    paymentListResult,
                    onItemClick,
                    onItemLongClick,
                    onSwipeToDeleteItem,
                    isSelectedStateOn,
                    priceViewState,
                    paymentsLitScrollingState,
                    scrollBehavior = appBarScrollBehavior
                )
            }
        }
    }

}

@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
@Composable
fun PaymentList(
    paymentListResult: MotUiState<List<PaymentListItemModel>>,
    onItemClick: (PaymentListItemModel.PaymentItemModel) -> Unit,
    onItemLongClick: (PaymentListItemModel.PaymentItemModel) -> Unit,
    onSwipeToDeleteItem: (PaymentListItemModel.PaymentItemModel) -> Unit,
    isSelectedStateOn: Boolean,
    priceViewState: PriceViewState,
    state: LazyListState,
    scrollBehavior: TopAppBarScrollBehavior? = null
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
                Column(
                    modifier = Modifier
                        .ifNotNull(scrollBehavior) { Modifier.nestedScroll(it.nestedScrollConnection) }
                ) {
                    // date range widget
//                    val startDate =
//                        paymentList.firstOrNull { it is PaymentListItemModel.Header } as? PaymentListItemModel.Header
//                    val endDate =
//                        paymentList.findLast { it is PaymentListItemModel.Header } as? PaymentListItemModel.Header
//                    if (startDate != null && endDate != null) {
//                        DateRangeWidget(startDate.date, endDate.date)
//                    }

                    val checkBoxTransitionState = remember { MutableTransitionState(false) }
                    val transition = updateTransition(checkBoxTransitionState, label = "")

                    LazyColumn(
                        state = state,
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
                                        directions = if (isSelectedStateOn.not()) setOf(
                                            DismissDirection.EndToStart
                                        ) else emptySet(),
                                        dismissContent = {
                                            PaymentListItem(
                                                paymentListItemModel,
                                                onClick = { payment -> onItemClick.invoke(payment) },
                                                dismissDirection = dismissState.dismissDirection,
                                                onLongClick = { payment ->
                                                    onItemLongClick.invoke(
                                                        payment
                                                    )
                                                },
                                                isSelectedStateOn = isSelectedStateOn,
                                                priceViewState = priceViewState,
                                                checkBoxTransitionState = checkBoxTransitionState,
                                                transition = transition,
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

@OptIn(ExperimentalMaterialApi::class)
@MotPreviewScreen
@Composable
private fun PaymentListScreenLightPreview() {
    MotMaterialTheme {
        PaymentListLayout(
            navigationIcon = { MotNavDrawerIcon {} },
            toolbarTitle = "Title",
            paymentListResult = Success(PreviewData.paymentListItemsPreview),
//        paymentListResult = Error(IllegalStateException("my error")),
            onItemClick = {},
            onItemLongClick = {},
            onFabClick = {},
            snackbarVisibleState = false,
            onUndoButtonClickEvent = {},
            deletedItemsCount = 0,
            onSwipeToDeleteItem = {},
            isSelectedStateOn = false,
            selectedItemsCount = 0,
            onCancelSelectionClick = {},
            onDeleteSelectedItemsClick = {},
            onChangeDateForSelectedItemsClick = {},
            categories = emptyList(),
            onCategoryClick = {},
            modalBottomSheetState = ModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden),
            priceViewState = PriceViewState()
        )
    }
}
