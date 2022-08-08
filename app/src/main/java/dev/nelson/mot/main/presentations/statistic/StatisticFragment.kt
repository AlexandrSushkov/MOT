package dev.nelson.mot.main.presentations.statistic

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import dev.nelson.mot.main.R
import dev.nelson.mot.main.databinding.ActivityStatisticBinding
import dev.nelson.mot.main.presentations.base.BaseFragment
import dev.nelson.mot.main.util.extention.getDataBinding

class StatisticFragment : BaseFragment() {

    lateinit var binding: ActivityStatisticBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = getDataBinding(inflater, R.layout.activity_statistic, container)
        binding.composeView.setContent { StatisticLayout() }
        return binding.root
    }

    @Composable
    fun StatisticLayout() {
        MaterialTheme {
            Scaffold(
                topBar = { TopAppBar({ Text(text = "Statistic") }) },
                content = {}
            )
        }
    }

    @Preview
    @Composable
    fun PhotographerCardPreview() {
        StatisticLayout()
    }

}

