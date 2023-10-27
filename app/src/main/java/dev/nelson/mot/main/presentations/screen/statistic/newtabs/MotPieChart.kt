package dev.nelson.mot.main.presentations.screen.statistic.newtabs

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.tehras.charts.piechart.PieChartData
import dev.nelson.mot.core.ui.AppTheme
import dev.nelson.mot.core.ui.view_state.PriceViewState
import dev.nelson.mot.main.presentations.screen.statistic.SelectedTimeViewState
import dev.nelson.mot.main.presentations.screen.statistic.StatisticByCategoryModel
import dev.nelson.mot.main.util.StringUtils
import dev.nelson.mot.main.util.compose.PreviewData
import dev.nelson.mot.main.util.toIntRepresentation
import dev.theme.lightChartColors
import dev.utils.formatPrice
import dev.utils.preview.MotPreview

@Composable
fun MotPieChart(
    modifier: Modifier = Modifier,
    selectedTimeViewState: SelectedTimeViewState,
    priceViewState: PriceViewState,
    onPieEntrySelected: (Int) -> Unit, // category id
    onNothingSelected: () -> Unit
) {
    var initialViewState by remember { mutableStateOf(SelectedTimeViewState()) }
    val isClearPieChart =
        selectedTimeViewState.selectedTimeModel.month != initialViewState.selectedTimeModel.month ||
            selectedTimeViewState.selectedTimeModel.year != initialViewState.selectedTimeModel.year
    initialViewState = selectedTimeViewState
    val categories = selectedTimeViewState.selectedTimeModel.categoriesModelList
    val entries = selectedTimeViewState.selectedTimePieChartData.slices.mapIndexed { index, slice ->
        PieEntry(
            slice.value,
            categories[index].category?.id.toString()
        ) // set category id to the lable to be able get it in on pie entry click event
    }
    val pieDataSet = PieDataSet(entries, "").apply {
//        setColors(*ColorTemplate.JOYFUL_COLORS)
//        setColors(
//            Color.Blue.toIntRepresentation(),
//            Color.Red.toIntRepresentation(),
//            Color.Green.toIntRepresentation()
//        )

        colors = lightChartColors.toIntRepresentation()
    }

    val pieData = PieData(pieDataSet).apply {
        setDrawValues(false)
    }

    val centerTextColor = MaterialTheme.colorScheme.onPrimaryContainer

    AndroidView(
        modifier = modifier,
        factory = { context ->
            PieChart(context).apply {
                isRotationEnabled = false

                // whole
                isDrawHoleEnabled = true
                setHoleColor(Color.Transparent.toIntRepresentation())
                holeRadius = 64.dp.value
                transparentCircleRadius = 0.dp.value
                setDrawSlicesUnderHole(false)
//                setDrawCenterText(false)
                // whole end

                // slice
                setUsePercentValues(false)
                setDrawEntryLabels(false)
                setDrawMarkers(false)
                // slice end

                description.isEnabled = false
                legend.isEnabled = false

                data = pieData
                setOnChartValueSelectedListener(object :
                        OnChartValueSelectedListener {
                        override fun onNothingSelected() {
                            centerText = StringUtils.EMPTY
                        }

                        override fun onValueSelected(e: Entry?, h: Highlight?) {
                            val categoryId = (e as? PieEntry)?.label?.toIntOrNull()
                            categoryId?.let {
                                onPieEntrySelected.invoke(it)
                                initialViewState.selectedTimeModel
                                    .categoriesModelList
                                    .find { categoryModel -> categoryModel.category?.id == it }
                                    ?.let { selectedCategoryModel ->
                                        val formattedCenterText =
                                            formatCenterText(selectedCategoryModel, priceViewState)
                                        setCenterTextColor(centerTextColor.toIntRepresentation())
                                        this@apply.centerText = formattedCenterText
                                    }
                            }
                        }
                    })
            }
        },
        update = { chart ->
            with(chart) {
                if (isClearPieChart) {
                    clear()
                    centerText = StringUtils.EMPTY
                    data = pieData
                } else {
                    data = pieData
                    invalidate()
                }
            }
        }
    )
}

private fun formatCenterText(
    selectedCategoryModel: StatisticByCategoryModel,
    priceViewState: PriceViewState
): String {
    val formattedPrice = formatPrice(selectedCategoryModel.sumOfPayments, priceViewState)
    val categoryName = selectedCategoryModel.category?.name
    return """
        $categoryName
        $formattedPrice | ${"%.1f%%".format(selectedCategoryModel.percentage)}
    """.trimIndent()
}

@MotPreview
@Composable
private fun MotPieChartPreview() {
    val modelList = PreviewData.statisticByMonthModelPreviewData
    val slices = modelList.categoriesModelList.map {
        PieChartData.Slice(
            value = it.sumOfPayments.toFloat(),
            color = lightChartColors.random()
        )
    }
    AppTheme {
        Card {
            MotPieChart(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f),
                selectedTimeViewState = SelectedTimeViewState(
                    selectedTimeModel = modelList,
                    selectedTimePieChartData = PieChartData(slices)
                ),
                priceViewState = PriceViewState(),
                onPieEntrySelected = { },
                onNothingSelected = {}
            )
        }
    }
}
