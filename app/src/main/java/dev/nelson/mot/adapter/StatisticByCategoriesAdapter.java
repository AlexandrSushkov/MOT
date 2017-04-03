package dev.nelson.mot.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.List;

import dev.nelson.mot.R;
import dev.nelson.mot.utils.StringUtils;
import dev.nelson.mot.utils.valueformatter.LineDataXAxisValueFormatter;
import dev.nelson.mot.utils.valueformatter.SideYAxisValueFormatter;
import dev.nelson.mot.utils.valueformatter.YAxisValueFormatter;


public class StatisticByCategoriesAdapter extends ArrayAdapter<LineData> implements OnChartValueSelectedListener {

    private ViewHolder holder = null;

    public StatisticByCategoriesAdapter(Context context, List<LineData> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LineData data = getItem(position);
        assert data != null;
        ILineDataSet dataSet = data.getDataSetByIndex(0);

        if (convertView == null) {

            holder = new ViewHolder();

            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_linechart, null);
            holder.chart = (LineChart) convertView.findViewById(R.id.item_line_chart);
            holder.title = (TextView) convertView.findViewById(R.id.item_chart_title);
            holder.totalCost = (TextView) convertView.findViewById(R.id.item_chart_total_cost);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.title.setText(dataSet.getLabel());
        String total = getContext().getString(R.string.total) + getTotalCost(dataSet);
        holder.totalCost.setText(total);

        // apply styling
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
        xAxis.setValueFormatter(new LineDataXAxisValueFormatter(dataSet));

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
        data.setValueTextColor(Color.BLACK);
        LineDataSet set = (LineDataSet) dataSet;
        set.setColors(R.color.colorPrimary);
        set.setCircleColor(R.color.colorPrimary);
        // yAxis value formatter
        set.setValueFormatter(new YAxisValueFormatter());
        set.setValueTextSize(11f);

        holder.chart.setData(data);
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

    private String getTotalCost(ILineDataSet dataSet){
        long totalCost = 0;
        for (int j = 0; j < dataSet.getEntryCount(); j++) {
            totalCost += dataSet.getEntryForIndex(j).getY();
        }
        return StringUtils.formattedCost(totalCost);
    }

    private class ViewHolder {
        LineChart chart;
        TextView title;
        TextView totalCost;
    }
}
