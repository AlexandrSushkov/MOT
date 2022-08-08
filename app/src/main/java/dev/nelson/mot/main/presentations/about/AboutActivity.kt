package dev.nelson.mot.main.presentations.about

import android.os.Bundle
import dev.nelson.mot.main.R
import dev.nelson.mot.main.databinding.FragmentAboutBinding
import dev.nelson.mot.main.presentations.base.BaseActivity
import dev.nelson.mot.main.util.extention.getDataBinding

class AboutActivity: BaseActivity() {

    lateinit var binding: FragmentAboutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = getDataBinding(R.layout.fragment_about)
        // TODO: not implemented yet
    }
}
