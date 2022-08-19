package dev.nelson.mot.main.presentations.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.tooling.preview.Preview
import dagger.hilt.android.AndroidEntryPoint
import dev.nelson.mot.main.presentations.base.BaseFragment

@AndroidEntryPoint
class SettingsFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return ComposeView(requireContext()).apply {
            setContent {
                SettingsScreenLayout()
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
private fun SettingsScreenLayout() {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Text("Compose settings")
    }
}
