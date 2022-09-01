package dev.nelson.mot.main.presentations.statistic

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.nelson.mot.main.presentations.widgets.LineChartMot

@Composable
fun StatisticScreen()  {
    Column(modifier = Modifier.fillMaxSize()) {
        Text(text = "Stat")
        Box(modifier = Modifier.padding(16.dp, vertical = 24.dp)){
            LineChartMot(items = listOf(0.2f, 0.5f, 0.1f, 0.3f))
        }
    }

}

@Preview(showBackground = true)
@Composable
private fun StatisticScreenPreview(){
    StatisticScreen()
}

