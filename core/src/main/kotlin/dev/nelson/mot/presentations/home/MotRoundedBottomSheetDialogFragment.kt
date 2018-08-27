package dev.nelson.mot.presentations.home

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.navigation.NavigationView
import dev.nelson.mot.R

class MotRoundedBottomSheetDialogFragment : BottomSheetDialogFragment() {

    override fun getTheme(): Int = R.style.MotRoundedBottomSheetDialogTheme

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog = BottomSheetDialog(requireContext(), theme)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_bottomsheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navigationView: NavigationView = view.findViewById(R.id.bottom_navigation)
        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_menu_item_home -> toast("recent payments")
                R.id.nav_statistic_item -> toast("category")
                R.id.nav_categorise_item -> toast("statistic")
                R.id.nav_about_item -> toast("about")
            }
            finish()
            true
        }
    }

    private fun toast(string: String) = Toast.makeText(context, string, Toast.LENGTH_SHORT).show()

    private fun finish() {
        activity!!.supportFragmentManager.beginTransaction().remove(this).commit()
    }

}
