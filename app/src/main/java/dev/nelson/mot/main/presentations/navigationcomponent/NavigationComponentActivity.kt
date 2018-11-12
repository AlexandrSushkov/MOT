package dev.nelson.mot.main.presentations.navigationcomponent

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import dev.nelson.mot.main.R
import dev.nelson.mot.main.databinding.ActivityNavigationComponentBinding
import dev.nelson.mot.main.presentations.base.BaseActivity
import dev.nelson.mot.main.util.extention.getDataBinding
import dev.nelson.mot.main.util.extention.getViewModel

class NavigationComponentActivity: BaseActivity() {

    companion object {
        fun getIntent(context: Context): Intent{
            return Intent(context, NavigationComponentActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = getDataBinding<dev.nelson.mot.main.databinding.ActivityNavigationComponentBinding>(R.layout.activity_navigation_component)
        val model = getViewModel<NavigationComponentVeiwModel>(ViewModelProviders.DefaultFactory(this.application))
        binding.viewModel = model
    }
}