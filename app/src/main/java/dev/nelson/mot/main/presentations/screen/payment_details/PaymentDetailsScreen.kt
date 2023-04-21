package dev.nelson.mot.main.presentations.screen.payment_details

import android.app.DatePickerDialog
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.contentColorFor
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.EditCalendar
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.nelson.mot.main.data.model.Category
import dev.nelson.mot.main.presentations.ui.theme.MotTheme
import dev.nelson.mot.main.presentations.widgets.MotButton
import dev.nelson.mot.main.presentations.widgets.MotOutlinedButton
import dev.nelson.mot.main.presentations.ui.theme.textFieldMaterial3Colors
import dev.nelson.mot.main.presentations.widgets.MotModalBottomSheetLayout
import dev.nelson.mot.main.util.Constants
import dev.nelson.mot.main.util.compose.PreviewData
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Calendar

@Composable
fun PaymentDetailsScreen(
    viewModel: PaymentDetailsViewModel,
    closeScreen: () -> Unit
) {

    /**
     * Close screen effect
     */
    LaunchedEffect(
        key1 = true,
        block = {
            viewModel.finishAction.collect { closeScreen.invoke() }
        }
    )

    // TODO: move to VM
    val context = LocalContext.current
    val cldr: Calendar = Calendar.getInstance()
    val day: Int = cldr.get(Calendar.DAY_OF_MONTH)
    val month: Int = cldr.get(Calendar.MONTH)
    val year: Int = cldr.get(Calendar.YEAR)
    val picker = DatePickerDialog(
        context,
        { _, selectedYear, monthOfYear, dayOfMonth -> run { viewModel.onDateSet(selectedYear, monthOfYear, dayOfMonth) } },
        year,
        month,
        day
    )

    val date by viewModel.dateState.collectAsState("")
    val categories by viewModel.categoriesState.collectAsState(initial = emptyList())

    PaymentDetailsLayout(
        paymentNameState = viewModel.paymentNameState,
        costState = viewModel.costState,
        date = date,
        messageState = viewModel.messageState,
        categories = categories,
        onNameChange = { viewModel.onPaymentNameChanged(it) },
        onCostChange = { viewModel.onCostChange(it) },
        onMessageChange = { viewModel.onMessageChanged(it) },
        onDateClick = { picker.show() },
        onCategoryClick = { viewModel.onCategorySelected(it) },
        onSaveClick = { viewModel.onSaveClick() },
        categoryNameState = viewModel.categoryNameState,
    )
}

