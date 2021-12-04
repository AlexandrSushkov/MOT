package dev.nelson.mot.main.presentations.category_details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import dev.nelson.mot.main.R
import dev.nelson.mot.main.data.model.Category
import dev.nelson.mot.main.databinding.ActivityCategoryDetailsBinding
import dev.nelson.mot.main.util.extention.getDataBinding
import dev.nelson.mot.main.util.extention.onDone

@AndroidEntryPoint
class CategoryDetailsFragment : BottomSheetDialogFragment() {

    private val viewModel: CategoryDetailsViewModel by viewModels()
    private lateinit var binding: ActivityCategoryDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle)
//        binding = getDataBinding(R.layout.activity_category_details)
//        binding.viewModel = viewModel
//        binding.categoryName.requestFocus()
    }

//    override fun onDetach() {
//        activity?.hideKeyboard()
//        super.onDetach()
//    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = getDataBinding(inflater, R.layout.activity_category_details, container)
        binding.viewModel = viewModel
//        NavigationUI.setupWithNavController(binding.bottomNavigation, navController)
        initListeners()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.categoryName.requestFocus()
    }

    private fun initListeners() {
        viewModel.closeAction.observe(this, { closeScreen() })
        binding.categoryName.onDone { viewModel.onSaveClick() }
    }

    private fun closeScreen() {
//        activity?.hideKeyboard()
        dismiss()
//        finish()
    }

    companion object{
        fun getInstance(category: Category? = null) : CategoryDetailsFragment{
            val bundle = Bundle().apply {
                putParcelable("category", category)
            }

            return CategoryDetailsFragment().apply { arguments = bundle }
        }
    }
}
