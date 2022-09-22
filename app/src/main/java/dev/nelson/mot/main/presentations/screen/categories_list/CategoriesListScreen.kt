package dev.nelson.mot.main.presentations.screen.categories_list

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
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.IconToggleButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Star
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.nelson.mot.main.R
import dev.nelson.mot.main.data.model.Category
import dev.nelson.mot.main.data.model.CategoryListItemModel
import dev.nelson.mot.main.presentations.ui.theme.MotColors
import dev.nelson.mot.main.util.Constants
import dev.nelson.mot.main.util.compose.PreviewData
import kotlinx.coroutines.delay

@Composable
fun CategoryListScreen(
    viewModel: CategoriesListViewModel,
    openDrawer: () -> Unit,
    openCategoryDetails: (Int?) -> Unit,
    openPaymentsByCategory: (Category) -> Unit
) {

    val categories by viewModel.categoriesFlow.collectAsState(initial = emptyList())
    val categoryId by viewModel.categoryId.collectAsState()
    val categoryNameState by viewModel.categoryNameState.collectAsState()
    val openDialog by viewModel.showEditCategoryDialogAction.collectAsState(initial = false)
    val categoryToEdit by viewModel.showEditCategoryDialogAction.collectAsState(initial = false)

    CategoryListLayout(
        openDrawer = openDrawer,
        categories = categories,
        onCategoryClick = openPaymentsByCategory,
        openCategoryDetails = openCategoryDetails,
        categoryNameState = categoryNameState,
        onFavoriteClick = { cat, che -> viewModel.onFavoriteClick(cat, che) },
        closeEditCategoryDialog = { viewModel.closeEditCategoryDialog() },
        openDialog = openDialog,
        onAddCategoryClick = { viewModel.onAddCategoryClick() },
        onCategoryNameChanged = { viewModel.onNameChanged(it) },
        onCategoryLongPress = { viewModel.onCategoryLongPress(it) },
        onSaveCategoryClick = { viewModel.onSaveCategoryClick() }
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CategoryListLayout(
    categories: List<CategoryListItemModel>,
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
    onSaveCategoryClick: () -> Unit
) {

    if (openDialog) {
        EditCategoryDialog(
            categoryNameState = categoryNameState,
            closeEditCategoryDialog = closeEditCategoryDialog,
            onCategoryNameChanged = onCategoryNameChanged,
            onSaveCategoryClick = onSaveCategoryClick,
            openCategoryDetails = openCategoryDetails
        )
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
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddCategoryClick,
//                {
//                    openCategoryDetails.invoke(null)
//                    onAddCategoryClick
//                },
            ) {
                Icon(Icons.Default.Add, stringResource(R.string.accessibility_add_icon))
            }
        },
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                content = {
                    categories.forEach { categoryListItem ->
                        if (categoryListItem is CategoryListItemModel.CategoryItemModel) {
                            item {
                                var checked by remember { mutableStateOf(categoryListItem.category.isFavorite) }

                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .combinedClickable(
                                            onClick = { onCategoryClick.invoke(categoryListItem.category) },
                                            onLongClick = {
//                                                categoryListItem.category.id?.let { categoryId ->
//                                                    openCategoryDetails.invoke(categoryId)
//                                                }

                                                categoryListItem.category.id?.let {
                                                    onCategoryLongPress.invoke(categoryListItem.category)
//                                                    viewModel.onCategoryLongPress(categoryListItem.category)
                                                }
                                            }
                                        ),
                                    shape = RoundedCornerShape(0.dp)
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 8.dp, horizontal = 16.dp)
                                    ) {
                                        Text(
                                            text = categoryListItem.category.name,
                                            modifier = Modifier
                                                .weight(1f)
                                                .align(Alignment.CenterVertically),
                                            style = MaterialTheme.typography.subtitle1
                                        )
                                        if (categoryListItem.category.id != null) {
                                            IconToggleButton(
                                                checked = checked,
                                                onCheckedChange = { isChecked ->
                                                    checked = isChecked
                                                    onFavoriteClick.invoke(categoryListItem.category, isChecked)
                                                },
                                            ) {
                                                val tint by animateColorAsState(
                                                    if (checked) MotColors.FavoriteButtonOnBackground
                                                    else MotColors.FavoriteButtonOffBackground
                                                )
                                                Icon(
                                                    Icons.Filled.Star,
                                                    contentDescription = null,
                                                    tint = tint,
                                                    modifier = Modifier.size(24.dp)
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        if (categoryListItem is CategoryListItemModel.Letter) {
                            stickyHeader {
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
                    }
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.White)
                                .heightIn(60.dp)
                        )
                    }
                }
            )
        }
    }
}

@Composable
fun EditCategoryDialog(
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
//                categoryId?.let { Text(text = "Edit") } ?: Text(text = "Add")
                Text(
                    text = stringResource(R.string.text_add),
                    style = MaterialTheme.typography.button
                )
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun CategoryListLayoutPreview() {
    CategoryListLayout(
        openDrawer = {},
        categories = PreviewData.categoriesListItemsPreview,
        onCategoryClick = {},
        openCategoryDetails = {},
        onFavoriteClick = { _, _ -> },
        onSaveCategoryClick = {},
        onCategoryNameChanged = {},
        categoryNameState = TextFieldValue(),
        closeEditCategoryDialog = {},
        onAddCategoryClick = {},
        onCategoryLongPress = {},
        openDialog = false
    )
}

@Preview(showBackground = false)
@Composable
private fun EditCategoryDialogPreview() {
    EditCategoryDialog(
        onSaveCategoryClick = {},
        onCategoryNameChanged = {},
        categoryNameState = TextFieldValue(),
        closeEditCategoryDialog = {},
        openCategoryDetails = {}
    )
}
