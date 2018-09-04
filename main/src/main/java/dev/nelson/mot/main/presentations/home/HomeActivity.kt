package dev.nelson.mot.main.presentations.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dev.nelson.mot.activity.MainActivity
import dev.nelson.mot.main.R
import dev.nelson.mot.presentations.base.BaseActivity
import dev.nelson.mot.presentations.home.MotRoundedBottomSheetDialogFragment
import dev.nelson.mot.presentations.payment.EditPaymentActivity
import dev.nelson.mot.presentations.settings.SettingsActivity

class HomeActivity : BaseActivity() {

    companion object {
        fun getIntent(context: Context): Intent = Intent(context, HomeActivity::class.java)
    }

    lateinit var fab: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        val bar: BottomAppBar = this.findViewById(R.id.bottom_app_bar)
        fab = this.findViewById(R.id.fab)

        setSupportActionBar(bar)
        fab.setOnClickListener { startActivity(EditPaymentActivity.getIntent(this)) }

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
            R.id.settings -> startActivity(SettingsActivity.getIntent(this))
            R.id.show -> fab.show()
            R.id.hide -> fab.hide()
            R.id.legacy -> startActivity(Intent(this, MainActivity::class.java))
        }
        return true
    }

    private fun openNavigation() {
        val bottomNavDialogFragment = MotRoundedBottomSheetDialogFragment()
        bottomNavDialogFragment.show(supportFragmentManager, bottomNavDialogFragment.tag)
    }

    private fun toast(string: String) = Toast.makeText(this, string, Toast.LENGTH_SHORT).show()
}
