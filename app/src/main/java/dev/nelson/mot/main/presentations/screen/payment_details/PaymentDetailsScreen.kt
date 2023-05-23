package dev.nelson.mot.main.presentations.screen.payment_details

import android.app.DatePickerDialog
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.EditCalendar
import androidx.compose.material.icons.filled.Star
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import dev.nelson.mot.core.ui.MotButton
import dev.nelson.mot.core.ui.MotMaterialTheme
import dev.nelson.mot.core.ui.MotOutlinedButton
import dev.nelson.mot.core.ui.MotTextField
import dev.nelson.mot.core.ui.MotTopAppBar
import dev.nelson.mot.main.R
import dev.nelson.mot.main.data.model.Category
import dev.nelson.mot.main.presentations.widgets.MotModalBottomSheetLayout
import dev.nelson.mot.main.util.compose.PreviewData
import dev.nelson.mot.main.util.constant.Constants
import dev.utils.preview.MotPreview
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

@MotPreview
@Composable
fun PaymentDetailsLayoutPreview() {
    MotMaterialTheme {
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

@OptIn(
    ExperimentalComposeUiApi::class,
    ExperimentalMaterialApi::class,
    ExperimentalFoundationApi::class
)
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
        delay(Constants.DEFAULT_ANIMATION_DELAY) // <-- This delay is crucial. Otherwise keyboard won't pop on
        paymentNameFocusRequester.requestFocus()
    })

    /**
     * Back handler to hide modal bottom sheet
     */
    BackHandler(
        enabled = modalBottomSheetState.isVisible,
        onBack = { coroutineScope.launch { modalBottomSheetState.hide() } }
    )

    MotModalBottomSheetLayout(
        sheetContent = {
            CategoriesListBottomSheet(
                categories,
                onCategoryClick,
                modalBottomSheetState
            )
        },
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
                    MotTextField(
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
                    )
                    MotTextField(
                        modifier = Modifier.weight(1f),
                        value = costFieldValueState,
                        singleLine = true,
                        maxLines = 1,
                        onValueChange = {
//                            costFieldValueState = it
                            onCostChange.invoke(it)
                        },
                        placeholder = { Text(text = "0.0") },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Next
                        ),
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

                    value = messageFieldValueState,
                    onValueChange = {
//                        messageFieldValueState = it
                        onMessageChange.invoke(it)
                    },
                    placeholder = { Text(text = "message") },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(onDone = { onSaveClick.invoke() }),

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
                        Icon(
                            Icons.Default.EditCalendar,
                            modifier = Modifier.padding(end = 8.dp),
                            contentDescription = "date icon"
                        )

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
                        Icon(
                            Icons.Default.Category,
                            modifier = Modifier.padding(end = 8.dp),
                            contentDescription = "category icon"
                        )
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
@Composable
fun CategoriesListBottomSheet(
    categories: List<Category>,
    onCategoryClick: (Category) -> Unit,
    modalBottomSheetState: ModalBottomSheetState
) {
    val scope = rememberCoroutineScope()
    val layColumnState = rememberLazyListState()
    Column(modifier = Modifier.fillMaxSize()) {
        MotTopAppBar(appBarTitle = "Choose a category")
        LazyColumn(
            state = layColumnState,
            modifier = Modifier
                .fillMaxSize()
        ) {
            categories.forEachIndexed { index, category ->
                item {
                    val outerPadding = if (category.isFavorite) 16.dp else 0.dp
                    val textPadding = if (category.isFavorite) 16.dp else 32.dp

                    val firstFavoriteItemIndex =
                        categories.find { it.isFavorite }?.let { categories.indexOf(it) }
                    val lastFavoriteItemIndex =
                        categories.findLast { it.isFavorite }?.let { categories.indexOf(it) }
                    val shape = getShape(firstFavoriteItemIndex, lastFavoriteItemIndex, index)

                    Box(
                        modifier = Modifier
                            .padding(horizontal = outerPadding)
                            .fillMaxWidth()
                    ) {
                        Box(modifier = Modifier
                            .fillMaxWidth()
                            .clip(shape)
                            .background(
                                if (category.isFavorite) {
                                    MaterialTheme.colorScheme.secondaryContainer
                                } else {
                                    MaterialTheme.colorScheme.surface
                                }
                            )
                            .clickable {
                                onCategoryClick.invoke(category)
                                scope.launch {
                                    modalBottomSheetState.hide()
                                    layColumnState.scrollToItem(0)
                                }
                            }) {
                            Column(modifier = Modifier.fillMaxWidth()) {
                                Row(modifier = Modifier.fillMaxWidth()) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = category.name,
                                            modifier = Modifier
                                                .padding(
                                                    vertical = 12.dp,
                                                    horizontal = textPadding
                                                )
                                        )
                                    }
                                    Column(
                                        modifier = Modifier
                                            .align(Alignment.CenterVertically)
                                            .padding(end = outerPadding)
                                    ) {
                                        if (category.isFavorite) {
                                            Icon(
                                                Icons.Filled.Star,
                                                contentDescription = stringResource(id = R.string.accessibility_favorite_icon),
                                                tint = MaterialTheme.colorScheme.secondary,
                                                modifier = Modifier.size(24.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun getShape(firstItemIndex: Int?, lastItemIndex: Int?, currentItemIndex: Int): Shape {
    val cornerRadius = 24.dp
    
    val topRoundedCornerShape = RoundedCornerShape(
        topStart = cornerRadius,
        topEnd = cornerRadius,
    )
    val bottomRoundedCornerShape = RoundedCornerShape(
        bottomStart = cornerRadius,
        bottomEnd = cornerRadius
    )
    val roundedCornerShape = RoundedCornerShape(
        topStart = cornerRadius,
        topEnd = cornerRadius,
        bottomStart = cornerRadius,
        bottomEnd = cornerRadius
    )
    val rectangularShape = RectangleShape
    if (firstItemIndex == null && lastItemIndex == null) return rectangularShape // no favorites in the list
    if (firstItemIndex == lastItemIndex) return roundedCornerShape // only one favorite item in the list
    return when (currentItemIndex) {
        firstItemIndex -> topRoundedCornerShape // several favorites and current is 1st favorite
        lastItemIndex -> bottomRoundedCornerShape // several favorites and current is last favorite
        else -> rectangularShape // several favorites and current is in the middle of the list favorite
    }
}

@OptIn(ExperimentalMaterialApi::class)
@MotPreview
@Composable
private fun CategoriesListBottomSheetPreview() {
    MotMaterialTheme {
        CategoriesListBottomSheet(
            PreviewData.categoriesSelectListItemsPreview,
            onCategoryClick = {},
            rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
        )
    }
}