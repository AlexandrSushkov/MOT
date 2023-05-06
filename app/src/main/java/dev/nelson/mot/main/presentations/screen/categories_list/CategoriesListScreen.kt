@file:OptIn(ExperimentalMaterial3Api::class)

package dev.nelson.mot.main.presentations.screen.categories_list

import android.widget.Toast
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material.DismissDirection
import androidx.compose.material.DismissValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Snackbar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Filter
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.rememberDismissState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.nelson.mot.core.ui.MotMaterialTheme
import dev.nelson.mot.main.R
import dev.nelson.mot.main.data.model.Category
import dev.nelson.mot.main.data.model.CategoryListItemModel
import dev.nelson.mot.main.data.model.CategoryListItemModel.CategoryItemModel
import dev.nelson.mot.main.data.model.CategoryListItemModel.Footer
import dev.nelson.mot.main.data.model.CategoryListItemModel.Letter
import dev.nelson.mot.main.presentations.widgets.ListPlaceholder
import dev.nelson.mot.core.ui.MotDismissibleListItem
import dev.nelson.mot.core.ui.MotTextField
import dev.nelson.mot.core.ui.MotTopAppBar
import dev.nelson.mot.main.util.constant.Constants
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
    title: String,
    navigationIcon: @Composable () -> Unit = {},
    settingsIcon: @Composable () -> Unit = {},
    openCategoryDetails: (Int?) -> Unit,
    openPaymentsByCategory: (Int?) -> Unit,
) {

    val categories by viewModel.categories.collectAsState(Loading)
    val categoryToEditId by viewModel.categoryToEditId.collectAsState()
    val categoryNameState by viewModel.categoryNameState.collectAsState()
    val openDialog by viewModel.showEditCategoryDialogAction.collectAsState(false)
    val categoryToEdit by viewModel.showEditCategoryDialogAction.collectAsState(false)
    val snackbarVisibleState by viewModel.snackBarVisibilityState.collectAsState()
    val deleteItemsSnackbarText by viewModel.deleteItemsSnackbarText.collectAsState(StringUtils.EMPTY)
    val deletedItemsMessage by viewModel.deletedItemsMessage.collectAsState(StringUtils.EMPTY)
    val showDeletedMessageToast by viewModel.showDeletedItemsMessageToast.collectAsState(false)

    if (openDialog) {
        EditCategoryDialog(
            categoryToEditId = categoryToEditId,
            categoryNameState = categoryNameState,
            closeEditCategoryDialog = { viewModel.closeEditCategoryDialog() },
            onCategoryNameChanged = { viewModel.onNameChanged(it) },
            onSaveCategoryClick = { viewModel.onSaveCategoryClick() },
            openCategoryDetails = openCategoryDetails
        )
    }

    if (showDeletedMessageToast) {
        Toast.makeText(LocalContext.current, deletedItemsMessage, Toast.LENGTH_SHORT).show()
    }

    CategoryListLayout(
        appBarTitle = title,
        appBarNavigationIcon = navigationIcon,
        settingsNavigationIcon = settingsIcon,
        categoriesMotResult = categories,
        categoryToEditId = categoryToEditId,
        onCategoryClick = openPaymentsByCategory,
        openCategoryDetails = openCategoryDetails,
        categoryNameState = categoryNameState,
        onFavoriteClick = { cat, che -> viewModel.onFavoriteClick(cat, che) },
        closeEditCategoryDialog = { viewModel.closeEditCategoryDialog() },
        openDialog = openDialog,
        onAddCategoryClickEvent = { viewModel.onAddCategoryClick() },
        onCategoryNameChanged = { viewModel.onNameChanged(it) },
        onCategoryLongPress = { viewModel.onCategoryLongPress(it) },
        onSaveCategoryClick = { viewModel.onSaveCategoryClick() },
        onSwipeCategory = { viewModel.onSwipeCategory(it) },
        snackbarVisibleState = snackbarVisibleState,
        deleteItemsCountText = deleteItemsSnackbarText,
        undoDeleteClickEvent = { viewModel.onUndoDeleteClick() },
        deletedItemsMessage = deletedItemsMessage,
        showDeletedMessageToast = showDeletedMessageToast
    )
}

