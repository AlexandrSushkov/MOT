package dev.nelson.mot.main.presentations.screen.payment_details

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.EditCalendar
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import dev.nelson.mot.R
import dev.nelson.mot.core.ui.MotButton
import dev.nelson.mot.core.ui.MotMaterialTheme
import dev.nelson.mot.core.ui.MotOutlinedButton
import dev.nelson.mot.core.ui.MotTextField
import dev.nelson.mot.core.ui.fundation.getDisplayCornerRadius
import dev.nelson.mot.main.data.model.Category
import dev.nelson.mot.main.presentations.shared.CategoriesListBottomSheet
import dev.nelson.mot.main.presentations.shared_view_state.DateViewState
import dev.nelson.mot.main.presentations.widgets.MotSingleLineText
import dev.nelson.mot.main.util.compose.PreviewData
import dev.nelson.mot.main.util.constant.Constants
import dev.utils.preview.MotPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentDetailsScreen(
    viewModel: PaymentDetailsViewModel,
    closeScreen: () -> Unit
) {
    val dateViewState by viewModel.dateViewState.collectAsState()
    val categories by viewModel.categoriesState.collectAsState(initial = emptyList())
    val selectedCategory by viewModel.selectedCategoryState.collectAsState(null)
    val onShowDateDialog by viewModel.showDatePickerDialogState.collectAsState(false)

    val datePickerState = rememberDatePickerState(initialSelectedDateMillis = dateViewState.mills)
    datePickerState.setSelection(dateViewState.mills)

    /**
     * Close screen effect
     */
    LaunchedEffect(
        key1 = true,
        block = {
            viewModel.finishAction.collect { closeScreen.invoke() }
        }
    )

    DatePicker(state = datePickerState)

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

    PaymentDetailsLayout(
        paymentNameState = viewModel.paymentNameState,
        costState = viewModel.costState,
        dateViewState = dateViewState,
        messageState = viewModel.messageState,
        categories = categories,
        onNameChange = { viewModel.onPaymentNameChanged(it) },
        onCostChange = { viewModel.onCostChange(it) },
        onMessageChange = { viewModel.onMessageChanged(it) },
        onDateClick = { viewModel.onDateClick() },
        onCategoryClick = { viewModel.onCategorySelected(it) },
        onSaveClick = { viewModel.onSaveClick() },
        selectedCategory = selectedCategory,
    )
}

@MotPreview
@Composable
fun PaymentDetailsLayoutPreview() {
    MotMaterialTheme {
        PaymentDetailsLayout(
            paymentNameState = MutableStateFlow(TextFieldValue()),
            costState = MutableStateFlow(TextFieldValue()),
            dateViewState = DateViewState(),
            selectedCategory = PreviewData.categoryPreview,
            messageState = MutableStateFlow(TextFieldValue()),
            categories = emptyList(),
            onNameChange = {},
            onCostChange = {},
            onMessageChange = {},
            onSaveClick = {},
            onCategoryClick = {},
            onDateClick = {},
        )
    }
}

