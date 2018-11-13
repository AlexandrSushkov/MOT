package dev.nelson.mot.main.presentations.navigationcomponent.bottomnav

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.navigation.NavigationView
import dev.nelson.mot.main.R

class NavBottomSheetDialogFragment : BottomSheetDialogFragment() {

    companion object {
        fun newInstance(): NavBottomSheetDialogFragment {
            return NavBottomSheetDialogFragment()
        }
    }

    override fun getTheme(): Int = R.style.MotRoundedBottomSheetDialogTheme

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog = BottomSheetDialog(requireContext(), theme)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_nav_rounded_botton_dialog_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navigationView: NavigationView = view.findViewById(R.id.bottom_navigation)
        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_home -> {
                    if (isCurrentFragment(R.id.navHomeFragment).not()){
                        findNavController().popBackStack()
                        findNavController().navigate(R.id.navHomeFragment)
                    }
                }
                R.id.nav_about -> {
                    if (isCurrentFragment(R.id.navAboutFragment).not()){
                        findNavController().popBackStack()
                        findNavController().navigate(R.id.navAboutFragment)
                    }
                }
            }
            finish()
            true
        }
    }

    private fun toast(string: String) = Toast.makeText(context, string, Toast.LENGTH_SHORT).show()

    private fun isCurrentFragment(fragmentId: Int): Boolean  = findNavController().currentDestination?.id == fragmentId

    private fun finish() {
        activity?.supportFragmentManager
                ?.beginTransaction()
                ?.remove(this)
                ?.commit()
    }

    private fun openHome(){
//        val action = NavDi

    }
}