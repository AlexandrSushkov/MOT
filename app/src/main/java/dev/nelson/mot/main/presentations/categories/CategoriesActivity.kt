package dev.nelson.mot.main.presentations.categories

import android.os.Bundle
import dev.nelson.mot.main.R
import dev.nelson.mot.main.databinding.CategoriesFragmentBinding
import dev.nelson.mot.main.presentations.base.BaseActivity
import dev.nelson.mot.main.util.extention.getDataBinding

class CategoriesActivity : BaseActivity() {

    lateinit var binding: CategoriesFragmentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = getDataBinding(R.layout.categories_fragment)
    }

}