@OptIn(
    ExperimentalComposeUiApi::class,
    ExperimentalFoundationApi::class,
    ExperimentalMaterial3Api::class
)
@Composable
fun PaymentDetailsLayout(
    paymentNameState: StateFlow<TextFieldValue>,
    costState: StateFlow<TextFieldValue>,
    dateViewState: DateViewState,
    selectedCategory: Category?,
    messageState: StateFlow<TextFieldValue>,
    categories: List<Category>,
    onNameChange: (TextFieldValue) -> Unit,
    onCostChange: (TextFieldValue) -> Unit,
    onMessageChange: (TextFieldValue) -> Unit,
    onDateClick: () -> Unit,
    onCategoryClick: (Category) -> Unit,
    onSaveClick: () -> Unit,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    val coroutineScope = rememberCoroutineScope()
    val paymentNameFocusRequester = remember { FocusRequester() }
    val costFocusRequester = remember { FocusRequester() }
    val bringIntoViewRequester = remember { BringIntoViewRequester() }
    val paymentNameFieldValueState by paymentNameState.collectAsState(TextFieldValue())
    val costFieldValueState by costState.collectAsState(TextFieldValue())
    val messageFieldValueState by messageState.collectAsState(TextFieldValue())

    val modalBottomSheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }

    LaunchedEffect(
        key1 = "keyboard focus request action",
        block = {
            delay(Constants.DEFAULT_ANIMATION_DELAY) // <-- This delay is crucial. Otherwise keyboard won't pop on
            costFocusRequester.requestFocus()
        }
    )

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
                selectedCategory = selectedCategory,
                onCategoryClick = {
                    onCategoryClick.invoke(it)
                    coroutineScope.launch { modalBottomSheetState.hide() }.invokeOnCompletion {
                        if (!modalBottomSheetState.isVisible) {
                            showBottomSheet = false
                        }
                    }
                },
            )
        }
    }

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(
                    state = rememberScrollState(),
                    enabled = true,
                )
                .padding(
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 16.dp,
                )
        ) {
            Spacer(modifier = Modifier.height(32.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                Spacer(modifier = Modifier.weight(1f))
                OutlinedTextField(
                    modifier = Modifier.focusRequester(costFocusRequester),
                    value = costFieldValueState,
                    singleLine = true,
                    maxLines = 1,
                    onValueChange = { onCostChange.invoke(it) },
                    placeholder = {
                        Text(
                            text = "0",
//                                color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.displayMedium
                        )
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    ),
                    colors = TextFieldDefaults.colors(
//                            focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
//                            disabledIndicatorColor = Color.Transparent,
//                            focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
//                            focusedTextColor = MaterialTheme.colorScheme.error,
//                            unfocusedTextColor = MaterialTheme.colorScheme.error,
                        cursorColor = Color.Transparent,
                        errorCursorColor = Color.Transparent,
                    ),
                    textStyle = MaterialTheme.typography.displayMedium,
                    shape = RoundedCornerShape(corner = CornerSize(16.dp)),
                )
                Spacer(modifier = Modifier.weight(1f))
            }
            Spacer(modifier = Modifier.height(32.dp))
            Column(modifier = Modifier.fillMaxWidth()) {
                MotTextField(
                    modifier = Modifier
                        .focusRequester(paymentNameFocusRequester)
                        .fillMaxWidth(),
                    value = paymentNameFieldValueState,
                    singleLine = true,
                    maxLines = 1,
                    onValueChange = { onNameChange.invoke(it) },
                    shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
                    placeholder = { Text(text = stringResource(id = R.string.payment_name_hint)) },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                )
            }
            MotTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .bringIntoViewRequester(bringIntoViewRequester)
                    .onFocusEvent { event ->
                        if (event.isFocused) {
                            coroutineScope.launch { bringIntoViewRequester.bringIntoView() }
                        }
                    },
                minLines = 3,
                maxLines = 5,
                value = messageFieldValueState,
                onValueChange = { onMessageChange.invoke(it) },
                placeholder = { Text(text = stringResource(id = R.string.message_hint)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                shape = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp),
                keyboardActions = KeyboardActions(onDone = { onSaveClick.invoke() }),
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                )
            )
            Spacer(modifier = Modifier.height(24.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                MotOutlinedButton(
                    onClick = {
                        keyboardController?.hide()
                        focusManager.clearFocus()
                        onDateClick.invoke()
                    },
                ) {
                    Icon(
                        Icons.Default.EditCalendar,
                        modifier = Modifier.padding(end = 4.dp),
                        contentDescription = "date icon"
                    )

                    Text(
                        text = dateViewState.text,
                        modifier = Modifier.align(Alignment.CenterVertically),
                        style = MaterialTheme.typography.labelMedium
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                MotOutlinedButton(
                    onClick = {
                        coroutineScope.launch {
                            keyboardController?.hide()
                            delay(Constants.DEFAULT_ANIMATION_DELAY)
                            focusManager.clearFocus()
                            showBottomSheet = true
                        }
                    },
                ) {
                    Icon(
                        Icons.Default.Category,
                        modifier = Modifier.padding(end = 4.dp),
                        contentDescription = "category icon"
                    )
                    MotSingleLineText(
                        text = selectedCategory?.name ?: "No category",
                        modifier = Modifier.align(Alignment.CenterVertically),
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                MotButton(
                    onClick = onSaveClick
                ) {
                    Text(text = "Save")
                }
            }
        }
    }

}


