@file:OptIn(ExperimentalMaterial3Api::class)

package dev.nelson.mot.main.presentations.screen.payment_list

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.EditCalendar
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.nestedscroll.nestedScroll
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
import dev.nelson.mot.core.ui.fundation.getDisplayCornerRadius
import dev.nelson.mot.core.ui.view_state.PriceViewState
import dev.nelson.mot.main.data.model.MotPaymentListItemModel
import dev.nelson.mot.main.presentations.screen.payment_list.actions.OpenPaymentDetailsAction
import dev.nelson.mot.main.presentations.shared.CategoriesListBottomSheet
import dev.nelson.mot.main.presentations.widgets.EmptyListPlaceholder
import dev.nelson.mot.main.presentations.widgets.FABFooter
import dev.nelson.mot.main.util.MotUiState
import dev.nelson.mot.main.util.MotUiState.Error
import dev.nelson.mot.main.util.MotUiState.Loading
import dev.nelson.mot.main.util.MotUiState.Success
import dev.nelson.mot.main.util.StringUtils
import dev.utils.MotTransitions
import dev.nelson.mot.main.util.compose.PreviewData
import dev.nelson.mot.main.util.extention.ifNotNull
import dev.nelson.mot.main.util.successOr
import dev.utils.preview.MotPreviewScreen
import kotlinx.coroutines.launch
import timber.log.Timber

