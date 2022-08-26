package dev.nelson.mot.main.presentations.payment

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.BottomDrawerValue
import androidx.compose.material.Button
import androidx.compose.material.Chip
import androidx.compose.material.ChipDefaults
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.EditCalendar
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.rememberBottomDrawerState
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.MutableLiveData
import dev.nelson.mot.main.data.model.Category
import dev.nelson.mot.main.presentations.ui.theme.MotTheme
import dev.nelson.mot.main.presentations.widgets.MotButton
import dev.nelson.mot.main.util.Constants
import dev.nelson.mot.main.util.compose.PreviewData
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun PaymentDetailsScreen(closeScreen: () -> Unit) {

    MotTheme {
        val viewModel = hiltViewModel<PaymentDetailsViewModel>()
        val date by viewModel.date.observeAsState("")
        val categories by viewModel.categories.collectAsState(initial = emptyList())
        PaymentDetailsLayout(
            nameLiveData = viewModel.paymentName,
            costLiveData = viewModel.cost,
            date = date,
            messageLiveData = viewModel.message,
            categories = categories,
            onNameChange = { viewModel.paymentName.value = it },
            onCostChange = { viewModel.cost.value = it },
            onMessageChange = { viewModel.message.value = it },
            onDateClick = { viewModel.onDateClick() },
            onCategoryClick = { viewModel.onCategoryClick(it) },
            onSaveClick = { viewModel.onSaveClick() },
            categoryNameLiveData = viewModel.categoryName
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PaymentDetailsLayoutPreview() {
    PaymentDetailsLayout(
        nameLiveData = MutableLiveData(""),
        costLiveData = MutableLiveData(""),
        date = "1/1/2022",
        categoryNameLiveData = MutableLiveData("category"),
        messageLiveData = MutableLiveData(""),
        categories = emptyList(),
        onNameChange = {},
        onCostChange = {},
        onMessageChange = {},
        onSaveClick = {},
        onCategoryClick = {},
        onDateClick = {},

        )
}

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterialApi::class)
@Composable
fun PaymentDetailsLayout(
    nameLiveData: MutableLiveData<String>,
    costLiveData: MutableLiveData<String>,
    date: String,
    categoryNameLiveData: MutableLiveData<String>,
    messageLiveData: MutableLiveData<String>,
    categories: List<Category>,
    onNameChange: (String) -> Unit,
    onCostChange: (String) -> Unit,
    onMessageChange: (String) -> Unit,
    onDateClick: () -> Unit,
    onCategoryClick: (Category) -> Unit,
    onSaveClick: () -> Unit
) {
    val name = nameLiveData.value.orEmpty()
    val cost = costLiveData.value.orEmpty()
    val message = messageLiveData.value.orEmpty()
    val categoryName by categoryNameLiveData.observeAsState("temp")
    val nameFocusRequester = remember { FocusRequester() }
    var nameFieldValueState by remember { mutableStateOf(TextFieldValue(text = name, selection = TextRange(name.length))) }
    var costFieldValueState by remember { mutableStateOf(TextFieldValue(text = cost, selection = TextRange(cost.length))) }
    var messageFieldValueState by remember { mutableStateOf(TextFieldValue(text = message, selection = TextRange(message.length))) }
//    var categoryNameValueState by remember { mutableStateOf(TextFieldValue(text = categoryName)) }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    val scope = rememberCoroutineScope()
    val scaffoldState = rememberBottomSheetScaffoldState()
    val bottomDrawerState = rememberBottomDrawerState(BottomDrawerValue.Closed)
    val modalBottomSheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)


    LaunchedEffect(key1 = Unit, block = {
        delay(Constants.DEFAULT_ANIMATION_DELAY) // <-- This is crucial.
        nameFocusRequester.requestFocus()
    })
    ModalBottomSheetLayout(
        sheetContent = { CategoriesListBottomSheet(categories, onCategoryClick, modalBottomSheetState) },
        sheetState = modalBottomSheetState
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
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
                            .focusRequester(nameFocusRequester)
                            .weight(2f),
//                    value = if (LocalInspectionMode.current) "preview new payment" else name,
                        value = nameFieldValueState,
                        singleLine = true,
                        maxLines = 1,
                        onValueChange = {
                            nameFieldValueState = it
                            onNameChange.invoke(nameFieldValueState.text)
                        },
                        placeholder = { Text(text = "new payment") },
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    )
                    TextField(
                        modifier = Modifier.weight(1f),
                        value = costFieldValueState,
                        singleLine = true,
                        maxLines = 1,
                        onValueChange = {
                            costFieldValueState = it
                            onCostChange.invoke(costFieldValueState.text)
                        },
                        placeholder = { Text(text = "0.0") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next),
                    )
                }
                Row(
                    modifier = Modifier
                        .padding(8.dp)
//                    .clickable {
//                        keyboardController?.hide()
//                        focusManager.clearFocus()
//                        onDateClick.invoke()
//                    }
                ) {
                    Chip(
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.EditCalendar,
                                contentDescription = "date icon"
                            )
                        },
                        colors = ChipDefaults.chipColors(
                            backgroundColor = Color.White
                        ),
                        onClick = {
                            keyboardController?.hide()
                            focusManager.clearFocus()
                            onDateClick.invoke()
                        },
                        border = BorderStroke(1.dp, Color.LightGray)
                    ) {
                        Text(
                            text = date,
                            modifier = Modifier.align(Alignment.CenterVertically)
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Chip(
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Category,
                                contentDescription = "category icon"
                            )
                        },
                        colors = ChipDefaults.chipColors(
                            backgroundColor = Color.White
                        ),
                        onClick = {
//                            onCategoryClick.invoke()
                            scope.launch {
                                keyboardController?.hide()
                                delay(Constants.DEFAULT_ANIMATION_DELAY)
                                focusManager.clearFocus()
                                modalBottomSheetState.show()
                            }
                        },
                        border = BorderStroke(1.dp, Color.LightGray)
                    ) {
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
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = messageFieldValueState,
                    onValueChange = {
                        messageFieldValueState = it
                        onMessageChange.invoke(messageFieldValueState.text)
                    },
                    placeholder = { Text(text = "message") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = { onSaveClick.invoke() })
                )
                MotButton(
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(8.dp),
                    onClick = onSaveClick) {
                    Text(text = "Save")
                }

            }
        }
    }

}

@OptIn(ExperimentalMaterialApi::class)
@Preview(showBackground = true)
@Composable
fun CategoriesListBottomSheetPreview() {
    CategoriesListBottomSheet(
        PreviewData.categoriesSelectListItemsPreview,
        onCategoryClick = {},
        rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    )
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
                            Divider()
                        }
                    }
                }
            }
        }
    }
}