package dev.nelson.mot.main.presentations.categories_list

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.IconToggleButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import dev.nelson.mot.main.data.model.Category
import dev.nelson.mot.main.data.model.CategoryListItemModel
import dev.nelson.mot.main.presentations.ui.theme.MotColors
import dev.nelson.mot.main.util.compose.PreviewData

@Composable
fun CategoryListScreen(
    openDrawer: () -> Unit,
    openCategoryDetails: (Int?) -> Unit,
    openPaymentsByCategory: (Category) -> Unit
) {
    val viewModel = hiltViewModel<CategoriesListViewModel>()
    val categories by viewModel.categoriesFlow.collectAsState(initial = emptyList())
    CategoryListLayout(
        openDrawer = openDrawer,
        categories = categories,
        onCategoryClick = openPaymentsByCategory,
        openCategoryDetails = openCategoryDetails,
        onFavoriteClick = { cat, che -> viewModel.onFavoriteClick(cat, che) }
    )
}

@Preview
@Composable
fun CategoryListLayoutPreview() {
    CategoryListLayout(
        openDrawer = {},
        categories = PreviewData.categoriesListItemsPreview,
        onCategoryClick = {},
        openCategoryDetails = {},
        onFavoriteClick = { _, _ -> },
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CategoryListLayout(
    openDrawer: () -> Unit,
    categories: List<CategoryListItemModel>,
    onCategoryClick: (Category) -> Unit,
    openCategoryDetails: (Int?) -> Unit,
    onFavoriteClick: (Category, Boolean) -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                navigationIcon = {
                    IconButton(onClick = openDrawer) {
                        Icon(Icons.Default.Menu, contentDescription = "drawer icon")
                    }
                },
                title = { Text(text = "Categories") }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { openCategoryDetails.invoke(null) },
            ) {
                Icon(Icons.Default.Add, "categories fab")
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
                                                categoryListItem.category.id?.let { categoryId ->
                                                    openCategoryDetails.invoke(categoryId)
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
                                                .align(Alignment.CenterVertically)
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
                                        .background(Color.LightGray)
                                ) {
                                    Text(
                                        text = categoryListItem.letter,
                                        modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp),
                                        fontWeight = FontWeight.Bold
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