@Composable
fun PaymentListScreen(
    navigationIcon: @Composable () -> Unit,
    openPaymentDetails: (Int?) -> Unit,
    openPaymentDetailsForCategory: (Int?) -> Unit,
    viewModel: PaymentListViewModel
) {
    val toolbarTitle by viewModel.toolBarTitleState.collectAsState(StringUtils.EMPTY)
    val paymentListResult by viewModel.paymentListResult.collectAsState(Loading)
    val snackbarVisibilityState by viewModel.snackBarVisibilityState.collectAsState()
    val deletedItemsCount by viewModel.deletedItemsCountState.collectAsState(0)
    val isSelectedStateOn by viewModel.isSelectedModeOnState.collectAsState(false)
    val selectedItemsCount by viewModel.selectedItemsCountState.collectAsState(0)
    val categories by viewModel.categoriesState.collectAsState(emptyList())
    val priceViewState by viewModel.priceViewState.collectAsState(PriceViewState())
    val dateViewState by viewModel.dateViewState.collectAsState()
    val onShowDateDialog by viewModel.showDatePickerDialogState.collectAsState(false)

    val haptic = LocalHapticFeedback.current
    val coroutineScope = rememberCoroutineScope()
    val datePickerState = rememberDatePickerState(initialSelectedDateMillis = dateViewState.mills)
    val modalBottomSheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }

    /**
     * Open payment details
     */
    LaunchedEffect(
        key1 = Unit,
        block = {
            viewModel.openPaymentDetailsAction.collect { action ->
                when (action) {
                    is OpenPaymentDetailsAction.NewPayment -> openPaymentDetails.invoke(null)
                    is OpenPaymentDetailsAction.ExistingPayment -> openPaymentDetails.invoke(action.paymentId)
                    is OpenPaymentDetailsAction.NewPaymentForCategory -> openPaymentDetailsForCategory.invoke(
                        action.categoryId
                    )
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
    
    if (onShowDateDialog) {
        DatePickerDialog(
            onDismissRequest = { viewModel.onDismissDatePickerDialog() },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let {
                        viewModel.onDateSelected(
                            it
                        )
                    }
                }) {
                    Text(stringResource(id = android.R.string.ok))
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.onDismissDatePickerDialog() }) {
                    Text(stringResource(id = android.R.string.cancel))
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    if (showBottomSheet) {
        /**
         * Back handler to hide modal bottom sheet
         */
        BackHandler(
            enabled = true,
            onBack = {
                Timber.d("bottom sheet BackHandler")
                coroutineScope.launch { modalBottomSheetState.hide() }.invokeOnCompletion {
                    if (!modalBottomSheetState.isVisible) {
                        showBottomSheet = false
                    }
                }
            }
        )

        val displayCornerRadius = getDisplayCornerRadius()

        ModalBottomSheet(
            onDismissRequest = { showBottomSheet = false },
            sheetState = modalBottomSheetState,
            shape = RoundedCornerShape(
                topStart = displayCornerRadius,
                topEnd = displayCornerRadius
            ),
            dragHandle = { BottomSheetDefaults.DragHandle() },
        ) {
            CategoriesListBottomSheet(
                categories = categories,
                onCategoryClick = {
                    viewModel.onCategorySelected(it)
                    coroutineScope.launch { modalBottomSheetState.hide() }.invokeOnCompletion {
                        if (!modalBottomSheetState.isVisible) {
                            showBottomSheet = false
                        }
                    }
                },
            )
        }
    }

    PaymentListLayout(
        navigationIcon = navigationIcon,
        toolbarTitle = toolbarTitle,
        paymentListResult = paymentListResult,
        onItemClick = { paymentItemModel -> viewModel.onItemClick(paymentItemModel) },
        onItemLongClick = { paymentItemModel -> viewModel.onItemLongClick(paymentItemModel) },
        onFabClick = { viewModel.onFabClick() },
        snackbarVisibleState = snackbarVisibilityState,
        onUndoButtonClickEvent = { viewModel.onUndoDeleteClick() },
        deletedItemsCount = deletedItemsCount,
        onItemSwiped = { paymentItemModel -> viewModel.onPaymentSwiped(paymentItemModel) },
        isSelectedStateOn = isSelectedStateOn,
        selectedItemsCount = selectedItemsCount,
        onCancelSelectionClick = {
            viewModel.onCancelSelectionClickEvent()
            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
        },
        onDeleteSelectedItemsClick = { viewModel.onDeleteSelectedItemsClick() },
        onChangeDateForSelectedItemsClick = { viewModel.onDateClick() },
        onSelectCategoryIconClick = { showBottomSheet = true },
        priceViewState = priceViewState,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentListLayout(
    navigationIcon: @Composable () -> Unit,
    toolbarTitle: String,
    paymentListResult: MotUiState<List<MotPaymentListItemModel>>,
    onItemClick: (MotPaymentListItemModel.Item) -> Unit,
    onItemLongClick: (MotPaymentListItemModel.Item) -> Unit,
    onFabClick: () -> Unit,
    snackbarVisibleState: Boolean,
    onUndoButtonClickEvent: () -> Unit,
    deletedItemsCount: Int,
    onItemSwiped: (MotPaymentListItemModel.Item) -> Unit,
    isSelectedStateOn: Boolean,
    selectedItemsCount: Int,
    onCancelSelectionClick: () -> Unit,
    onDeleteSelectedItemsClick: () -> Unit,
    onChangeDateForSelectedItemsClick: () -> Unit,
    onSelectCategoryIconClick: () -> Unit,
    priceViewState: PriceViewState,
) {
    val appBarScrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val paymentsLitScrollingState = rememberLazyListState()

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
                        IconButton(onClick = onChangeDateForSelectedItemsClick) {
                            Icon(Icons.Default.EditCalendar, contentDescription = "")
                        }
                        IconButton(onClick = onSelectCategoryIconClick) {
                            Icon(Icons.Default.Category, contentDescription = "")
                        }
                        IconButton(onClick = onDeleteSelectedItemsClick) {
                            Icon(Icons.Default.Delete, contentDescription = "")
                        }
                    },
                ).also {
                    // TODO: use accompanist like id done with scroll
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
        },
        floatingActionButton = {
            if (isSelectedStateOn.not()) {
                FloatingActionButton(
                    onClick = onFabClick,
                    content = { Icon(Icons.Default.Add, "new payment fab") }
                )
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            PaymentList(
                paymentListResult,
                onItemClick,
                onItemLongClick,
                onItemSwiped,
                isSelectedStateOn,
                priceViewState,
                paymentsLitScrollingState,
                scrollBehavior = appBarScrollBehavior
            )
        }
    }
}

@OptIn(
    ExperimentalFoundationApi::class,
    ExperimentalMaterial3Api::class
)
@Composable
fun PaymentList(
    paymentListResult: MotUiState<List<MotPaymentListItemModel>>,
    onItemClick: (MotPaymentListItemModel.Item) -> Unit,
    onItemLongClick: (MotPaymentListItemModel.Item) -> Unit,
    onItemSwiped: (MotPaymentListItemModel.Item) -> Unit,
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
                EmptyListPlaceholder(Modifier.fillMaxSize())
            } else {
                Column(
                    modifier = Modifier.ifNotNull(scrollBehavior) {
                        Modifier.nestedScroll(it.nestedScrollConnection)
                    }
                ) {
                    val checkBoxTransitionState = remember { MutableTransitionState(false) }
                    val transition = updateTransition(checkBoxTransitionState, label = "")

                    LazyColumn(
                        state = state,
                        modifier = Modifier.fillMaxSize(),
                    ) {
                        paymentList.forEach { paymentListItemModel ->
                            when (paymentListItemModel) {
                                is MotPaymentListItemModel.Header -> {
                                    stickyHeader(key = paymentListItemModel.key) {
                                        val enterTransition =
                                            remember { MotTransitions.listItemEnterTransition }
                                        val exitTransition =
                                            remember { MotTransitions.listItemExitTransition }
                                        AnimatedVisibility(
                                            visible = paymentListItemModel.isShow,
                                            enter = enterTransition,
                                            exit = exitTransition
                                        ) {
                                            PaymentListDateItem(date = paymentListItemModel.date)
                                        }
                                    }
                                }

                                is MotPaymentListItemModel.Item -> {
                                    item(key = paymentListItemModel.key) {
                                        PaymentItem(
                                            motPaymentListItemModel = paymentListItemModel,
                                            isSelectedStateOn = isSelectedStateOn,
                                            onItemClick = onItemClick,
                                            onItemLongClick = onItemLongClick,
                                            onItemSwiped = onItemSwiped,
                                            priceViewState = priceViewState,
                                            checkBoxTransitionState = checkBoxTransitionState,
                                            transition = transition
                                        )
                                    }
                                }

                                is MotPaymentListItemModel.Footer -> item { FABFooter() }
                            }
                        }
                    }
                }
            }
        }

        is Error -> {
            Box(modifier = Modifier.fillMaxSize()) {
                EmptyListPlaceholder(
                    Modifier.align(Alignment.Center),
                    Icons.Default.Error,
                    "error"
                )
            }
        }
    }
}

