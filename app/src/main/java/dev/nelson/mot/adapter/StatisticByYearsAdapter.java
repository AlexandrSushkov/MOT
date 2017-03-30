package dev.nelson.mot.adapter;


import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

import dev.nelson.mot.R;
import dev.nelson.mot.utils.StringUtils;
import dev.nelson.mot.utils.valueformatter.SideYAxisValueFormatter;
import dev.nelson.mot.utils.valueformatter.BarDataXAxisValueFormatter;
import dev.nelson.mot.utils.valueformatter.YAxisValueFormatter;

public class StatisticByYearsAdapter extends ArrayAdapter<BarData> implements OnChartValueSelectedListener {


    private ViewHolder holder = null;
    private TextView mTitle;
    private TextView mTotalCost;

    public StatisticByYearsAdapter(Context context, List<BarData> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        BarData data = getItem(position);

        if (convertView == null) {

            holder = new ViewHolder();

            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_barchart, null);
            holder.chart = (BarChart) convertView.findViewById(R.id.item_bar_chart);
            mTitle = (TextView) convertView.findViewById(R.id.item_chart_title);
            mTotalCost = (TextView) convertView.findViewById(R.id.item_chart_total_cost);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        mTitle.setText(data.getDataSetByIndex(0).getLabel());
        String total = getContext().getString(R.string.total) + getTotalCost(data);
        mTotalCost.setText(total);

        // apply styling
        holder.chart.setDrawBarShadow(false);
        holder.chart.setDrawValueAboveBar(true);
        //place for on click listener
        holder.chart.setOnChartValueSelectedListener(this);
        //disable some gestures
        holder.chart.setPinchZoom(false);
        holder.chart.setDoubleTapToZoomEnabled(false);
        holder.chart.setScaleYEnabled(false);

        holder.chart.setDrawGridBackground(false);
        holder.chart.getDescription().setEnabled(false);

        //disable legend
        holder.chart.getLegend().setEnabled(false);

        //set xAxis
        XAxis xAxis = holder.chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setDrawGridLines(false);
        xAxis.setLabelCount(3);
        xAxis.setTextSize(9f);
//        this realization of value formatter cause java.lang.IndexOutOfBoundsException: Invalid index 4, size is 2
        xAxis.setValueFormatter(new BarDataXAxisValueFormatter(data.getDataSetByIndex(0)));

        //set leftyAxis
        YAxis leftAxis = holder.chart.getAxisLeft();
//        leftAxis.setTypeface(mTfLight);
        leftAxis.setValueFormatter(new SideYAxisValueFormatter());
        leftAxis.setLabelCount(5, false);
        leftAxis.setSpaceTop(15f);

        //set rightAxis
        YAxis rightAxis = holder.chart.getAxisRight();
//        rightAxis.setTypeface(mTfLight);
        rightAxis.setValueFormatter(new SideYAxisValueFormatter());
        rightAxis.setLabelCount(5, false);
        rightAxis.setSpaceTop(15f);

        data.setValueTextColor(Color.BLACK);
        // set data
        BarDataSet set = (BarDataSet) data.getDataSets().get(0);
        set.setColors(ColorTemplate.MATERIAL_COLORS);
        // yAxis value formatter
        set.setValueFormatter(new YAxisValueFormatter());
        set.setValueTextSize(11f);
        ArrayList<IBarDataSet> dataSets = new ArrayList<>();
        dataSets.add(set);

        holder.chart.setData(data);
        holder.chart.setFitBars(true);

        // do not forget to refresh the chart
        holder.chart.invalidate();
        holder.chart.animateY(1400);

        return convertView;
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {
        if (e == null)
            return;
        Toast.makeText(getContext(), e.getData() + ": " + String.valueOf(StringUtils.formattedCost((long) e.getY())), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected() {

    }

    private String getTotalCost(BarData data){
        long totalCost = 0;
        for (int j = 0; j < data.getEntryCount(); j++) {
            totalCost += data.getDataSetByIndex(0).getEntryForIndex(j).getY();
        }
        return StringUtils.formattedCost(totalCost);
    }


    private class ViewHolder {

        BarChart chart;
    }
}
