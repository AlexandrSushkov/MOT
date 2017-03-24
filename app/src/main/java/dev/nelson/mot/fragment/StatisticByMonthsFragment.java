package dev.nelson.mot.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import dev.nelson.mot.R;
import dev.nelson.mot.callback.StatisticByMonthsCallback;
import dev.nelson.mot.loadercalback.StatisticByMonthsLoaderCallbacks;
import dev.nelson.mot.utils.StringUtils;
import dev.nelson.mot.utils.valueformatter.SideYAxisValueFormatter;
import dev.nelson.mot.utils.valueformatter.LineDataXAxisValueFormatter;
import dev.nelson.mot.utils.valueformatter.YAxisValueFormatter;

public class StatisticByMonthsFragment extends Fragment implements StatisticByMonthsCallback, OnChartValueSelectedListener {

    public static final String FRAGMENT_TAG = StatisticByMonthsFragment.class.getName();

    @BindView(R.id.line_chart)
    LineChart mChart;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_statistic_by_months, container, false);
        ButterKnife.bind(this, view);
        StatisticByMonthsLoaderCallbacks loadersCallback = new StatisticByMonthsLoaderCallbacks(getContext(), this);
        getActivity().getSupportLoaderManager().restartLoader(StatisticByMonthsLoaderCallbacks.LOADER_ID, null, loadersCallback);
        return view;
    }

    @Override
    public void getDataFromStatisticByMonthsLoaderCallbacks(LineData data) {
        //init line chart
        initChart(data);
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

    private void initChart(LineData data) {
        mChart.setOnChartValueSelectedListener(this);
        //disable description
        mChart.getDescription().setEnabled(false);
        //set up gestures
        mChart.setScaleXEnabled(true);
        mChart.setDragEnabled(false);
        mChart.setScaleEnabled(false);
        mChart.setPinchZoom(false);

        //set xAxis
        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setDrawGridLines(false);
        xAxis.setLabelCount(3);
        xAxis.setValueFormatter(new LineDataXAxisValueFormatter(data.getDataSetByIndex(0)));

        //set leftyAxis
        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setValueFormatter(new SideYAxisValueFormatter());
        leftAxis.setLabelCount(5, false);
        leftAxis.setSpaceTop(15f);

        //set rightAxis
        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setValueFormatter(new SideYAxisValueFormatter());
        rightAxis.setLabelCount(5, false);
        rightAxis.setSpaceTop(15f);

        data.setValueTextColor(Color.BLACK);
        // set data
        LineDataSet set = (LineDataSet) data.getDataSets().get(0);
        // yAxis value formatter
        set.setColor(R.color.colorPrimary);
        set.setCircleColor(R.color.colorPrimary);
        set.setValueFormatter(new YAxisValueFormatter());
        set.setValueTextSize(11f);
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set);

        mChart.animateX(800);

        mChart.setData(data);

    }
}
