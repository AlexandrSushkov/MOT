package dev.nelson.mot.legacy.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import dev.nelson.mot.R;
import dev.nelson.mot.legacy.callback.EmptyCursorCallback;
import dev.nelson.mot.legacy.callback.StatisticByMonthsCallback;
import dev.nelson.mot.legacy.loadercallback.StatisticByMonthsLoaderCallbacks;
import dev.nelson.mot.legacy.utils.marker.CustomMarker;
import dev.nelson.mot.legacy.utils.valueformatter.LineDataXAxisValueFormatter;
import dev.nelson.mot.legacy.utils.valueformatter.SideYAxisValueFormatter;
import dev.nelson.mot.legacy.utils.valueformatter.YAxisValueFormatter;

public class StatisticByMonthsFragment extends Fragment implements StatisticByMonthsCallback,
        EmptyCursorCallback {

    public static final String FRAGMENT_TAG = StatisticByMonthsFragment.class.getName();

    LineChart mChart;
    TextView mNoDataAnnouncement;
    TextView mTitle;
    TextView mTotalCost;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_statistic_by_months, container, false);
        mChart = view.findViewById(R.id.line_chart);
        mNoDataAnnouncement = view.findViewById(R.id.no_data_announcement);
        mTitle = view.findViewById(R.id.statistic_by_months_title);
        mTotalCost = view.findViewById(R.id.statistic_by_months_total_cost);
        StatisticByMonthsLoaderCallbacks loadersCallback = new StatisticByMonthsLoaderCallbacks(getContext(), this, this);
        getActivity().getSupportLoaderManager().restartLoader(StatisticByMonthsLoaderCallbacks.LOADER_ID, null, loadersCallback);
        return view;
    }

    @Override
    public void getDataFromStatisticByMonthsLoaderCallbacks(LineData data) {
        //init line chart
        initChart(data);
    }

    @Override
    public void showNoDataAnnouncement() {
        mNoDataAnnouncement.setVisibility(View.VISIBLE);
        mChart.setVisibility(View.GONE);
    }

    private void initChart(LineData data) {
        assert data != null;
        ILineDataSet iLineDataSet = data.getDataSetByIndex(0);

        String firstMonth = (String) iLineDataSet.getEntryForIndex(0).getData();
        String lastMonth = (String) iLineDataSet.getEntryForIndex(iLineDataSet.getEntryCount() - 1).getData();
        mTitle.setText(firstMonth + " - " + lastMonth);
        mTotalCost.setText(iLineDataSet.getLabel());

        //set up legend
        mChart.getLegend().setEnabled(false);

        //set up description
        mChart.getDescription().setEnabled(false);

        //set up gestures
        mChart.setScaleXEnabled(true);
        mChart.setScaleYEnabled(false);
        mChart.setPinchZoom(false);
        mChart.setDoubleTapToZoomEnabled(false);

        //set xAxis
        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setDrawGridLines(false);
        xAxis.setLabelCount(3);
        xAxis.setValueFormatter(new LineDataXAxisValueFormatter(iLineDataSet)); //xAxis value formatter

        //set leftyAxis
        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setValueFormatter(new SideYAxisValueFormatter());// side yAxis value formatter
        leftAxis.setLabelCount(5, false);
        leftAxis.setSpaceTop(15f);

        //set rightAxis
        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setValueFormatter(new SideYAxisValueFormatter());// side yAxis value formatter
        rightAxis.setLabelCount(5, false);
        rightAxis.setSpaceTop(15f);

        //set data
        LineDataSet set = (LineDataSet) iLineDataSet;
        set.setColor(R.color.colorPrimary);
        set.setCircleColor(R.color.colorPrimary);
        set.setValueTextColor(Color.BLACK);
        set.setValueFormatter(new YAxisValueFormatter()); // yAxis value formatter
        set.setValueTextSize(11f);

        //set up marker view
        CustomMarker mv = new CustomMarker(getContext());
        mv.setChartView(mChart); // For bounds control
        mChart.setMarker(mv); // Set the marker to the chart

        mChart.setData(data);
        mChart.invalidate();
        mChart.animateX(1400);
    }
}
