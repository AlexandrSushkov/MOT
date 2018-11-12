package dev.nelson.mot.main.presentations.navigationcomponent

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomappbar.BottomAppBar
import dev.nelson.mot.main.R
import dev.nelson.mot.main.databinding.ActivityNavigationComponentBinding
import dev.nelson.mot.main.presentations.base.BaseActivity
import dev.nelson.mot.main.presentations.navigationcomponent.bottomnav.NavBottomSheetDialogFragment
import dev.nelson.mot.main.util.extention.getDataBinding
import dev.nelson.mot.main.util.extention.getViewModel

class NavigationComponentActivity : BaseActivity() {

    companion object {
        fun getIntent(context: Context): Intent {
            return Intent(context, NavigationComponentActivity::class.java)
        }
    }

    private lateinit var binding: ActivityNavigationComponentBinding
    private lateinit var viewModel: NavigationComponentVeiwModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = getDataBinding(R.layout.activity_navigation_component)
        viewModel = getViewModel(ViewModelProvider.AndroidViewModelFactory.getInstance(this.application))
        binding.viewModel = viewModel
        initBottomSheetAppBar()
        setUpNavigation()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            android.R.id.home -> openNavigation()
        }
        return true
    }

    private fun initBottomSheetAppBar() {
//        setSupportActionBar(binding.bottomAppBar)
        val bar: BottomAppBar = this.findViewById(R.id.bottom_app_bar)
        setSupportActionBar(bar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_dehaze_black_24dp)
    }

    private fun setUpNavigation() {
        val host: NavHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment? ?: return
        val navController = host.navController
        val bar: BottomAppBar = this.findViewById(R.id.bottom_app_bar)
        NavigationUI.setupWithNavController(bar, navController)
    }

    private fun openNavigation() {
        val bottomSheetNavigation = NavBottomSheetDialogFragment.newInstance()
        bottomSheetNavigation.show(supportFragmentManager, bottomSheetNavigation.tag)
    }
}