@Preview(showBackground = true)
@Composable
fun PaymentDetailsLayoutLightPreview() {
    PaymentDetailsLayout(
        paymentNameState = MutableStateFlow(TextFieldValue()),
        costState = MutableStateFlow(TextFieldValue()),
        date = "1/1/2022",
        categoryNameState = MutableStateFlow("category"),
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

@Preview(showBackground = true)
@Composable
fun PaymentDetailsLayoutDarkPreview() {
    MotTheme(darkTheme = true) {
        PaymentDetailsLayout(
            paymentNameState = MutableStateFlow(TextFieldValue()),
            costState = MutableStateFlow(TextFieldValue()),
            date = "1/1/2022",
            categoryNameState = MutableStateFlow("category"),
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

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
@Composable
fun PaymentDetailsLayout(
    paymentNameState: StateFlow<TextFieldValue>,
    costState: StateFlow<TextFieldValue>,
    date: String,
    categoryNameState: StateFlow<String>,
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
    val bringIntoViewRequester = remember { BringIntoViewRequester() }
    val modalBottomSheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)

    val categoryName by categoryNameState.collectAsState()
    val paymentNameFieldValueState by paymentNameState.collectAsState(TextFieldValue())
    val costFieldValueState by costState.collectAsState(TextFieldValue())
    val messageFieldValueState by messageState.collectAsState(TextFieldValue())

    LaunchedEffect(key1 = Unit, block = {
        delay(Constants.DEFAULT_ANIMATION_DELAY) // <-- This is crucial. Otherwise keyboard won't pop on
        paymentNameFocusRequester.requestFocus()
    })
    MotModalBottomSheetLayout(
        sheetContent = { CategoriesListBottomSheet(categories, onCategoryClick, modalBottomSheetState) },
        sheetState = modalBottomSheetState,
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
        ) {
            Column(
                modifier = Modifier.verticalScroll(
                    state = rememberScrollState(),
                    enabled = true
                )
            ) {
                Row {
                    TextField(
                        modifier = Modifier
                            .focusRequester(paymentNameFocusRequester)
                            .weight(2f),
//                    value = if (LocalInspectionMode.current) "preview new payment" else name,
                        value = paymentNameFieldValueState,
                        singleLine = true,
                        maxLines = 1,
                        onValueChange = {
//                            paymentNameFieldValueState = it
                            onNameChange.invoke(it)
                        },
                        placeholder = { Text(text = "new payment") },
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                        colors = TextFieldDefaults.textFieldMaterial3Colors()
                    )
                    TextField(
                        modifier = Modifier.weight(1f),
                        value = costFieldValueState,
                        singleLine = true,
                        maxLines = 1,
                        onValueChange = {
//                            costFieldValueState = it
                            onCostChange.invoke(it)
                        },
                        placeholder = { Text(text = "0.0") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next),
                        colors = TextFieldDefaults.textFieldMaterial3Colors()
                    )
                }
                TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .bringIntoViewRequester(bringIntoViewRequester)
                        .onFocusEvent { event ->
                            if (event.isFocused) {
                                coroutineScope.launch { bringIntoViewRequester.bringIntoView() }
                            }
                        },

                    value = messageFieldValueState,
                    onValueChange = {
//                        messageFieldValueState = it
                        onMessageChange.invoke(it)
                    },
                    placeholder = { Text(text = "message") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = { onSaveClick.invoke() }),
                    colors = TextFieldDefaults.textFieldMaterial3Colors()

                )
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier
                        .padding(8.dp)
//                    .clickable {
//                        keyboardController?.hide()
//                        focusManager.clearFocus()
//                        onDateClick.invoke()
//                    }
                ) {
                    MotOutlinedButton(
                        onClick = {
                            keyboardController?.hide()
                            focusManager.clearFocus()
                            onDateClick.invoke()
                        },
                    ) {
                        Icon(Icons.Default.EditCalendar, modifier = Modifier.padding(end = 8.dp), contentDescription = "date icon")

                        Text(
                            text = date,
                            modifier = Modifier.align(Alignment.CenterVertically)
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    MotOutlinedButton(
                        onClick = {
//                            onCategoryClick.invoke()
                            coroutineScope.launch {
                                keyboardController?.hide()
                                delay(Constants.DEFAULT_ANIMATION_DELAY)
                                focusManager.clearFocus()
                                modalBottomSheetState.show()
                            }
                        },
                    ) {
                        Icon(Icons.Default.Category, modifier = Modifier.padding(end = 8.dp), contentDescription = "category icon")
                        Text(
                            text = categoryName,
                            modifier = Modifier.align(Alignment.CenterVertically)
                        )
                    }


//                Spacer(modifier = Modifier.width(8.dp))
//                Text(
//                    text = date,
//                    modifier = Modifier.align(Alignment.CenterVertically)
//                )
                }
                Spacer(modifier = Modifier.height(10.dp))
                MotButton(
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(8.dp),
                    onClick = onSaveClick
                ) {
//                    Icon(Icons.Default.Save, modifier = Modifier.padding(end = 8.dp), contentDescription = "IconButton")
                    Text(text = "Save")
                }

            }
        }
    }

}

@OptIn(ExperimentalMaterialApi::class)
@Preview(showBackground = true)
@Composable
fun CategoriesListBottomSheetLightPreview() {
    CategoriesListBottomSheet(
        PreviewData.categoriesSelectListItemsPreview,
        onCategoryClick = {},
        rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Preview(showBackground = true)
@Composable
fun CategoriesListBottomSheetDarkPreview() {
    MotTheme(darkTheme = true) {
        CategoriesListBottomSheet(
            PreviewData.categoriesSelectListItemsPreview,
            onCategoryClick = {},
            rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CategoriesListBottomSheet(
    categories: List<Category>,
    onCategoryClick: (Category) -> Unit,
    modalBottomSheetState: ModalBottomSheetState
) {
    val scope = rememberCoroutineScope()
    val layColumnState = rememberLazyListState()
    Column(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = "Choose a category",
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.Center)
            )
        }
        LazyColumn(
            state = layColumnState,
            modifier = Modifier
                .fillMaxSize()
        ) {
            categories.forEachIndexed() { index, category ->
                item {
                    Box(modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onCategoryClick.invoke(category)
                            scope.launch {
                                modalBottomSheetState.hide()
                                layColumnState.scrollToItem(0)
                            }
                        }) {
                        Text(
                            text = category.name,
                            modifier = Modifier.padding(vertical = 16.dp, horizontal = 16.dp)
                        )
                    }
                    val nextCategoryIndex = index + 1
                    if (categories.size > nextCategoryIndex) {
                        val nextCategory = categories[index + 1]
                        if (category.isFavorite && !nextCategory.isFavorite) {
                            Divider(color = MaterialTheme.colorScheme.onSurface)
                        }
                    }
                }
            }
        }
    }
}