package dev.nelson.mot.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import butterknife.BindView;
import butterknife.ButterKnife;
import dev.nelson.mot.R;
import dev.nelson.mot.callback.EmptyCursorCallback;
import dev.nelson.mot.callback.StatisticCurrentMonthCallback;
import dev.nelson.mot.loadercallback.StatisticCurrentMonthLoaderCallbacks;
import dev.nelson.mot.utils.StringUtils;

public class StatisticCurrentMonthFragment extends Fragment implements StatisticCurrentMonthCallback,
        OnChartValueSelectedListener,
        EmptyCursorCallback{

    public static final String FRAGMENT_TAG = StatisticCurrentMonthFragment.class.getName();

    @BindView(R.id.pie_chart)
    PieChart mChart;
    @BindView(R.id.no_data_announcement)
    TextView mNoDataAnnouncement;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_statistic_month, container, false);
        ButterKnife.bind(this, view);
        StatisticCurrentMonthLoaderCallbacks mLoaderCallbacks = new StatisticCurrentMonthLoaderCallbacks(getContext(), this, this);
        getActivity().getSupportLoaderManager().restartLoader(StatisticCurrentMonthLoaderCallbacks.LOADER_ID, null, mLoaderCallbacks);
        return view;
    }

    @Override
    public void setDataFromStatisticCurrentMonthLoaderCallbacks(PieData data) {
        initPieChart(data);
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {
        if (e == null) {
            return;
        }
//        Log.i("VAL SELECTED", "Value: " + e.getY() + ", index: " + h.getX() + ", DataSet index: " + h.getDataSetIndex());
        Toast.makeText(getContext(), ((PieEntry) e).getLabel() + ": " + String.valueOf(StringUtils.formattedCost((long) e.getY())), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected() {
        Log.i("PieChart", "nothing selected");
    }

    @Override
    public void showNoDataAnnouncement() {
        mNoDataAnnouncement.setVisibility(View.VISIBLE);
        mChart.setVisibility(View.GONE);
    }

    private void initPieChart(PieData pieData) {
        String totalCost = pieData.getDataSet().getLabel();

        //setup inner circle
        //main circle
        mChart.setDrawHoleEnabled(true);
        mChart.setHoleRadius(58f);
        mChart.setHoleColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
        //transparent circle
        mChart.setTransparentCircleRadius(61f);
        mChart.setTransparentCircleColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
        mChart.setTransparentCircleAlpha(110);

        //inner circle text
        mChart.setDrawCenterText(true);
        mChart.setCenterText(generateCenterCircleText(totalCost));
        mChart.setCenterTextColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
        mChart.setCenterTextSize(20);

        //categories text color
        mChart.setEntryLabelColor(Color.BLACK);

        // add a selection listener
        mChart.setOnChartValueSelectedListener(this);

        // enable rotation of the chart by touch
        mChart.setRotationAngle(0);
        mChart.setRotationEnabled(true);
        mChart.setHighlightPerTapEnabled(true);

        //legend turned off
        mChart.getLegend().setEnabled(false);

        //set up dataSet
        PieDataSet dataSet = (PieDataSet) pieData.getDataSet();
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        dataSet.setValueLinePart1OffsetPercentage(80.f);
        dataSet.setValueLinePart1Length(0.2f);
        dataSet.setValueLinePart2Length(0.4f);
        // position of category names and percentage value
        dataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
//        dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        dataSet.setValueTextColor(Color.BLACK);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.BLACK);

        mChart.setUsePercentValues(true);
        mChart.setDragDecelerationFrictionCoef(0.95f);
        mChart.setExtraOffsets(20.f, 0.f, 20.f, 0.f);
//        mChart.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
        mChart.getDescription().setEnabled(false);
        // undo all highlights
        mChart.highlightValues(null);

        mChart.setData(data);
        mChart.animateXY(1400, 1400);
        mChart.invalidate();
    }

    private SpannableString generateCenterCircleText(String totalCost) {
        SpannableString s = new SpannableString(getString(R.string.pie_chart_center_text) + totalCost);
        s.setSpan(new RelativeSizeSpan(0.6f), 0, getString(R.string.pie_chart_center_text).length(), 0);
        return s;
    }
}
