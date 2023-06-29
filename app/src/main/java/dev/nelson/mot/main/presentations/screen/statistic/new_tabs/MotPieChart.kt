package dev.nelson.mot.main.presentations.screen.statistic.new_tabs

import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
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
import dev.nelson.mot.core.ui.MotMaterialTheme
import dev.nelson.mot.main.presentations.screen.statistic.SelectedTimeViewState
import dev.nelson.mot.main.presentations.screen.statistic.StatisticByCategoryModel
import dev.nelson.mot.main.util.compose.PreviewData
import dev.theme.lightChartColors
import dev.utils.preview.MotPreview
import timber.log.Timber

fun Color.toIntRepresentation(): Int {
    return android.graphics.Color.argb(
        (alpha * 255).toInt(),
        (red * 255).toInt(),
        (green * 255).toInt(),
        (blue * 255).toInt()
    )
}

fun List<Color>.toIntRepresentation(): List<Int> {
    return this.map {
        android.graphics.Color.argb(
            (it.alpha * 255).toInt(),
            (it.red * 255).toInt(),
            (it.green * 255).toInt(),
            (it.blue * 255).toInt()
        )
    }
}

@Composable
fun MotPieChart(
    modifier: Modifier = Modifier,
    selectedTimeViewState: SelectedTimeViewState,
    onPieEntrySelected: (Int) -> Unit, // category id
    onNothingSelected: () -> Unit
) {

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
                        Timber.d("onNothingSelected")
                        // TODO: clear selected category
                    }

                    override fun onValueSelected(e: Entry?, h: Highlight?) {
                        val categoryId = (e as? PieEntry)?.label?.toInt()
                        categoryId?.let {
                            onPieEntrySelected.invoke(it)
                            val selectedCategoryModel =
                                categories.find { categoryModel -> categoryModel.category?.id == it }
                            val formattedCenterText = formatCenterText(selectedCategoryModel)
                            this@apply.centerText = formattedCenterText
                        }
                    }
                })
            }
        },
        update = { chart ->
            chart.data = pieData
            chart.invalidate()
        }
    )
}

private fun formatCenterText(selectedCategoryModel: StatisticByCategoryModel?): String {
    val categoryName = selectedCategoryModel?.category?.name
    return """
        $categoryName
        ${selectedCategoryModel?.sumOfPayments} | ${"%.1f%%".format(selectedCategoryModel?.percentage)}
    """.trimIndent()
}

@MotPreview
@Composable
fun MotPieChartPreview() {
    val modelList = PreviewData.statisticByMonthModelPreviewData
    val slices = modelList.categoriesModelList.map {
        PieChartData.Slice(
            value = it.sumOfPayments.toFloat(),
            color = lightChartColors.random()
        )
    }
    MotMaterialTheme {
        MotPieChart(
            selectedTimeViewState = SelectedTimeViewState(
                selectedTimeModel = modelList,
                selectedTimePieChartData = PieChartData(slices)
            ),
            onPieEntrySelected = { },
            onNothingSelected = {}
        )
    }
}