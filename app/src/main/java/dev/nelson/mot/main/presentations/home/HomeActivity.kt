package dev.nelson.mot.main.presentations.home

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.core.content.res.ResourcesCompat
import androidx.navigation.findNavController
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import dev.nelson.mot.main.R
import dev.nelson.mot.main.databinding.ActivityHomeBinding
import dev.nelson.mot.main.presentations.base.BaseActivity
import dev.nelson.mot.main.presentations.category_details.compose.CategoryDetailsComposeFragment
import dev.nelson.mot.main.presentations.home.bottomnav.MotRoundedBottomSheetDialogFragment
import dev.nelson.mot.main.presentations.payment_list.PaymentListFragmentDirections
import dev.nelson.mot.main.presentations.payment_list.compose.PaymentListComposeFragmentDirections
import dev.nelson.mot.main.util.extention.getDataBinding

@AndroidEntryPoint
class HomeActivity : BaseActivity() {

    lateinit var binding: ActivityHomeBinding
    private val viewModel: HomeViewModel by viewModels()
    private val navController by lazy { findNavController(R.id.navigation_host_fragment) }

    override fun onCreate(savedInstanceState: Bundle?) {
//        window.requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS)
//        setExitSharedElementCallback(MaterialContainerTransformSharedElementCallback())
//        window.sharedElementsUseOverlay = false

        super.onCreate(savedInstanceState)
        binding = getDataBinding(R.layout.activity_home)
        binding.viewModel = viewModel
        initAppBar()
        initFab()
        initListeners()
//        binding.bottomAppBar.performHide()
//        binding.fab.hide()


        //setNavigationBarButtonsColor(this, )
    }

    private fun setNavigationBarButtonsColor(activity: Activity, navigationBarColor: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val decorView = activity.window.decorView
            val flags = decorView.systemUiVisibility
//            if (isColorLight(navigationBarColor)) {
//                flags = View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
//            } else {
//                flags = View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
//            }
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
        }
    }

    private fun isColorLight(color: Int): Boolean {
        val darkness = 1 - (0.299 * Color.red(color) + 0.587 * Color.green(color) + 0.114 * Color.blue(color)) / 255;
        return darkness < 0.5;
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_home, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> openNavigation()
//            R.id.search -> showToast(this, "search")
//            R.id.show -> binding.fab.show()
//            R.id.hide -> binding.fab.hide()
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
        binding.fab.setOnClickListener {
            when (navController.currentDestination?.id) {
                R.id.nav_menu_item_payment_list -> openPaymentDetails()
                R.id.nav_menu_item_payment_list_compose -> openPaymentDetailsCompose()
                R.id.nav_menu_item_categories -> openCategoryDetails()
            }
        }
    }

    private fun openPaymentDetails() {
        val openPaymentDetailsComposeAction = PaymentListFragmentDirections.goToPaymentFragment()
        navController.navigate(openPaymentDetailsComposeAction)
    }

    private fun openPaymentDetailsCompose() {
        val openPaymentDetailsComposeAction = PaymentListComposeFragmentDirections.goToPaymentDetailsCompose()
        navController.navigate(openPaymentDetailsComposeAction)
    }

    private fun openCategoryDetails() {
//        val categoryDialogFragment = CategoryDetailsFragment.getInstance()
//        categoryDialogFragment.show(supportFragmentManager, categoryDialogFragment.tag)

        val categoryDetailsComposeFragment = CategoryDetailsComposeFragment.getInstance()
        categoryDetailsComposeFragment.show(supportFragmentManager, categoryDetailsComposeFragment.tag)

    }

    private fun initListeners() {
        initNavControllerListener()
    }

    private fun initNavControllerListener() {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.nav_menu_item_payment_list -> binding.apply {
                    bottomAppBar.performShow()
                    fab.apply {
                        setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.ic_baseline_add_24, theme))
                        show()
                    }
                }
                R.id.nav_menu_item_categories -> binding.apply {
                    bottomAppBar.performShow()
                    fab.apply {
                        setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.ic_baseline_show_chart_24, theme))
                        show()
                    }
                }
                R.id.nav_menu_item_statistic -> binding.apply {
                    bottomAppBar.performHide()
//                    fab.hide()
                }
                R.id.nav_menu_item_movies_list -> binding.apply {
//                    fab.hide()
                }
                R.id.nav_menu_item_settings -> binding.apply {
//                    fab.hide()
                    bottomAppBar.performHide()
                }
            }
        }
    }

    private fun openNavigation() {
        val bottomNavDialogFragment = MotRoundedBottomSheetDialogFragment()
        bottomNavDialogFragment.show(supportFragmentManager, bottomNavDialogFragment.tag)
    }

    private fun showSneakBar() {
        val snackbar: Snackbar = Snackbar.make(
            binding.coordinatorLayout2, "afaf",
            Snackbar.LENGTH_LONG
        )
        snackbar.setAction("undo") { _ -> { } }
        snackbar.show()
    }

    companion object {
        fun getIntent(context: Context): Intent = Intent(context, HomeActivity::class.java)
    }

    private fun showKeyboard() {
        val imm: InputMethodManager? = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        imm?.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0)
    }

}
