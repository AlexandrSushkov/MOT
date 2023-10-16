package dev.nelson.mot.main.presentations.shared

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.nelson.mot.R
import dev.nelson.mot.core.ui.MotMaterialTheme
import dev.nelson.mot.main.data.model.Category
import dev.nelson.mot.main.presentations.widgets.MotSingleLineText
import dev.nelson.mot.main.util.compose.PreviewData
import dev.utils.preview.MotPreview
import kotlinx.coroutines.launch

@Composable
fun CategoriesListBottomSheet(
    categories: List<Category>,
    selectedCategory: Category? = null,
    onCategoryClick: (Category) -> Unit
) {
    val scope = rememberCoroutineScope()
    val layColumnState = rememberLazyListState()
    Column(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            state = layColumnState,
            modifier = Modifier
                .fillMaxSize()
        ) {
            categories.forEachIndexed { index, category ->
                item {
                    val firstFavoriteItemIndex =
                        categories.find { it.isFavorite }?.let { categories.indexOf(it) }
                    val lastFavoriteItemIndex =
                        categories.findLast { it.isFavorite }?.let { categories.indexOf(it) }
                    val shape = getFavoriteCategoryContainerShape(firstFavoriteItemIndex, lastFavoriteItemIndex, index)
                    val isSelected = selectedCategory?.id == category.id
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                                .clip(shape)
                                .clickable {
                                    onCategoryClick.invoke(category)
                                    scope.launch { layColumnState.scrollToItem(0) }
                                },
                            tonalElevation = if (category.isFavorite) 4.dp else 0.dp
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        vertical = 12.dp,
                                        horizontal = 16.dp
                                    ),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                MotSingleLineText(
                                    text = category.name,
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(end = 8.dp)
                                )
                                if (isSelected) {
                                    Icon(
                                        Icons.Filled.Done,
                                        modifier = Modifier.size(24.dp),
                                        contentDescription = stringResource(id = R.string.accessibility_done_icon),
                                        tint = MaterialTheme.colorScheme.secondary
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

private fun getFavoriteCategoryContainerShape(
    firstItemIndex: Int?,
    lastItemIndex: Int?,
    currentItemIndex: Int
): Shape {
    val cornerRadius = 24.dp

    val topRoundedCornerShape = RoundedCornerShape(
        topStart = cornerRadius,
        topEnd = cornerRadius
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

@MotPreview
@Composable
private fun CategoriesListBottomSheetPreview() {
    val categories = PreviewData.categoriesSelectListItemsPreview
    MotMaterialTheme {
        CategoriesListBottomSheet(
            categories = categories,
            onCategoryClick = {},
            selectedCategory = categories[0]
        )
    }
}
