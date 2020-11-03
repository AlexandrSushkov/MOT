package dev.nelson.mot.main.presentations.home

import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.Window
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.transition.platform.MaterialContainerTransformSharedElementCallback
import dagger.hilt.android.AndroidEntryPoint
import dev.nelson.mot.main.R
import dev.nelson.mot.main.databinding.ActivityHomeBinding
import dev.nelson.mot.main.presentations.home.bottomnav.MotRoundedBottomSheetDialogFragment
import dev.nelson.mot.main.presentations.movieslist.MoviesListFragment
import dev.nelson.mot.main.presentations.payment.NewPaymentActivity
import dev.nelson.mot.main.presentations.settings.SettingsActivity
import dev.nelson.mot.main.util.extention.getDataBinding

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    companion object {
        fun getIntent(context: Context): Intent = Intent(context, HomeActivity::class.java)
    }

    lateinit var binding: ActivityHomeBinding
    private val viewModel: HomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        window.requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS)
        setExitSharedElementCallback(MaterialContainerTransformSharedElementCallback())
        window.sharedElementsUseOverlay = false

        super.onCreate(savedInstanceState)
        binding = getDataBinding(R.layout.activity_home)
        binding.viewModel = viewModel
        initAppBar()
        initFab()
        binding.settings.setOnClickListener { startActivity(Intent(this, SettingsActivity::class.java)) }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        menuInflater.inflate(R.menu.menu_home, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> openNavigation()
            R.id.search -> toast("search")
            R.id.show -> binding.fab.show()
            R.id.hide -> binding.fab.hide()
//            R.id.legacy -> startActivity(Intent(this, MainActivity::class.java))
//            R.id.transfer_db -> startActivity(Intent(this, TransferDBActivity::class.java))
        }
        return true
    }

    private fun initAppBar() {
        setSupportActionBar(binding.bottomAppBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_dehaze_black_24dp)
    }

    private fun initFab() {
//        binding.fab.setOnClickListener { moviesListFragment.expandFilterFragment() }
        binding.fab.setOnClickListener {
            val options = ActivityOptions.makeSceneTransitionAnimation(this, binding.fab,"new_payment")
            startActivity(NewPaymentActivity.getIntent(this), options.toBundle())
        }
//        binding.fab.setOnClickListener { binding.fab.isExpanded = true }
    }

    private fun openNavigation() {
        val bottomNavDialogFragment = MotRoundedBottomSheetDialogFragment()
        bottomNavDialogFragment.show(supportFragmentManager, bottomNavDialogFragment.tag)
    }

    private fun toast(string: String) = Toast.makeText(this, string, Toast.LENGTH_SHORT).show()

}
