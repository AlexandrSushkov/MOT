package dev.nelson.mot.main.presentations.screen.statistic.newtabs

import android.widget.RelativeLayout
import android.widget.TextView
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import dev.nelson.mot.core.ui.AppTheme
import dev.nelson.mot.core.ui.fundation.motIsDarkTheme
import dev.nelson.mot.core.ui.view_state.AppThemeViewState
import dev.nelson.mot.main.presentations.screen.statistic.SelectedCategoryViewState
import dev.nelson.mot.main.util.compose.PreviewData
import dev.nelson.mot.main.util.constant.Constants
import dev.nelson.mot.main.util.extention.formatMillsToDateText
import dev.nelson.mot.main.util.toIntRepresentation
import dev.utils.preview.MotPreview

val centsValueFormatter = object : ValueFormatter() {
    override fun getFormattedValue(value: Float): String {
        return (value / 100f).toString()
    }
}

val centsYAxisValueFormatter = object : ValueFormatter() {
    override fun getFormattedValue(value: Float): String {
        val dollar = (value / 100).toLong()
        return formatValue(dollar)
    }
}

val centsEntryValueFormatter = object : ValueFormatter() {
    override fun getFormattedValue(value: Float): String {
        return formatPriceShort(value.toLong())
    }
}
fun formatValue(value: Long): String {
    return if (value < 1000) {
        value.toString()
    } else if (value < 1000000) {
        val newValue = value / 1000.0
        String.format("%.0fk", newValue)
    } else {
        val newValue = value / 1000000.0
        String.format("%.0fM", newValue)
    }
}

fun formatK(string: String): String {
//    val formatter = NumberFormat.getInstance(Locale.US) as DecimalFormat
//    formatter.applyPattern("#.#")
//    return formatter.format(number)
    if (string.length < 2) {
        return string // Return the original string if it has less than 2 characters
    }

    val penultimateIndex = string.length - 1
    val builder = StringBuilder(string)
    builder.insert(penultimateIndex, ".")

    return builder.toString()
}

/**
 * @param number price in cents
 */
fun formatPriceShort(number: Long): String {
    return when (number) {
        0L -> "0"
        // in 1.00 - 999.99
        in 1..99999 -> (number / 100).toString() // 999.99 -> 999
        // in 1,000.00 - 999,999.99
        in 100000..99999999 -> "${formatK((number / 10000).toString())}k" // 1k - 9.9k
        // in 10,000.00 - 99,999.99
//        in 1000000..9999999 -> "${formatK((number / 10000).toString())}k" // 10k - 99.9k
        // in 100,000.00 - 999,999.99
//        in 10000000..99999999 -> "${formatK((number / 10000).toString())}k" // 1k - 999.9k
        // in 1,000,000.00 - 9,999,999.99
        else -> "${number / 100000000}M" // 1M - 999M
    }
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
    val textColor = MaterialTheme.colorScheme.onPrimaryContainer
    val lineColor = MaterialTheme.colorScheme.onPrimaryContainer
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
                entry.value.payments.first().dateInMills.formatMillsToDateText(Constants.MONTH_SHORT_YEAR_DATE_PATTERN) // month year text for marker
            )
        }.toList()

    val set = LineDataSet(
        entries,
        "Total: X"
    ).apply {
        valueFormatter = centsEntryValueFormatter
        valueTextColor = textColor.toIntRepresentation()
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
                xAxis.isEnabled = false
                with(xAxis) {
                    position = XAxis.XAxisPosition.BOTTOM
                    textSize = axisTextSize.fontSize.value
                    granularity = 1f // minimum axis-step (interval) is 1
                    setTextColor(textColor.toIntRepresentation())
                }

                with(axisLeft) {
                    valueFormatter = centsYAxisValueFormatter
                    textSize = axisTextSize.fontSize.value
                    setTextColor(textColor.toIntRepresentation())
                }

                axisRight.isEnabled = false

                description.isEnabled = false
                legend.isEnabled = false
                // set up marker view

                // set up marker view
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
    AppTheme {
        Card {
            MotLineChart(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1.2f, true),
                selectedCategoryViewState = SelectedCategoryViewState(
                    selectedTimeModel = PreviewData.statisticByCategoryPerMonthModel
                )
            )
        }
    }
}
