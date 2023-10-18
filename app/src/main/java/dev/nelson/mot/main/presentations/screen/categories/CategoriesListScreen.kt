@file:OptIn(ExperimentalMaterial3Api::class)

package dev.nelson.mot.main.presentations.screen.categories

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import dev.nelson.mot.R
import dev.nelson.mot.core.ui.AppButtons
import dev.nelson.mot.core.ui.AppCard
import dev.nelson.mot.core.ui.AppTheme
import dev.nelson.mot.core.ui.AppToolbar
import dev.nelson.mot.core.ui.MotDismissibleListItem
import dev.nelson.mot.core.ui.AppIconButtons
import dev.nelson.mot.core.ui.AppIcons
import dev.nelson.mot.main.data.model.Category
import dev.nelson.mot.main.data.model.MotListItemModel
import dev.nelson.mot.main.presentations.widgets.EmptyListPlaceholder
import dev.nelson.mot.main.presentations.widgets.MotSingleLineText
import dev.nelson.mot.main.util.MotUiState
import dev.nelson.mot.main.util.MotUiState.Error
import dev.nelson.mot.main.util.MotUiState.Loading
import dev.nelson.mot.main.util.MotUiState.Success
import dev.nelson.mot.main.util.StringUtils
import dev.nelson.mot.main.util.compose.PreviewData
import dev.nelson.mot.main.util.constant.Constants
import dev.nelson.mot.main.util.extention.ifNotNull
import dev.nelson.mot.main.util.successOr
import dev.utils.MotTransitions
import dev.utils.preview.MotPreviewScreen
import kotlinx.coroutines.delay

