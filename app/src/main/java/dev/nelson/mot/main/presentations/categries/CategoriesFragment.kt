package dev.nelson.mot.main.presentations.categries

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dev.nelson.mot.main.R
import dev.nelson.mot.main.databinding.CategoriesFragmentBinding
import dev.nelson.mot.main.presentations.base.BaseFragment
import dev.nelson.mot.main.util.extention.getDataBinding

class CategoriesFragment : BaseFragment() {

    lateinit var binding:CategoriesFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = getDataBinding(inflater, R.layout.categories_fragment, container)
        return binding.root
    }

}