@Composable
private fun PaymentItem(
    motPaymentListItemModel: MotPaymentListItemModel.Item,
    isSelectedStateOn: Boolean,
    onItemClick: (MotPaymentListItemModel.Item) -> Unit,
    onItemLongClick: (MotPaymentListItemModel.Item) -> Unit,
    onItemSwiped: (MotPaymentListItemModel.Item) -> Unit,
    priceViewState: PriceViewState,
    checkBoxTransitionState: MutableTransitionState<Boolean>,
    transition: Transition<Boolean>,
) {
    val directions = if (isSelectedStateOn.not()) setOf(DismissDirection.EndToStart) else emptySet()

    MotDismissibleListItem(
        isShow = motPaymentListItemModel.isShow,
        directions = directions,
        onItemSwiped = { onItemSwiped.invoke(motPaymentListItemModel) }
    ) {
        PaymentListItem(
            motPaymentListItemModel,
            onClick = { payment -> onItemClick.invoke(payment) },
            onLongClick = { payment -> onItemLongClick.invoke(payment) },
            isSelectedStateOn = isSelectedStateOn,
            priceViewState = priceViewState,
            checkBoxTransitionState = checkBoxTransitionState,
            transition = transition,
        )
    }
}

@MotPreviewScreen
@Composable
private fun PaymentListScreenPreview() {
    PaymentListScreenTemplatePreview(Success(PreviewData.paymentListItemsPreview))
}

@MotPreviewScreen
@Composable
private fun PaymentListScreenEmptyPreview() {
    PaymentListScreenTemplatePreview(Success(emptyList()))
}

@MotPreviewScreen
@Composable
private fun PaymentListScreenErrorPreview() {
    PaymentListScreenTemplatePreview(Error(IllegalStateException("my error")))
}

@Composable
private fun PaymentListScreenTemplatePreview(
    paymentListResult: MotUiState<List<MotPaymentListItemModel>>
) {
    MotMaterialTheme {
        PaymentListLayout(
            navigationIcon = { MotNavDrawerIcon {} },
            toolbarTitle = "Title",
            paymentListResult = paymentListResult,
            onItemClick = {},
            onItemLongClick = {},
            onFabClick = {},
            snackbarVisibleState = false,
            onUndoButtonClickEvent = {},
            deletedItemsCount = 0,
            onItemSwiped = {},
            isSelectedStateOn = false,
            selectedItemsCount = 0,
            onCancelSelectionClick = {},
            onDeleteSelectedItemsClick = {},
            onChangeDateForSelectedItemsClick = {},
            onSelectCategoryIconClick = {},
            priceViewState = PreviewData.priceViewState,
        )
    }
}