@Composable
fun CategoryListScreen(
    viewModel: CategoriesListViewModel,
    appBarNavigationIcon: @Composable () -> Unit = {},
    openPaymentsByCategoryAction: (Int?) -> Unit
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
        onSwipeCategory = { viewModel.onCategorySwiped(it) },
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
    categoriesListUiState: MotUiState<List<MotListItemModel>>,
    onCategoryClick: (Int?) -> Unit,
    onFavoriteClick: (Category, Boolean) -> Unit,
    onAddCategoryClickEvent: () -> Unit,
    onCategoryLongPress: (Category) -> Unit,
    onSwipeCategory: (MotListItemModel.Item) -> Unit,
    snackbarVisibleState: Boolean,
    deleteItemsCountText: String,
    undoDeleteClickEvent: () -> Unit
) {
    val categoriesListScrollingState = rememberLazyListState()
    val appBerScrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    Scaffold(
        topBar = {
            AppToolbar.Regular(
                appBarTitle = appBarTitle,
                navigationIcon = appBarNavigationIcon,
                scrollBehavior = appBerScrollBehavior
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
                AppIcons.Add()
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            CategoryList(
                categoriesListUiState,
                onSwipeCategory = onSwipeCategory,
                onCategoryClick = onCategoryClick,
                onCategoryLongPress = onCategoryLongPress,
                onFavoriteClick = onFavoriteClick,
                scrollState = categoriesListScrollingState,
                scrollBehavior = appBerScrollBehavior
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CategoryList(
    categoriesListUiState: MotUiState<List<MotListItemModel>>,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    onSwipeCategory: (MotListItemModel.Item) -> Unit,
    onCategoryClick: (Int?) -> Unit,
    onCategoryLongPress: (Category) -> Unit,
    onFavoriteClick: (Category, Boolean) -> Unit,
    scrollState: LazyListState
) {
    when (categoriesListUiState) {
        is Success -> {
            val categories = categoriesListUiState.successOr(emptyList())
            if (categories.isEmpty()) {
                EmptyListPlaceholder(Modifier.fillMaxSize())
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .ifNotNull(scrollBehavior) { Modifier.nestedScroll(it.nestedScrollConnection) },
                    state = scrollState,
                    content = {
                        categories.forEach { categoryListItem ->
                            when (categoryListItem) {
                                is MotListItemModel.Item -> {
                                    item(key = categoryListItem.key) {
                                        categoryListItem.category.id?.let {
                                            if (it > 0) {
                                                MotDismissibleListItem(
                                                    isShow = categoryListItem.isShow,
                                                    directions = setOf(DismissDirection.EndToStart),
                                                    onItemSwiped = {
                                                        onSwipeCategory.invoke(
                                                            categoryListItem
                                                        )
                                                    }
                                                ) {
                                                    CategoryListItem(
                                                        categoryListItem.category,
                                                        onCategoryClick,
                                                        onCategoryLongPress,
                                                        onFavoriteClick
                                                    )
                                                }
                                            } else {
                                                // for "No category" category
                                                CategoryListItem(
                                                    categoryListItem.category,
                                                    onCategoryClick,
                                                    onCategoryLongPress,
                                                    onFavoriteClick
                                                )
                                            }
                                        }
                                    }
                                }

                                is MotListItemModel.Header -> {
                                    stickyHeader(key = categoryListItem.key) {
                                        val enterTransition =
                                            remember { MotTransitions.listItemEnterTransition }
                                        val exitTransition =
                                            remember { MotTransitions.listItemExitTransition }
                                        AnimatedVisibility(
                                            visible = categoryListItem.isShow,
                                            enter = enterTransition,
                                            exit = exitTransition
                                        ) {
                                            Surface(
                                                modifier = Modifier.fillMaxWidth(),
                                                tonalElevation = 4.dp,
                                                content = {
                                                    Text(
                                                        modifier = Modifier
                                                            .padding(
                                                                vertical = 4.dp,
                                                                horizontal = 16.dp
                                                            ),
                                                        text = categoryListItem.date,
                                                        style = MaterialTheme.typography.titleLarge
                                                    )
                                                }
                                            )
                                        }
                                    }
                                }

                                is MotListItemModel.Footer -> item { CardFooter() }
                            }
                        }
                    }
                )
            }
        }

        is Loading -> {
            Box(modifier = Modifier.fillMaxSize()) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                CircularProgressIndicator(Modifier.align(Alignment.Center))
            }
        }

        is Error -> {
            Box(modifier = Modifier.fillMaxSize()) {
                EmptyListPlaceholder(
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
    onFavoriteClick: (Category, Boolean) -> Unit
) {
    AppCard.Rectangular(
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = {
                    onCategoryClick.invoke(category.id ?: Constants.NO_CATEGORY_CATEGORY_ID)
                },
                onLongClick = {
                    category.id?.let { if (it > 0) onCategoryLongPress.invoke(category) }
                }
            )
    ) {
        ListItem(
            headlineContent = {
                MotSingleLineText(
                    text = category.name,
                    style = MaterialTheme.typography.titleMedium
                )
            },
            trailingContent = {
                category.id?.let {
                    if (it > 0) {
                        IconToggleButton(
                            checked = category.isFavorite,
                            onCheckedChange = { isChecked ->
                                onFavoriteClick.invoke(category, isChecked)
                            }
                        ) {
                            val modifier = Modifier.size(24.dp)
                            val tint = MaterialTheme.colorScheme.secondary
                            if (category.isFavorite) {
                                AppIcons.FavoriteChecked(modifier, tint)
                            } else {
                                AppIcons.FavoriteUnchecked(modifier, tint)
                            }
                        }
                    }
                }
            }
        )
    }
}

@Composable
private fun EditCategoryDialog(
    categoryToEditId: Int?,
    categoryNameState: TextFieldValue,
    onCategoryNameChanged: (TextFieldValue) -> Unit,
    closeEditCategoryDialog: () -> Unit,
    onSaveCategoryClick: () -> Unit
) {
    val categoryNameFocusRequester = remember { FocusRequester() }

    LaunchedEffect(
        key1 = Unit,
        block = {
            delay(Constants.DEFAULT_ANIMATION_DELAY)
            categoryNameFocusRequester.requestFocus()
        }
    )

    AlertDialog(
        onDismissRequest = closeEditCategoryDialog,
        text = {
            OutlinedTextField(
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
            val buttonTextId = categoryToEditId?.let { R.string.text_edit } ?: R.string.text_add
            AppButtons.TextButton(
                stringResource = buttonTextId,
                enabled = categoryNameState.text.isNotEmpty(),
                onClick = onSaveCategoryClick
            )
        }
    )
}

@Composable
private fun CardFooter() {
    AppCard.Rectangular(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(80.dp),
        content = {}
    )
}

@MotPreviewScreen
@Composable
private fun CategoryListLayoutPreview() {
    AppTheme {
        CategoryListLayoutPreviewData(
            categoriesListUiState = Success(PreviewData.categoriesListItemsPreview)
        )
    }
}

@MotPreviewScreen
@Composable
private fun CategoryListLayoutSnackbarPreview() {
    CategoryListLayoutPreviewData(
        categoriesListUiState = Success(PreviewData.categoriesListItemsPreview),
        snackbarVisibleState = true
    )
}

@MotPreviewScreen
@Composable
private fun CategoryListLayoutEmptyPreview() {
    CategoryListLayoutPreviewData(categoriesListUiState = Success(emptyList()))
}

@MotPreviewScreen
@Composable
private fun CategoryListLayoutLoadingPreview() {
    CategoryListLayoutPreviewData(categoriesListUiState = Loading)
}

@MotPreviewScreen
@Composable
private fun CategoryListLayoutErrorPreview() {
    CategoryListLayoutPreviewData(categoriesListUiState = Error(IllegalStateException("error")))
}

@Composable
private fun CategoryListLayoutPreviewData(
    categoriesListUiState: MotUiState<List<MotListItemModel>>,
    snackbarVisibleState: Boolean = false
) {
    AppTheme {
        CategoryListLayout(
            appBarTitle = "Categories",
            categoriesListUiState = categoriesListUiState,
            appBarNavigationIcon = { AppIconButtons.Drawer(onClick = {}) },
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
}

@MotPreviewScreen
@Composable
private fun EditCategoryDialogPreview() {
    AppTheme {
        EditCategoryDialog(
            categoryToEditId = null,
            categoryNameState = TextFieldValue(),
            onCategoryNameChanged = {},
            closeEditCategoryDialog = {},
            onSaveCategoryClick = {}
        )
    }
}
