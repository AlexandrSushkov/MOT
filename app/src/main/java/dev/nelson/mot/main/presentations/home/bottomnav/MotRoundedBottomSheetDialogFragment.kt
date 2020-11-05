package dev.nelson.mot.main.presentations.home.bottomnav

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import dev.nelson.mot.main.R
import dev.nelson.mot.main.databinding.FragmentBottomsheetBinding
import dev.nelson.mot.main.presentations.statistic.StatisticActivity
import dev.nelson.mot.main.presentations.about.AboutActivity
import dev.nelson.mot.main.presentations.categories.CategoriesActivity
import dev.nelson.mot.main.presentations.home.HomeViewModel
import dev.nelson.mot.main.presentations.settings.SettingsActivity
import dev.nelson.mot.main.util.extention.getDataBinding
import dev.nelson.mot.main.util.showToast

@AndroidEntryPoint
class MotRoundedBottomSheetDialogFragment : BottomSheetDialogFragment() {

    lateinit var binding: FragmentBottomsheetBinding
    private val viewModel: HomeViewModel by viewModels()

    override fun getTheme(): Int = R.style.RoundedBottomSheetDialogTheme

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog = BottomSheetDialog(requireContext(), theme)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = getDataBinding(inflater, R.layout.fragment_bottomsheet, container)
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.bottomNavigation.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_menu_item_recent_payments -> showToast(context, "movies")
                R.id.nav_menu_item_categories -> { startActivity(Intent(context, CategoriesActivity::class.java)) }
                R.id.nav_menu_item_statistic -> { startActivity(Intent(context, StatisticActivity::class.java)) }
                R.id.nav_menu_item_settings -> { startActivity(Intent(context, SettingsActivity::class.java)) }
                R.id.nav_menu_item_about -> { startActivity(Intent(context, AboutActivity::class.java)) }
            }
            finish()
            true
        }
    }

    private fun finish() {
        activity?.supportFragmentManager?.beginTransaction()?.remove(this)?.commit()
    }

}

//
//override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//    super.onViewCreated(view, savedInstanceState)
//    val navigationView: NavigationView = view.findViewById(R.id.bottom_navigation)
//    navigationView.setNavigationItemSelectedListener { menuItem ->
//        when (menuItem.itemId) {
//            R.id.nav_home -> {
//                if (isCurrentFragment(R.id.navHomeFragment).not()){
//                    findNavController().popBackStack()
//                    findNavController().navigate(R.id.navHomeFragment)
//                }
//            }
//            R.id.nav_about -> {
//                if (isCurrentFragment(R.id.navAboutFragment).not()){
//                    findNavController().popBackStack()
//                    findNavController().navigate(R.id.navAboutFragment)
//                }
//            }
//        }
//        finish()
//        true
//    }
//}
//
//private fun isCurrentFragment(fragmentId: Int): Boolean  = findNavController().currentDestination?.id == fragmentId
//
//private fun finish() {
//    activity?.supportFragmentManager
//        ?.beginTransaction()
//        ?.remove(this)
//        ?.commit()
//}
