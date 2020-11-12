package dev.nelson.mot.main.presentations.categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import dev.nelson.mot.main.R
import dev.nelson.mot.main.databinding.CategoriesFragmentBinding
import dev.nelson.mot.main.presentations.base.BaseActivity
import dev.nelson.mot.main.presentations.base.BaseFragment
import dev.nelson.mot.main.util.extention.getDataBinding

@AndroidEntryPoint
class CategoriesFragment : BaseFragment() {

    lateinit var binding: CategoriesFragmentBinding
    private val viewModel: CategoriesViewModel by viewModels()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = getDataBinding(inflater, R.layout.categories_fragment, container)
        binding.viewModel = viewModel
        return binding.root
    }

}
