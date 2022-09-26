package dev.nelson.mot.main.presentations.screen.categories_list

import android.widget.Toast
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.DismissValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.IconToggleButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Snackbar
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Filter
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.rememberDismissState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.nelson.mot.main.R
import dev.nelson.mot.main.data.model.Category
import dev.nelson.mot.main.data.model.CategoryListItemModel
import dev.nelson.mot.main.data.model.CategoryListItemModel.CategoryItemModel
import dev.nelson.mot.main.data.model.CategoryListItemModel.Footer
import dev.nelson.mot.main.data.model.CategoryListItemModel.Letter
import dev.nelson.mot.main.presentations.ui.theme.MotColors
import dev.nelson.mot.main.presentations.widgets.MotDismissibleListItem
import dev.nelson.mot.main.util.Constants
import dev.nelson.mot.main.util.MotResult
import dev.nelson.mot.main.util.MotResult.Error
import dev.nelson.mot.main.util.MotResult.Loading
import dev.nelson.mot.main.util.MotResult.Success
import dev.nelson.mot.main.util.StringUtils
import dev.nelson.mot.main.util.compose.PreviewData
import dev.nelson.mot.main.util.successOr
import kotlinx.coroutines.delay

@Composable
fun CategoryListScreen(
    viewModel: CategoriesListViewModel,
    openDrawer: () -> Unit,
    openCategoryDetails: (Int?) -> Unit,
    openPaymentsByCategory: (Category) -> Unit
) {

    val categories by viewModel.categories.collectAsState(initial = Loading)
    val categoryToEditId by viewModel.categoryToEditId.collectAsState()
    val categoryNameState by viewModel.categoryNameState.collectAsState()
    val openDialog by viewModel.showEditCategoryDialogAction.collectAsState(initial = false)
    val categoryToEdit by viewModel.showEditCategoryDialogAction.collectAsState(initial = false)
    val snackbarVisibleState by viewModel.snackBarVisibilityState.collectAsState()
    val deleteItemsSnackbarText by viewModel.deleteItemsSnackbarText.collectAsState(StringUtils.EMPTY)
    val deletedItemsMessage by viewModel.deletedItemsMessage.collectAsState(StringUtils.EMPTY)
    val showDeletedMessageToast by viewModel.showDeletedItemsMessageToast.collectAsState(false)

    CategoryListLayout(
        openDrawer = openDrawer,
        categoriesMotResult = categories,
        categoryToEditId = categoryToEditId,
        onCategoryClick = openPaymentsByCategory,
        openCategoryDetails = openCategoryDetails,
        categoryNameState = categoryNameState,
        onFavoriteClick = { cat, che -> viewModel.onFavoriteClick(cat, che) },
        closeEditCategoryDialog = { viewModel.closeEditCategoryDialog() },
        openDialog = openDialog,
        onAddCategoryClick = { viewModel.onAddCategoryClick() },
        onCategoryNameChanged = { viewModel.onNameChanged(it) },
        onCategoryLongPress = { viewModel.onCategoryLongPress(it) },
        onSaveCategoryClick = { viewModel.onSaveCategoryClick() },
        onSwipeCategory = { viewModel.onSwipeCategory(it) },
        snackbarVisibleState = snackbarVisibleState,
        deleteItemsSnackbarText = deleteItemsSnackbarText,
        undoDeleteClick = { viewModel.onUndoDeleteClick() },
        deletedItemsMessage = deletedItemsMessage,
        showDeletedMessageToast = showDeletedMessageToast
    )
}

@Composable
fun CategoryListLayout(
    categoriesMotResult: MotResult<List<CategoryListItemModel>>,
    categoryNameState: TextFieldValue,
    openDialog: Boolean,
    openDrawer: () -> Unit,
    onCategoryClick: (Category) -> Unit,
    openCategoryDetails: (Int?) -> Unit,
    onFavoriteClick: (Category, Boolean) -> Unit,
    closeEditCategoryDialog: () -> Unit,
    onAddCategoryClick: () -> Unit,
    onCategoryNameChanged: (TextFieldValue) -> Unit,
    onCategoryLongPress: (Category) -> Unit,
    onSaveCategoryClick: () -> Unit,
    onSwipeCategory: (CategoryItemModel) -> Unit,
    categoryToEditId: Int?,
    snackbarVisibleState: Boolean,
    deleteItemsSnackbarText: String,
    undoDeleteClick: () -> Unit,
    deletedItemsMessage: String,
    showDeletedMessageToast: Boolean,
) {

    if (openDialog) {
        EditCategoryDialog(
            categoryToEditId = categoryToEditId,
            categoryNameState = categoryNameState,
            closeEditCategoryDialog = closeEditCategoryDialog,
            onCategoryNameChanged = onCategoryNameChanged,
            onSaveCategoryClick = onSaveCategoryClick,
            openCategoryDetails = openCategoryDetails
        )
    }

    if (showDeletedMessageToast) {
        Toast.makeText(LocalContext.current, deletedItemsMessage, Toast.LENGTH_SHORT).show()
    }
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                navigationIcon = {
                    IconButton(onClick = openDrawer) {
                        Icon(
                            Icons.Default.Menu,
                            contentDescription = stringResource(R.string.accessibility_drawer_icon)
                        )
                    }
                },
                title = {
                    Text(
                        text = stringResource(R.string.categories),
                        style = MaterialTheme.typography.subtitle1
                    )
                }
            )
        },
        snackbarHost = {
            if (snackbarVisibleState) {
                Snackbar(
                    action = {
                        TextButton(
                            onClick = undoDeleteClick,
                            content = { Text("Undo") }
                        )
                    },
                    modifier = Modifier.padding(8.dp),
                    content = { Text(text = deleteItemsSnackbarText) }
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddCategoryClick) {
                Icon(Icons.Default.Add, stringResource(R.string.accessibility_add_icon))
            }
        },
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            CategoryList(
                categoriesMotResult,
                onSwipeCategory,
                onCategoryClick,
                onCategoryLongPress,
                onFavoriteClick
            )

        }
    }
}

