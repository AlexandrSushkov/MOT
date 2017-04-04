package dev.nelson.mot.adapter;


import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.List;

import dev.nelson.mot.R;
import dev.nelson.mot.utils.StringUtils;
import dev.nelson.mot.utils.marker.CustomMarker;
import dev.nelson.mot.utils.valueformatter.BarDataXAxisValueFormatter;
import dev.nelson.mot.utils.valueformatter.SideYAxisValueFormatter;
import dev.nelson.mot.utils.valueformatter.YAxisValueFormatter;

public class StatisticByYearsAdapter extends ArrayAdapter<BarData> {

    private ViewHolder holder = null;

    public StatisticByYearsAdapter(Context context, List<BarData> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BarData data = getItem(position);
        assert data != null;
        IBarDataSet dataSet = data.getDataSetByIndex(0);

        if (convertView == null) {
            holder = new ViewHolder();

            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_barchart, null);
            holder.chart = (BarChart) convertView.findViewById(R.id.item_bar_chart);
            holder.title = (TextView) convertView.findViewById(R.id.item_chart_title);
            holder.totalCost = (TextView) convertView.findViewById(R.id.item_chart_total_cost);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.title.setText(dataSet.getLabel());
        String total = getContext().getString(R.string.total) + getTotalCost(dataSet);
        holder.totalCost.setText(total);

        //set up legend
        holder.chart.getLegend().setEnabled(false);

        //set up description
        holder.chart.getDescription().setEnabled(false);

        //set up gestures
        holder.chart.setScaleXEnabled(true);
        holder.chart.setPinchZoom(false);
        holder.chart.setDoubleTapToZoomEnabled(false);
        holder.chart.setScaleYEnabled(false);

        //set xAxis
        XAxis xAxis = holder.chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setDrawGridLines(false);
        xAxis.setLabelCount(3);
        xAxis.setTextSize(9f);
        xAxis.setValueFormatter(new BarDataXAxisValueFormatter(data.getDataSetByIndex(0)));

        //set leftyAxis
        YAxis leftAxis = holder.chart.getAxisLeft();
        leftAxis.setValueFormatter(new SideYAxisValueFormatter());
        leftAxis.setLabelCount(5, false);
        leftAxis.setSpaceTop(15f);

        //set rightAxis
        YAxis rightAxis = holder.chart.getAxisRight();
        rightAxis.setValueFormatter(new SideYAxisValueFormatter());
        rightAxis.setLabelCount(5, false);
        rightAxis.setSpaceTop(15f);

        // set data
        BarDataSet set = (BarDataSet) dataSet;
        set.setColors(ColorTemplate.MATERIAL_COLORS);
        set.setValueTextColor(Color.BLACK);
        set.setValueFormatter(new YAxisValueFormatter());// yAxis value formatter
        set.setValueTextSize(11f);

        //set up marker view
        CustomMarker mv = new CustomMarker(getContext());
        mv.setChartView(holder.chart); // For bounds control
        holder.chart.setMarker(mv); // Set the marker to the chart

        holder.chart.setDrawBarShadow(false);
        holder.chart.setDrawValueAboveBar(true);
        holder.chart.setDrawGridBackground(false);
        holder.chart.setFitBars(true);
        holder.chart.setData(data);
        holder.chart.invalidate();
        holder.chart.animateY(1400);
        return convertView;
    }

    private String getTotalCost(IBarDataSet dataSet){
        long totalCost = 0;
        for (int j = 0; j < dataSet.getEntryCount(); j++) {
            totalCost += dataSet.getEntryForIndex(j).getY();
        }
        return StringUtils.formattedCost(totalCost);
    }

    private class ViewHolder {
        BarChart chart;
        TextView title;
        TextView totalCost;
    }
}
