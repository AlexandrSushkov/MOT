@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)

package dev.nelson.mot.main.presentations.screen.categories_list

import android.widget.Toast
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.DismissDirection
import androidx.compose.material.DismissValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Snackbar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.FormatListBulleted
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.rememberDismissState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import dev.nelson.mot.R
import dev.nelson.mot.core.ui.MotCard
import dev.nelson.mot.core.ui.MotDismissibleListItem
import dev.nelson.mot.core.ui.MotMaterialTheme
import dev.nelson.mot.core.ui.MotNavDrawerIcon
import dev.nelson.mot.core.ui.MotTextButton
import dev.nelson.mot.core.ui.MotTextField
import dev.nelson.mot.core.ui.MotTopAppBar
import dev.nelson.mot.main.data.model.Category
import dev.nelson.mot.main.data.model.CategoryListItemModel
import dev.nelson.mot.main.data.model.CategoryListItemModel.CategoryItemModel
import dev.nelson.mot.main.data.model.CategoryListItemModel.Footer
import dev.nelson.mot.main.data.model.CategoryListItemModel.Letter
import dev.nelson.mot.main.presentations.widgets.ListPlaceholder
import dev.nelson.mot.main.util.MotUiState
import dev.nelson.mot.main.util.MotUiState.Error
import dev.nelson.mot.main.util.MotUiState.Loading
import dev.nelson.mot.main.util.MotUiState.Success
import dev.nelson.mot.main.util.StringUtils
import dev.nelson.mot.main.util.compose.PreviewData
import dev.nelson.mot.main.util.constant.Constants
import dev.nelson.mot.main.util.successOr
import dev.utils.preview.MotPreviewScreen
import kotlinx.coroutines.delay

