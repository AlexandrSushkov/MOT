package dev.nelson.mot.presentations.home

import android.arch.lifecycle.ViewModelProvider
import android.content.Context
import android.content.Intent
import android.os.Bundle
import dev.nelson.mot.R
import dev.nelson.mot.databinding.ActivityHomeBinding
import dev.nelson.mot.extentions.getDataBinding
import dev.nelson.mot.extentions.getViewModel
import dev.nelson.mot.presentations.base.BaseActivity
import javax.inject.Inject

class HomeActivity : BaseActivity() {

    companion object {
        fun getIntent(context: Context): Intent = Intent(context, HomeActivity::class.java)
    }

    @Inject lateinit var factory: ViewModelProvider.Factory
    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = getDataBinding(R.layout.activity_home)
        binding.viewModel = getViewModel(factory)
    }

}
