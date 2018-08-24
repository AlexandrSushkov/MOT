package dev.nelson.mot.presentations.settings

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import com.google.android.material.bottomappbar.BottomAppBar
import dev.nelson.mot.R
import dev.nelson.mot.presentations.base.BaseActivity

class SettingsActivity: BaseActivity() {

    companion object {
        fun getIntent(context: Context): Intent = Intent(context, SettingsActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val bottomAppBar: BottomAppBar = findViewById(R.id.bottom_bar)
        setSupportActionBar(bottomAppBar)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            android.R.id.home -> finish()
        }
        return true
    }
}