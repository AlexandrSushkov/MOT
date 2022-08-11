package dev.nelson.mot.main.presentations.categories_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import dev.nelson.mot.main.data.mapers.toCategory
import dev.nelson.mot.main.data.model.Category
import dev.nelson.mot.main.data.room.model.category.CategoryEntity
import dev.nelson.mot.main.presentations.base.BaseFragment
import dev.nelson.mot.main.presentations.categories.CategoriesViewModel
import dev.nelson.mot.main.presentations.categories.CategoryListItemModel
import dev.nelson.mot.main.presentations.category_details.compose.CategoryDetailsComposeFragment
import dev.nelson.mot.main.presentations.payment_list.compose.widgets.TopAppBarMot
import dev.nelson.mot.main.util.compose.PreviewData

@AndroidEntryPoint
class CategoryListComposeFragment : BaseFragment() {

    private val viewModel: CategoriesViewModel by viewModels()
    private val navController by lazy { findNavController() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return ComposeView(requireContext()).apply {
            setContent {
                val categories by viewModel.categoriesFlow.collectAsState(initial = emptyList())
                CategoryListComposeFragmentLayout(
                    categories = categories,
                    onCategoryClick = { viewModel.onCategoryClick(it) },
                    onCategoryLongClick = { viewModel.onCategoryLongClick(it) }
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
            openCategoryDetailsAction.observe(viewLifecycleOwner) { openCategoryDetails(it.toCategory()) }
            openPaymentsByCategoryAction.observe(viewLifecycleOwner) { openPaymentByCategory(it.toCategory()) }
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
        onCategoryClick: (CategoryEntity) -> Unit,
        onCategoryLongClick: (CategoryEntity) -> Unit
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            TopAppBarMot(title = "Categories")
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                content = {
                    categories.forEach {
                        if (it is CategoryListItemModel.CategoryItemModel) {
                            item {
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .combinedClickable(
                                            onClick = { onCategoryClick.invoke(it.category) },
                                            onLongClick = { onCategoryLongClick.invoke(it.category) }
                                        ),
                                    shape = RoundedCornerShape(0.dp)
                                ) {
                                    Text(
                                        text = it.category.name,
                                        modifier = Modifier.padding(all = 16.dp)
                                    )
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
        )
    }
}
