package dev.nelson.mot.main.presentations.home

import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.Window
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.transition.platform.MaterialContainerTransformSharedElementCallback
import dagger.hilt.android.AndroidEntryPoint
import dev.nelson.mot.main.R
import dev.nelson.mot.main.databinding.ActivityHomeBinding
import dev.nelson.mot.main.presentations.home.bottomnav.MotRoundedBottomSheetDialogFragment
import dev.nelson.mot.main.presentations.payment.PaymentActivity
import dev.nelson.mot.main.util.extention.getDataBinding
import dev.nelson.mot.main.util.showToast

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
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        menuInflater.inflate(R.menu.menu_home, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> openNavigation()
            R.id.search -> showToast(this, "search")
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
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_dehaze_24)
    }

    private fun initFab() {
//        binding.fab.setOnClickListener { moviesListFragment.expandFilterFragment() }
        binding.fab.setOnClickListener {
            val options = ActivityOptions.makeSceneTransitionAnimation(this, binding.fab,"new_payment")
            startActivity(PaymentActivity.getIntent(this), options.toBundle())
        }
//        binding.fab.setOnClickListener { binding.fab.isExpanded = true }
    }

    private fun openNavigation() {
        val bottomNavDialogFragment = MotRoundedBottomSheetDialogFragment()
        bottomNavDialogFragment.show(supportFragmentManager, bottomNavDialogFragment.tag)
    }

}
