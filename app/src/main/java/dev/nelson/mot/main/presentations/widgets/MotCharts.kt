package dev.nelson.mot.main.presentations.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.mikephil.charting.charts.BarChart
import dev.theme.MotColors
import kotlin.random.Random

@Preview(showBackground = true)
@Composable
fun LineChartMotPreview() {
    LineChartMot(listOf(0.2f, 0.5f, 0.1f, 0.3f))
}

/**
 * Simple line chart can be created with a row.
 * To make it work correct sum of all weights of all item MUST me 1.
 * 1 = 100%
 * 0.3 == 30%
 * 0.01 == 1%
 *
 */
@Composable
fun LineChartMot(items: List<Float>){
    val random = Random(MotColors.chartColors.size)
    Row(modifier = Modifier
        .clip(RoundedCornerShape(corner = CornerSize(8.dp)))
        .height(20.dp)
        .fillMaxWidth()) {
        items.forEach() {
            val colorId = random.nextInt(MotColors.chartColors.size)
            Box(Modifier.background(MotColors.chartColors[colorId])
                .fillMaxSize()
                .weight(it)){}
        }
    }
}
