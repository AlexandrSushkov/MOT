package dev.nelson.mot.main.presentations.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import dev.nelson.mot.main.R
import dev.nelson.mot.main.databinding.FragmentSettingsBinding
import dev.nelson.mot.main.presentations.base.BaseFragment
import dev.nelson.mot.main.util.extention.getDataBinding

class SettingsFragment : BaseFragment() {

    lateinit var binding: FragmentSettingsBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = getDataBinding(inflater, R.layout.fragment_settings, container)
//        binding.backButton.setOnClickListener { findNavController().popBackStack() }
        binding.composeView.setContent {  }
        return binding.root
    }


    @Composable
    private fun SettingsScreenLayout(){
        MaterialTheme {
            Text("Compose settings")
        }
    }

}