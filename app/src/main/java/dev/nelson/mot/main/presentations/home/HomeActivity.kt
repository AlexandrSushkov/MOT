package dev.nelson.mot.main.presentations.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import dev.nelson.mot.main.R
import dev.nelson.mot.main.databinding.ActivityHomeBinding
import dev.nelson.mot.main.presentations.base.BaseActivity
import dev.nelson.mot.main.presentations.home.bottomnav.MotRoundedBottomSheetDialogFragment
import dev.nelson.mot.main.presentations.movieslist.MoviesListFragment
import dev.nelson.mot.main.util.extention.getDataBinding
import dev.nelson.mot.main.util.extention.getViewModel

class HomeActivity : BaseActivity() {

    companion object {
        fun getIntent(context: Context): Intent = Intent(context, HomeActivity::class.java)
    }

    lateinit var binding: ActivityHomeBinding
    lateinit var viewModel: HomeViewModel
    lateinit var moviesListFragment: MoviesListFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = getDataBinding(R.layout.activity_home)
        viewModel = getViewModel(factory)
        binding.viewModel = viewModel
        initAppBar()
        initFab()
        moviesListFragment = MoviesListFragment.getInstance()
        supportFragmentManager.beginTransaction().replace(R.id.container, moviesListFragment).commit()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_home, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            android.R.id.home -> openNavigation()
            R.id.search -> toast("search")
//            R.id.settings -> startActivity(SettingsActivity.getIntent(this))
//            R.id.show -> fab.show()
//            R.id.hide -> fab.hide()
//            R.id.legacy -> startActivity(Intent(this, MainActivity::class.java))
//            R.id.transfer_db -> startActivity(Intent(this, TransferDBActivity::class.java))
        }
        return true
    }

    override fun onBackPressed() {
        if (moviesListFragment.isFilterOpen()) moviesListFragment.collapseFilterFragment() else super.onBackPressed()
    }

    private fun initAppBar() {
        setSupportActionBar(binding.bottomAppBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_dehaze_black_24dp)
    }

    private fun initFab() {
        binding.fab.setOnClickListener { moviesListFragment.expandFilterFragment() }
    }

    private fun openNavigation() {
        val bottomNavDialogFragment = MotRoundedBottomSheetDialogFragment()
        bottomNavDialogFragment.show(supportFragmentManager, bottomNavDialogFragment.tag)
    }

    private fun toast(string: String) = Toast.makeText(this, string, Toast.LENGTH_SHORT).show()

    private fun hideBottomNav(){

    }

    private fun showBottomNav(){

    }
}
