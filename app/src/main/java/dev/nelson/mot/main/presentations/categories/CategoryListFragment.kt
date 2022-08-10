package dev.nelson.mot.main.presentations.categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import dev.nelson.mot.main.HomeNavigationDirections
import dev.nelson.mot.main.R
import dev.nelson.mot.main.data.mapers.toCategory
import dev.nelson.mot.main.data.model.Category
import dev.nelson.mot.main.databinding.CategoryListFragmentBinding
import dev.nelson.mot.main.presentations.base.BaseFragment
import dev.nelson.mot.main.presentations.category_details.compose.CategoryDetailsComposeFragment
import dev.nelson.mot.main.util.extention.getDataBinding

@AndroidEntryPoint
class CategoryListFragment : BaseFragment() {

    lateinit var binding: CategoryListFragmentBinding
    private val viewModel: CategoriesViewModel by viewModels()
    private val navController by lazy { findNavController() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = getDataBinding(inflater, R.layout.category_list_fragment, container)
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        val firstVisiblePosition: Int = (binding.categoryList.layoutManager as GridLayoutManager).findFirstCompletelyVisibleItemPosition()
        viewModel.onScrollChanged.accept(firstVisiblePosition)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
    }

    private fun initListeners() {
        with(binding) {
            categoryList.addOnScrollListener(getCategoryOnScrollListener(categoryList))
        }

        with(viewModel) {
            swipeToDeleteCallback.observe(viewLifecycleOwner) {
                val itemTouchHelper = ItemTouchHelper(it)
                itemTouchHelper.attachToRecyclerView(binding.categoryList)
            }
//            openCategoryDetailsAction.observe(viewLifecycleOwner, { navController.navigate(R.id.nav_menu_item_payment_list) })
            openCategoryDetailsAction.observe(viewLifecycleOwner) { openCategoryDetails(it.toCategory()) }
            openPaymentsByCategoryAction.observe(viewLifecycleOwner) { openPaymentByCategory(it.toCategory()) }
        }
    }

    private fun openCategoryDetails(category: Category) {
//        val action = HomeNavigationDirections.openCategoryDetails()
//            .apply { this.category = category }
//        navController.navigate(action)
//        private fun openCategoryDelails() {
//        val categoryDialogFragment = CategoryDetailsFragment.getInstance(category)
//        categoryDialogFragment.show(childFragmentManager, categoryDialogFragment.tag)
        val categoryDialogFragment = CategoryDetailsComposeFragment.getInstance(category)
        categoryDialogFragment.show(childFragmentManager, categoryDialogFragment.tag)
//        }
    }

    private fun openPaymentByCategory(category: Category) {
        val action = HomeNavigationDirections.openPaymentsByCategory()
            .apply { this.category = category }
        navController.navigate(action)
    }

    private fun getCategoryOnScrollListener(categoryList: RecyclerView): RecyclerView.OnScrollListener {
        return object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val visiblePosition: Int = (categoryList.layoutManager as GridLayoutManager).findFirstCompletelyVisibleItemPosition()
                viewModel.onScrollChanged.accept(visiblePosition)
            }
        }
    }

}
