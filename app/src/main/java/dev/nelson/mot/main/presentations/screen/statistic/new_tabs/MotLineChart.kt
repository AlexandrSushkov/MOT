package dev.nelson.mot.main.presentations.screen.statistic.new_tabs

import android.graphics.Color
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF
import dev.nelson.mot.R
import dev.nelson.mot.core.ui.MotMaterialTheme
import dev.nelson.mot.core.ui.motIsDarkTheme
import dev.nelson.mot.core.ui.view_state.AppThemeViewState
import dev.nelson.mot.main.presentations.screen.statistic.SelectedCategoryViewState
import dev.nelson.mot.main.util.compose.PreviewData
import dev.nelson.mot.main.util.constant.Constants
import dev.nelson.mot.main.util.extention.convertMillisecondsToDate
import dev.utils.preview.MotPreview

val centsValueFormatter = object : ValueFormatter() {
    override fun getFormattedValue(value: Float): String {
        return (value / 100f).toString()
    }
}

val centsXAxisValueFormatter = object : ValueFormatter() {
    override fun getFormattedValue(value: Float): String {
        val dollar = (value / 100).toInt()
//        return formatValue(dollar.toLong())
        return formatValue(value.toLong())
    }
}

//private val suffixes: NavigableMap<Long, String> = TreeMap<Long, String>().apply {
//    1_000L to "k"
//    1_000_000L to "M"
//    1_000_000_000L to "G"
//    1_000_000_000_000L to "T"
//    1_000_000_000_000_000L to "P"
//    1_000_000_000_000_000_000L to "E"
//}
//
//fun formatValue(value: Long): String {
//    //Long.MIN_VALUE == -Long.MIN_VALUE so we need an adjustment here
//    if (value == Long.MIN_VALUE) return format("${Long.MIN_VALUE + 1}")
//    if (value < 0) return "-" + format(value.toString())
//    if (value < 1000) return value.toString() //deal with easy case
//    val (divideBy, suffix) = suffixes.floorEntry(value) ?: return value.toString()
//    val truncated = value / (divideBy / 10) //the number part of the output times 10
//    val hasDecimal = truncated < 100 && truncated / 10.0 != (truncated / 10).toDouble()
//    return if (hasDecimal) (truncated / 10.0).toString() + suffix else (truncated / 10).toString() + suffix
//}

fun formatValue(number: Long): String {
    return when (number) {
        0L -> {
            "0"
        }

        in 0..99999 -> {
            (number / 100).toString()
        }

        in 100000..99999999 -> {
//            "${(number / 10000.0)}k"
            "${(String.format("%.1f", number / 10000.0))}k"
        }

        else -> {
            "${(number / 100000000.0)}M"
        }
    }

//    val formattedNumber = String.format("%.1f", num.toDouble())
//    return "$formattedNumber${suffixes[index]}"
}


@Composable
fun MotLineChart(
    modifier: Modifier = Modifier,
    selectedCategoryViewState: SelectedCategoryViewState,
    appThemeViewState: AppThemeViewState = AppThemeViewState()
) {

    var initialViewState by remember { mutableStateOf(SelectedCategoryViewState()) }
    val isClearLineChart =
        selectedCategoryViewState.selectedTimeModel.category != initialViewState.selectedTimeModel.category
    initialViewState = selectedCategoryViewState
    val isDarkTheme = motIsDarkTheme(appThemeViewState = appThemeViewState)
    val textColor = if (isDarkTheme) Color.WHITE else Color.BLACK
    val lineColor = MaterialTheme.colorScheme.onBackground
    val axisTextSize = MaterialTheme.typography.bodySmall
    val dataTextSize = MaterialTheme.typography.labelSmall
    val markerBackgroundColor = MaterialTheme.colorScheme.tertiaryContainer
    val markerTextColor = MaterialTheme.colorScheme.onTertiaryContainer

    val entries = selectedCategoryViewState.selectedTimeModel
        .paymentToMonth
        .entries
        .mapIndexed { index, entry ->
            Entry(
                index.toFloat(),
                entry.value.sumOfPaymentsForThisMonth.toFloat(),
                entry.value.payments.first().dateInMills.convertMillisecondsToDate(Constants.MONTH_SHORT_YEAR_DATE_PATTERN) // month year text for marker
            )
        }.toList()

    val set = LineDataSet(
        entries,
        "Total: X"
    ).apply {
        valueFormatter = centsXAxisValueFormatter
        valueTextColor = textColor
        valueTextSize = dataTextSize.fontSize.value
        color = lineColor.toIntRepresentation()
        setCircleColor(lineColor.toIntRepresentation())
        setDrawCircleHole(true)
        circleHoleColor = MaterialTheme.colorScheme.surfaceVariant.toIntRepresentation()
        circleRadius = 4.dp.value
        circleHoleRadius = 2.dp.value
    }
    val dataSetList = listOf(set)
    val lineData = LineData(dataSetList)
    AndroidView(
        modifier = modifier,
        factory = { context ->
            LineChart(context).apply {
                data = lineData
                with(xAxis) {
                    position = XAxis.XAxisPosition.BOTTOM
                    textSize = axisTextSize.fontSize.value
                    granularity = 1f // minimum axis-step (interval) is 1
                    setTextColor(textColor)
                }

                with(axisLeft) {
                    valueFormatter = centsXAxisValueFormatter
                    textSize = axisTextSize.fontSize.value
                    setTextColor(textColor)
                }

                axisRight.isEnabled = false

                description.isEnabled = false
                legend.isEnabled = false
                //set up marker view

                //set up marker view
                val mv = object : MarkerView(context, R.layout.mot_chart_marker) {
                    override fun refreshContent(e: Entry, highlight: Highlight) {
                        findViewById<RelativeLayout>(R.id.mot_chart_marker_container).apply {
                            e.data?.let {
                                val markerBg =
                                    ContextCompat.getDrawable(context, R.drawable.marker_background)
                                        .apply {
                                            this?.setTint(markerBackgroundColor.toIntRepresentation())
                                        }
                                background = markerBg
                            }
                        }
                        findViewById<TextView>(R.id.mot_chart_marker_text).apply {
                            (e.data as? String)?.let { monthYearText ->
                                text = monthYearText
                                setTextColor(markerTextColor.toIntRepresentation())
                                textSize = dataTextSize.fontSize.value
                            }
                        }

                        super.refreshContent(e, highlight)
                    }

                    override fun getOffset(): MPPointF {
                        return MPPointF((-(width / 2)).toFloat(), (-height).toFloat())
                    }

                }

                mv.chartView = this // For bounds control

                marker = mv


            }
        },
        update = {
            if (isClearLineChart) {
                it.clear()
                it.data = lineData
            } else {
                it.data = lineData
                it.invalidate()
            }
        }
    )
}


@MotPreview
@Composable
private fun MotLineChartPreview() {
    MotMaterialTheme {
        MotLineChart(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1.2f, true),
            selectedCategoryViewState = SelectedCategoryViewState(
                selectedTimeModel = PreviewData.statisticByCategoryPerMonthModel,
            ),
        )
    }
}