@Composable
fun CategoryListLayout(
    appBarTitle: String,
    categoriesMotResult: MotResult<List<CategoryListItemModel>>,
    categoryNameState: TextFieldValue,
    openDialog: Boolean,
    appBarNavigationIcon: @Composable () -> Unit = {},
    settingsNavigationIcon: @Composable () -> Unit = {},
    onCategoryClick: (Int?) -> Unit,
    openCategoryDetails: (Int?) -> Unit,
    onFavoriteClick: (Category, Boolean) -> Unit,
    closeEditCategoryDialog: () -> Unit,
    onAddCategoryClickEvent: () -> Unit,
    onCategoryNameChanged: (TextFieldValue) -> Unit,
    onCategoryLongPress: (Category) -> Unit,
    onSaveCategoryClick: () -> Unit,
    onSwipeCategory: (CategoryItemModel) -> Unit,
    categoryToEditId: Int?,
    snackbarVisibleState: Boolean,
    deleteItemsCountText: String,
    undoDeleteClickEvent: () -> Unit,
    deletedItemsMessage: String,
    showDeletedMessageToast: Boolean,
) {
    Scaffold(
        topBar = {
            MotTopAppBar(
                title = appBarTitle,
                navigationIcon = appBarNavigationIcon,
                actions = { settingsNavigationIcon.invoke() }
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
    onCategoryClick: (Int?) -> Unit,
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
                        stringResource(R.string.text_empty)
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
                                        Box(
                                            modifier = Modifier
                                                .background(color = MaterialTheme.colorScheme.secondaryContainer)
                                                .fillMaxWidth()
                                        ) {
                                            Text(
                                                modifier = Modifier.padding(vertical = 8.dp, horizontal = 24.dp),
                                                text = categoryListItem.letter,
                                                style = MaterialTheme.typography.titleLarge
                                            )
                                        }
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
    val iconColor = if (checked) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.secondaryContainer
    val iconTint by animateColorAsState(iconColor, label = "icon tint animation state")

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = { onCategoryClick.invoke(category.id) },
                onLongClick = { category.id?.let { onCategoryLongPress.invoke(category) } }
            ),
        backgroundColor = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(0.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(start = 24.dp)
                .fillMaxWidth()
//                .padding(vertical = 8.dp, horizontal = 16.dp)
        ) {
//            Spacer(modifier = Modifier.width(24.dp))

//            Icon(
//                imageVector = Icons.Default.Category,
//                contentDescription = stringResource(id = R.string.accessibility_category_icon),
//                modifier = Modifier
//                    .size(24.dp)
//                    .align(Alignment.CenterVertically)
//            )
//            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = category.name,
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 16.dp)
                    .align(Alignment.CenterVertically),
                style = MaterialTheme.typography.titleMedium
            )
            if (category.id != null) {
                IconToggleButton(
                    modifier = Modifier.padding(end = 16.dp),
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
            MotTextField(
                value = categoryNameState,
                onValueChange = { onCategoryNameChanged.invoke(it) },
                placeholder = { Text(stringResource(R.string.text_category_name)) },
                maxLines = 1,
                singleLine = true,
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
//                    style = MaterialTheme.typography.button
                )
            }
        }
    )
}

@Composable
private fun CardFooter() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(80.dp),
        backgroundColor = MaterialTheme.colorScheme.surface,
    ) {}
}

@Preview(showBackground = true)
@Composable
private fun CategoryListLayoutLightPreview() {
    CategoryListLayout(
        appBarTitle = "Categories",
        categoriesMotResult = Success(PreviewData.categoriesListItemsPreview),
        categoryNameState = TextFieldValue(),
        openDialog = false,
        appBarNavigationIcon = {
            IconButton(onClick = {}) {
                Icon(Icons.Default.Menu, contentDescription = "menu drawer icon")
            }
        },
        onCategoryClick = {},
        openCategoryDetails = {},
        onFavoriteClick = { _, _ -> },
        closeEditCategoryDialog = {},
        onAddCategoryClickEvent = {},
        onCategoryNameChanged = {},
        onCategoryLongPress = {},
        onSaveCategoryClick = {},
        onSwipeCategory = {},
        categoryToEditId = null,
        snackbarVisibleState = false,
        deleteItemsCountText = "",
        undoDeleteClickEvent = {},
        deletedItemsMessage = "toast",
        showDeletedMessageToast = false
    )
}

@Preview(showBackground = true)
@Composable
private fun CategoryListLayoutDarkPreview() {
    MotMaterialTheme(darkTheme = true) {
        CategoryListLayout(
            appBarTitle = stringResource(R.string.categories),
            categoriesMotResult = Success(PreviewData.categoriesListItemsPreview),
            categoryNameState = TextFieldValue(),
            openDialog = false,
            appBarNavigationIcon = {
                IconButton(onClick = {}) {
                    Icon(Icons.Default.Menu, contentDescription = "menu drawer icon")
                }
            },
            onCategoryClick = {},
            openCategoryDetails = {},
            onFavoriteClick = { _, _ -> },
            closeEditCategoryDialog = {},
            onAddCategoryClickEvent = {},
            onCategoryNameChanged = {},
            onCategoryLongPress = {},
            onSaveCategoryClick = {},
            onSwipeCategory = {},
            categoryToEditId = null,
            snackbarVisibleState = false,
            deleteItemsCountText = "",
            undoDeleteClickEvent = {},
            deletedItemsMessage = "toast",
            showDeletedMessageToast = false
        )
    }
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