@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
@Composable
fun CategoryList(
    categoriesMotResult: MotResult<List<CategoryListItemModel>>,
    onSwipeCategory: (CategoryItemModel) -> Unit,
    onCategoryClick: (Category) -> Unit,
    onCategoryLongPress: (Category) -> Unit,
    onFavoriteClick: (Category, Boolean) -> Unit,
) {
    when (categoriesMotResult) {
        is Success -> {
            val categories = categoriesMotResult.successOr(emptyList())
            if (categories.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize()) {
                    ListPlaceholder(
                        Modifier.align(Alignment.Center),
                        Icons.Default.Filter,
                        "empty"
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    content = {
                        categories.forEach { categoryListItem ->
                            when (categoryListItem) {
                                is CategoryItemModel -> {
                                    item(key = categoryListItem.key) {
                                        val dismissState = rememberDismissState(
                                            confirmStateChange = { dismissValue ->
                                                if (dismissValue == DismissValue.DismissedToStart) {
                                                    onSwipeCategory.invoke(categoryListItem)
                                                    true
                                                } else {
                                                    false
                                                }
                                            }
                                        )
                                        categoryListItem.category.id?.let {
                                            MotDismissibleListItem(
                                                dismissState = dismissState,
                                                dismissContent = {
                                                    CategoryListItem(
                                                        categoryListItem.category,
                                                        onCategoryClick,
                                                        onCategoryLongPress,
                                                        onFavoriteClick,
                                                    )
                                                }
                                            )
                                        } ?: CategoryListItem(
                                            categoryListItem.category,
                                            onCategoryClick,
                                            onCategoryLongPress,
                                            onFavoriteClick
                                        )
                                    }
                                }
                                is Letter -> {
                                    stickyHeader(key = categoryListItem.key) {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .background(MotColors.PurpleGrey80)
                                        ) {
                                            Text(
                                                modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp),
                                                text = categoryListItem.letter,
                                                style = MaterialTheme.typography.subtitle2
                                            )
                                        }
                                    }
                                }
                                is Footer -> item() { Footer() }
                            }
                        }
                    }
                )
            }
        }
        is Loading -> {
            Box(modifier = Modifier.fillMaxSize()) {
                CircularProgressIndicator(Modifier.align(Alignment.Center))
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CategoryListItem(
    category: Category,
    onCategoryClick: (Category) -> Unit,
    onCategoryLongPress: (Category) -> Unit,
    onFavoriteClick: (Category, Boolean) -> Unit,
) {
    var checked by remember { mutableStateOf(category.isFavorite) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = { onCategoryClick.invoke(category) },
                onLongClick = { category.id?.let { onCategoryLongPress.invoke(category) } }
            ),
        shape = RoundedCornerShape(0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Category,
                contentDescription = stringResource(id = R.string.accessibility_category_icon),
                modifier = Modifier
                    .size(0.dp)
                    .align(Alignment.CenterVertically)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = category.name,
                modifier = Modifier
                    .weight(1f)
                    .align(Alignment.CenterVertically),
                style = MaterialTheme.typography.subtitle1
            )
            if (category.id != null) {
                IconToggleButton(
                    checked = checked,
                    onCheckedChange = { isChecked ->
                        checked = isChecked
                        onFavoriteClick.invoke(category, isChecked)
                    },
                ) {
                    val tint by animateColorAsState(
                        if (checked) MotColors.FavoriteButtonOnBackground
                        else MotColors.FavoriteButtonOffBackground
                    )
                    Icon(
                        Icons.Filled.Star,
                        contentDescription = stringResource(id = R.string.accessibility_favorite_icon),
                        tint = tint,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun EditCategoryDialog(
    categoryToEditId: Int?,
    categoryNameState: TextFieldValue,
    onCategoryNameChanged: (TextFieldValue) -> Unit,
    closeEditCategoryDialog: () -> Unit,
    onSaveCategoryClick: () -> Unit,
    openCategoryDetails: (Int?) -> Unit,
) {
    val categoryNameFocusRequester = remember { FocusRequester.Default }

    LaunchedEffect(
        key1 = Unit,
        block = {
            delay(Constants.DEFAULT_ANIMATION_DELAY)
            categoryNameFocusRequester.requestFocus()
//            if (category.name.isNotEmpty()) {
//                categoryNameValueState = TextFieldValue(text = category.name, selection = TextRange(category.name.length))
//            }
        })

    AlertDialog(
        onDismissRequest = closeEditCategoryDialog,
        icon = {
            IconButton(
                onClick = { openCategoryDetails.invoke(null) }
            ) {
                Icon(
                    imageVector = Icons.Default.Category,
                    contentDescription = stringResource(R.string.accessibility_category_icon),
                    modifier = Modifier
                        .width(40.dp)
                        .height(40.dp)
                )
            }
        },
        text = {
            TextField(
                value = categoryNameState,
                onValueChange = { onCategoryNameChanged.invoke(it) },
                placeholder = { Text(stringResource(R.string.text_category_name)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(categoryNameFocusRequester),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Done),
//                keyboardActions = KeyboardActions(onDone = { viewModel.onSaveCategoryClick() })
                keyboardActions = KeyboardActions(onDone = { onSaveCategoryClick.invoke() })
            )
        },
        confirmButton = {
            TextButton(onClick = onSaveCategoryClick) {
                val textId = categoryToEditId?.let { R.string.text_edit } ?: R.string.text_add
                Text(
                    text = stringResource(textId),
                    style = MaterialTheme.typography.button
                )
            }
        }
    )
}

@Composable
private fun Footer() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .heightIn(60.dp)
    )
}

@Composable
private fun ListPlaceholder(modifier: Modifier, imageVector: ImageVector, text: String) {
    Column(
        modifier = modifier
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = "empty list icon",
            modifier = Modifier
                .size(42.dp)
                .align(Alignment.CenterHorizontally)
        )
        Spacer(
            modifier = Modifier
                .height(8.dp)
                .align(Alignment.CenterHorizontally)
        )
        Text(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            text = text
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CategoryListLayoutPreview() {
    CategoryListLayout(
        categoriesMotResult = Success(PreviewData.categoriesListItemsPreview),
        categoryNameState = TextFieldValue(),
        openDialog = false,
        openDrawer = {},
        onCategoryClick = {},
        openCategoryDetails = {},
        onFavoriteClick = { _, _ -> },
        closeEditCategoryDialog = {},
        onAddCategoryClick = {},
        onCategoryNameChanged = {},
        onCategoryLongPress = {},
        onSaveCategoryClick = {},
        onSwipeCategory = {},
        categoryToEditId = null,
        snackbarVisibleState = false,
        deleteItemsSnackbarText = "",
        undoDeleteClick = {},
        deletedItemsMessage = "toast",
        showDeletedMessageToast = false
    )
}

@Preview(showBackground = true)
@Composable
private fun CategoryListWithDataPreview() {
    CategoryList(
        categoriesMotResult = Success(PreviewData.categoriesListItemsPreview),
        onCategoryClick = {},
        onFavoriteClick = { _, _ -> },
        onCategoryLongPress = {},
        onSwipeCategory = {},
    )
}

@Preview(showBackground = true)
@Composable
private fun CategoryListWithEmptyDataPreview() {
    CategoryList(
        categoriesMotResult = Success(emptyList()),
        onCategoryClick = {},
        onFavoriteClick = { _, _ -> },
        onCategoryLongPress = {},
        onSwipeCategory = {},
    )
}

@Preview(showBackground = true)
@Composable
private fun CategoryListLoadingPreview() {
    CategoryList(
        categoriesMotResult = Loading,
        onCategoryClick = {},
        onFavoriteClick = { _, _ -> },
        onCategoryLongPress = {},
        onSwipeCategory = {},
    )
}

@Preview(showBackground = true)
@Composable
private fun CategoryListErrorPreview() {
    CategoryList(
        categoriesMotResult = Error(IllegalStateException("mot error")),
        onCategoryClick = {},
        onFavoriteClick = { _, _ -> },
        onCategoryLongPress = {},
        onSwipeCategory = {},
    )
}

@Preview(showBackground = false)
@Composable
private fun EditCategoryDialogPreview() {
    EditCategoryDialog(
        categoryToEditId = null,
        onSaveCategoryClick = {},
        onCategoryNameChanged = {},
        categoryNameState = TextFieldValue(),
        closeEditCategoryDialog = {},
        openCategoryDetails = {}
    )
}