@Composable
fun CategoryListScreen(
    viewModel: CategoriesListViewModel,
    appBarNavigationIcon: @Composable () -> Unit = {},
    openPaymentsByCategoryAction: (Int?) -> Unit,
) {

    val titleStringsRes by viewModel.titleStringRes.collectAsState(R.string.categories)
    val categoriesListUiState by viewModel.categoriesResult.collectAsState()
    val categoryToEditId by viewModel.categoryToEditId.collectAsState()
    val categoryNameState by viewModel.categoryNameState.collectAsState()
    val showEditCategoryDialog by viewModel.showEditCategoryDialogAction.collectAsState(false)
    val snackbarVisibleState by viewModel.snackBarVisibilityState.collectAsState()
    val deleteItemsSnackbarText by viewModel.deleteItemsSnackbarText.collectAsState(StringUtils.EMPTY)
    val deletedItemsMessage by viewModel.deletedItemsMessage.collectAsState(StringUtils.EMPTY)
    val showDeletedMessageToast by viewModel.showDeletedItemsMessageToast.collectAsState(false)

    if (showEditCategoryDialog) {
        EditCategoryDialog(
            categoryToEditId = categoryToEditId,
            categoryNameState = categoryNameState,
            closeEditCategoryDialog = { viewModel.closeEditCategoryDialog() },
            onCategoryNameChanged = { viewModel.onNameChanged(it) },
            onSaveCategoryClick = { viewModel.onSaveCategoryClick() }
        )
    }

    if (showDeletedMessageToast) {
        Toast.makeText(LocalContext.current, deletedItemsMessage, Toast.LENGTH_SHORT).show()
    }

    CategoryListLayout(
        appBarTitle = stringResource(titleStringsRes),
        appBarNavigationIcon = appBarNavigationIcon,
        categoriesListUiState = categoriesListUiState,
        onCategoryClick = openPaymentsByCategoryAction,
        onFavoriteClick = { cat, che -> viewModel.onFavoriteClick(cat, che) },
        onAddCategoryClickEvent = { viewModel.onAddCategoryClick() },
        onCategoryLongPress = { viewModel.onCategoryLongPress(it) },
        onSwipeCategory = { viewModel.onSwipeCategory(it) },
        snackbarVisibleState = snackbarVisibleState,
        deleteItemsCountText = deleteItemsSnackbarText,
        undoDeleteClickEvent = { viewModel.onUndoDeleteClick() }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryListLayout(
    appBarTitle: String,
    appBarNavigationIcon: @Composable () -> Unit = {},
    categoriesListUiState: MotUiState<List<CategoryListItemModel>>,
    onCategoryClick: (Int?) -> Unit,
    onFavoriteClick: (Category, Boolean) -> Unit,
    onAddCategoryClickEvent: () -> Unit,
    onCategoryLongPress: (Category) -> Unit,
    onSwipeCategory: (CategoryItemModel) -> Unit,
    snackbarVisibleState: Boolean,
    deleteItemsCountText: String,
    undoDeleteClickEvent: () -> Unit,
) {

    val categoriesListScrollingState = rememberLazyListState()

    Scaffold(
        topBar = {
            MotTopAppBar(
                appBarTitle = appBarTitle,
                navigationIcon = appBarNavigationIcon,
                isContentScrolling = categoriesListScrollingState.firstVisibleItemIndex != 0
            )
        },
        snackbarHost = {
            if (snackbarVisibleState) {
                Snackbar(
                    action = {
                        TextButton(
                            onClick = undoDeleteClickEvent,
                            content = { Text(stringResource(R.string.text_undo)) }
                        )
                    },
                    modifier = Modifier.padding(8.dp),
                    content = { Text(text = deleteItemsCountText) }
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddCategoryClickEvent) {
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
                categoriesListUiState,
                onSwipeCategory,
                onCategoryClick,
                onCategoryLongPress,
                onFavoriteClick,
                categoriesListScrollingState,
            )

        }
    }
}

@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
@Composable
fun CategoryList(
    categoriesListUiState: MotUiState<List<CategoryListItemModel>>,
    onSwipeCategory: (CategoryItemModel) -> Unit,
    onCategoryClick: (Int?) -> Unit,
    onCategoryLongPress: (Category) -> Unit,
    onFavoriteClick: (Category, Boolean) -> Unit,
    scrollState: LazyListState
) {
    when (categoriesListUiState) {
        is Success -> {
            val categories = categoriesListUiState.successOr(emptyList())
            if (categories.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize()) {
                    ListPlaceholder(
                        Modifier.align(Alignment.Center),
                        Icons.Default.FormatListBulleted,
                        stringResource(R.string.text_empty)
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    state = scrollState,
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
                                                directions = setOf(DismissDirection.EndToStart),
                                                dismissContent = {
                                                    CategoryListItem(
                                                        categoryListItem.category,
                                                        onCategoryClick,
                                                        onCategoryLongPress,
                                                        onFavoriteClick,
                                                    )
                                                }
                                            )
                                        } ?: CategoryListItem( // for "No category" category
                                            categoryListItem.category,
                                            onCategoryClick,
                                            onCategoryLongPress,
                                            onFavoriteClick
                                        )
                                    }
                                }

                                is Letter -> {
                                    stickyHeader(key = categoryListItem.key) {
                                        Surface(
                                            modifier = Modifier.fillMaxWidth(),
                                            tonalElevation = 4.dp,
                                            content = {
                                                Text(
                                                    modifier = Modifier
                                                        .padding(vertical = 4.dp, horizontal = 16.dp),
                                                    text = categoryListItem.letter,
                                                    style = MaterialTheme.typography.titleLarge
                                                )
                                            }
                                        )
                                    }
                                }

                                is Footer -> item { CardFooter() }
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
                    stringResource(R.string.text_error)
                )
            }
        }
    }

}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CategoryListItem(
    category: Category,
    onCategoryClick: (Int?) -> Unit,
    onCategoryLongPress: (Category) -> Unit,
    onFavoriteClick: (Category, Boolean) -> Unit,
) {
    var checked by remember { mutableStateOf(category.isFavorite) }
    val iconColor =
        if (checked) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.secondaryContainer
    val iconTint by animateColorAsState(iconColor, label = "icon tint animation state")

    MotCard(
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = { onCategoryClick.invoke(category.id ?: -1) },
                onLongClick = { category.id?.let { onCategoryLongPress.invoke(category) } }
            ),
    ) {
        ListItem(
            headlineContent = {
                Text(
                    text = category.name,
                    style = MaterialTheme.typography.titleMedium
                )
            },
            trailingContent = {
                category.id?.let {
                    IconToggleButton(
                        checked = checked,
                        onCheckedChange = { isChecked ->
                            checked = isChecked
                            onFavoriteClick.invoke(category, isChecked)
                        },
                    ) {
                        Icon(
                            Icons.Filled.Star,
                            contentDescription = stringResource(id = R.string.accessibility_favorite_icon),
                            tint = iconTint,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
        )
    }
}

@Composable
fun EditCategoryDialog(
    categoryToEditId: Int?,
    categoryNameState: TextFieldValue,
    onCategoryNameChanged: (TextFieldValue) -> Unit,
    closeEditCategoryDialog: () -> Unit,
    onSaveCategoryClick: () -> Unit,
) {
    val categoryNameFocusRequester = remember { FocusRequester() }

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
        text = {
            MotTextField(
                value = categoryNameState,
                onValueChange = { onCategoryNameChanged.invoke(it) },
                placeholder = { Text(stringResource(R.string.category_name)) },
                maxLines = 1,
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(categoryNameFocusRequester),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(onDone = { onSaveCategoryClick.invoke() })
            )
        },
        confirmButton = {
            MotTextButton(
                onClick = onSaveCategoryClick,
                stringResource = categoryToEditId?.let { R.string.text_edit } ?: R.string.text_add
            )
        }
    )
}

@Composable
private fun CardFooter() {
    MotCard(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(80.dp),
    ) {}
}

@MotPreviewScreen
@Composable
private fun CategoryListLayoutPreview() {
    MotMaterialTheme {
        CategoryListLayoutPreviewData(
            categoriesListUiState = Success(PreviewData.categoriesListItemsPreview)
        )
    }
}

@MotPreviewScreen
@Composable
private fun CategoryListLayoutSnackbarPreview() {
    MotMaterialTheme {
        CategoryListLayoutPreviewData(
            categoriesListUiState = Success(PreviewData.categoriesListItemsPreview),
            snackbarVisibleState = true
        )
    }
}

@MotPreviewScreen
@Composable
private fun CategoryListLayoutEmptyPreview() {
    MotMaterialTheme {
        CategoryListLayoutPreviewData(categoriesListUiState = Success(emptyList()))
    }
}

@MotPreviewScreen
@Composable
private fun CategoryListLayoutLoadingPreview() {
    MotMaterialTheme {
        CategoryListLayoutPreviewData(categoriesListUiState = Loading)
    }
}

@MotPreviewScreen
@Composable
private fun CategoryListLayoutErrorPreview() {
    MotMaterialTheme {
        CategoryListLayoutPreviewData(categoriesListUiState = Error(IllegalStateException("error")))
    }
}

@Composable
private fun CategoryListLayoutPreviewData(
    categoriesListUiState: MotUiState<List<CategoryListItemModel>>,
    snackbarVisibleState: Boolean = false,
) {
    CategoryListLayout(
        appBarTitle = "Categories",
        categoriesListUiState = categoriesListUiState,
        appBarNavigationIcon = { MotNavDrawerIcon(onClick = {}) },
        onCategoryClick = {},
        onFavoriteClick = { _, _ -> },
        onAddCategoryClickEvent = {},
        onCategoryLongPress = {},
        onSwipeCategory = {},
        snackbarVisibleState = snackbarVisibleState,
        deleteItemsCountText = "",
        undoDeleteClickEvent = {}
    )
}

@MotPreviewScreen
@Composable
private fun EditCategoryDialogPreview() {
    MotMaterialTheme {
        EditCategoryDialog(
            categoryToEditId = null,
            categoryNameState = TextFieldValue(),
            onCategoryNameChanged = {},
            closeEditCategoryDialog = {}
        ) {}
    }
}
