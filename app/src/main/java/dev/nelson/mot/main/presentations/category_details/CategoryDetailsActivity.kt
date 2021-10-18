package dev.nelson.mot.main.presentations.category_details

import android.os.Bundle
import androidx.activity.viewModels
import androidx.navigation.findNavController
import dagger.hilt.android.AndroidEntryPoint
import dev.nelson.mot.main.R
import dev.nelson.mot.main.databinding.ActivityCategoryDetailsBinding
import dev.nelson.mot.main.presentations.base.BaseActivity
import dev.nelson.mot.main.util.extention.getDataBinding
import dev.nelson.mot.main.util.extention.hideKeyboard
import dev.nelson.mot.main.util.extention.onDone

@AndroidEntryPoint
class CategoryDetailsActivity : BaseActivity() {

    private val viewModel: CategoryDetailsViewModel by viewModels()
    private lateinit var binding: ActivityCategoryDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = getDataBinding(R.layout.activity_category_details)
        binding.viewModel = viewModel
        initListeners()
    }

    private fun initListeners() {
        viewModel.closeAction.observe(this, { closeScreen() })
        binding.categoryName.onDone { viewModel.onSaveClick(binding.saveButton) }
    }

    private fun closeScreen() {
        this.hideKeyboard()
        finish()
    }
}
