package dev.nelson.mot.main.presentations.categories_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconToggleButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import dev.nelson.mot.main.HomeNavigationDirections
import dev.nelson.mot.main.data.model.Category
import dev.nelson.mot.main.presentations.base.BaseFragment
import dev.nelson.mot.main.presentations.categories.CategoriesListComposeViewModel
import dev.nelson.mot.main.presentations.categories.CategoryListItemModel
import dev.nelson.mot.main.presentations.category_details.compose.CategoryDetailsComposeFragment
import dev.nelson.mot.main.presentations.payment_list.compose.widgets.TopAppBarMot
import dev.nelson.mot.main.presentations.ui.theme.MotColors
import dev.nelson.mot.main.util.compose.PreviewData

@AndroidEntryPoint
class CategoryListComposeFragment : BaseFragment() {

    private val viewModel: CategoriesListComposeViewModel by viewModels()
    private val navController by lazy { findNavController() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return ComposeView(requireContext()).apply {
            setContent {
                val categories by viewModel.categoriesFlow.collectAsState(initial = emptyList())
                CategoryListComposeFragmentLayout(
                    categories = categories,
                    onCategoryClick = { viewModel.onCategoryClick(it) },
                    onCategoryLongClick = { viewModel.onCategoryLongClick(it) },
                    onFavoriteClick = { cat, che -> viewModel.onFavoriteClick(cat, che) }
                )
            }
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
    }

    private fun initListeners() {
        with(viewModel) {
            openCategoryDetailsAction.observe(viewLifecycleOwner) { openCategoryDetails(it) }
            openPaymentsByCategoryAction.observe(viewLifecycleOwner) { openPaymentByCategory(it) }
        }
    }

    private fun openCategoryDetails(category: Category) {
        val categoryDialogFragment = CategoryDetailsComposeFragment.getInstance(category)
        categoryDialogFragment.show(childFragmentManager, categoryDialogFragment.tag)
    }

    private fun openPaymentByCategory(category: Category) {
        val action = HomeNavigationDirections.openPaymentsByCategory()
            .apply { this.category = category }
        navController.navigate(action)
    }

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    fun CategoryListComposeFragmentLayout(
        categories: List<CategoryListItemModel>,
        onCategoryClick: (Category) -> Unit,
        onCategoryLongClick: (Category) -> Unit,
        onFavoriteClick: (Category, Boolean) -> Unit
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            TopAppBarMot(title = "Categories")
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                content = {
                    categories.forEach {
                        if (it is CategoryListItemModel.CategoryItemModel) {
                            item {
                                var checked by remember { mutableStateOf(it.category.isFavorite) }

                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .combinedClickable(
                                            onClick = { onCategoryClick.invoke(it.category) },
                                            onLongClick = { onCategoryLongClick.invoke(it.category) }
                                        ),
                                    shape = RoundedCornerShape(0.dp)
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 8.dp, horizontal = 16.dp)
                                    ) {
                                        Text(
                                            text = it.category.name,
                                            modifier = Modifier
                                                .weight(1f)
                                                .align(Alignment.CenterVertically)
                                        )
                                        IconToggleButton(
                                            checked = checked,
                                            onCheckedChange = { isChecked ->
                                                checked = isChecked
                                                onFavoriteClick.invoke(it.category, isChecked)
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
                        if (it is CategoryListItemModel.Letter) {
                            stickyHeader {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(Color.LightGray)
                                ) {
                                    Text(
                                        text = it.letter,
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

    @Preview
    @Composable
    fun CategoryListComposeFragmentLayoutPreview() {
        CategoryListComposeFragmentLayout(
            categories = PreviewData.categoryListPreview,
            onCategoryClick = {},
            onCategoryLongClick = {},
            onFavoriteClick = { _, _ -> },
        )
    }
}
