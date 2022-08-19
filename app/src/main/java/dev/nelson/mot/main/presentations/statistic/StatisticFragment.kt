package dev.nelson.mot.main.presentations.statistic

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.tooling.preview.Preview
import dev.nelson.mot.main.presentations.base.BaseFragment

class StatisticFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreateView(inflater, container, savedInstanceState)
        return ComposeView(requireContext()).apply {
            setContent {
                StatisticLayout()
            }
        }
    }

    @Composable
    fun StatisticLayout() {
        MaterialTheme {
            Text(text = "statistic")
        }
    }

    @Preview
    @Composable
    fun PhotographerCardPreview() {
        StatisticLayout()
    }

}

