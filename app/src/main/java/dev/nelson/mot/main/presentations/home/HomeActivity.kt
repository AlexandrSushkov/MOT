package dev.nelson.mot.main.presentations.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dev.nelson.mot.main.R
import dev.nelson.mot.main.presentations.base.BaseActivity
import dev.nelson.mot.main.presentations.home.bottomnav.MotRoundedBottomSheetDialogFragment
import dev.nelson.mot.main.presentations.movieslist.MoviesListFragment

class HomeActivity : BaseActivity() {

    companion object {
        fun getIntent(context: Context): Intent = Intent(context, HomeActivity::class.java)
    }

    lateinit var fab: FloatingActionButton
    lateinit var moviesListFragment: MoviesListFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        val homeViewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)
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

    private fun initAppBar() {
        val bar: BottomAppBar = this.findViewById(R.id.bottom_app_bar)
        setSupportActionBar(bar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_dehaze_black_24dp)
    }

    private fun initFab() {
        fab = findViewById(R.id.fab)
        fab.setOnClickListener {
            //todo open filter fragment
            moviesListFragment.expandFilterFragment()
        }
    }

    private fun openNavigation() {
        val bottomNavDialogFragment = MotRoundedBottomSheetDialogFragment()
        bottomNavDialogFragment.show(supportFragmentManager, bottomNavDialogFragment.tag)
    }

    private fun toast(string: String) = Toast.makeText(this, string, Toast.LENGTH_SHORT).show()
}
