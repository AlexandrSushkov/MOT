package dev.nelson.mot.main.presentations.settings

import android.os.Bundle
import android.view.MenuItem
import dev.nelson.mot.main.R
import dev.nelson.mot.main.databinding.ActivitySettingsBinding
import dev.nelson.mot.main.presentations.base.BaseActivity
import dev.nelson.mot.main.util.extention.getDataBinding

class SettingsActivity: BaseActivity() {

    lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = getDataBinding(R.layout.activity_settings)
    }

}
