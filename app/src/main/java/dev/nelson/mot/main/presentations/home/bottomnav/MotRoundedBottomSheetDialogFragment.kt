package dev.nelson.mot.main.presentations.home.bottomnav

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import dev.nelson.mot.main.R
import dev.nelson.mot.main.databinding.FragmentBottomsheetBinding
import dev.nelson.mot.main.presentations.home.HomeViewModel
import dev.nelson.mot.main.util.extention.getDataBinding

@AndroidEntryPoint
class MotRoundedBottomSheetDialogFragment : BottomSheetDialogFragment() {

    lateinit var binding: FragmentBottomsheetBinding
    private val viewModel: HomeViewModel by viewModels()
    private val navController by lazy { findNavController() }

    override fun getTheme(): Int = R.style.RoundedBottomSheetDialogTheme

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog = BottomSheetDialog(requireContext(), theme)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = getDataBinding(inflater, R.layout.fragment_bottomsheet, container)
        binding.viewModel = viewModel
        setupWithNavController(binding.bottomNavigation, navController)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        binding.bottomNavigation.setNavigationItemSelectedListener { menuItem ->
//            when (menuItem.itemId) {
//                R.id.nav_menu_item_recent_payments -> safeNavigate(R.id.moviesListFragment)
//                R.id.nav_menu_item_categories -> safeNavigate(R.id.categoriesFragment)
//                R.id.nav_menu_item_statistic -> safeNavigate(R.id.statisticFragment)
//                R.id.nav_menu_item_settings -> safeNavigate(R.id.settingsFragment)
//            }
//            finish()
//            true
//        }
        binding.bottomNavigation.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_menu_item_payment_list -> safeNavigate(R.id.nav_menu_item_payment_list)
//                R.id.nav_menu_item_payment_list_compose -> startActivity(Intent(context, PaymentListComposeActivity::class.java))
                R.id.nav_menu_item_payment_list_compose -> safeNavigate(R.id.nav_menu_item_payment_list_compose)
                R.id.nav_menu_item_categories -> safeNavigate(R.id.nav_menu_item_categories)
                R.id.nav_menu_item_statistic -> safeNavigate(R.id.nav_menu_item_statistic)
                R.id.nav_menu_item_movies_list -> safeNavigate(R.id.nav_menu_item_movies_list)
                R.id.nav_menu_item_settings -> safeNavigate(R.id.nav_menu_item_settings)
            }
            finish()
            true
        }

    }

    private fun finish() {
        activity?.supportFragmentManager
            ?.beginTransaction()
            ?.remove(this)
            ?.commit()
    }

    /**
     * - if destination is open fragment - do nothing
     * - if fragments exist in stack - pop back to fragment
     * - if fragment doesn't exist in the stack - open new one
     *
     * @param id destination id
     */
    private fun safeNavigate(@IdRes id: Int) {
        if (navController.currentDestination?.id != id) {
//            if (navController.graph.contains(id)) {
//                navController.popBackStack(id, false)
//            } else {
            if(navController.popBackStack(id,false).not()){
                navController.navigate(id)
            }
//            }
        }
    }

}
