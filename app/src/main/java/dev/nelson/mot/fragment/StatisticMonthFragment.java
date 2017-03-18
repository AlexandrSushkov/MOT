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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import dev.nelson.mot.R;
import dev.nelson.mot.callback.SetDataFromStatisticLoader;
import dev.nelson.mot.loadercalback.StatisticLoaderCallbacks;
import dev.nelson.mot.utils.StringUtils;

public class StatisticMonthFragment extends Fragment implements SetDataFromStatisticLoader, OnChartValueSelectedListener {

    public static final String FRAGMENT_TAG = StatisticMonthFragment.class.getName();

    @BindView(R.id.pie_chart)
    PieChart mChart;
    //xData
    private ArrayList<String> mCategoriesNames = new ArrayList<>();
    //yData
    private ArrayList<Long> mSumPerCategory = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_statistic_month, container, false);
        ButterKnife.bind(this, view);
        StatisticLoaderCallbacks loaderCallbacks = new StatisticLoaderCallbacks(getContext(), this);
        getActivity().getSupportLoaderManager().initLoader(StatisticLoaderCallbacks.LOADER_ID, null, loaderCallbacks);
        // i don't like this, it looks like crunch, but i don't know how to fix this in other way.
        // without this check after changing fragment xData and yData arrays are empty. and loader doesn't load data automatically
        if (mSumPerCategory.size() == 0 && mCategoriesNames.size() == 0){
            getActivity().getSupportLoaderManager().restartLoader(StatisticLoaderCallbacks.LOADER_ID, null, loaderCallbacks);
        }
        return view;
    }

    @Override
    public void setDataFromStatisticLoader(ArrayList<String> categoriesNames, ArrayList<Long> sumPerCategory) {
        mCategoriesNames = categoriesNames;
        mSumPerCategory = sumPerCategory;
        initPieChart();
        mChart.setCenterText(generateCenterCircleText());
        setData();
    }

    // OnChartValueSelectedListener methods
    @Override
    public void onValueSelected(Entry e, Highlight h) {
        if (e == null){
            return;
        }
//        Log.i("VAL SELECTED",
//                "Value: " + e.getY() + ", index: " + h.getX()
//                        + ", DataSet index: " + h.getDataSetIndex());
        String formattedCost = String.valueOf(StringUtils.formattedCost((long)e.getY()));
        int categoryIndex = (int) h.getX();
        Toast.makeText(getActivity(), mCategoriesNames.get(categoryIndex) + ": " + formattedCost, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected() {
        Log.i("PieChart", "nothing selected");
    }

    private void initPieChart(){
        mChart.setUsePercentValues(true);
        mChart.setDragDecelerationFrictionCoef(0.95f);
        mChart.setExtraOffsets(20.f, 0.f, 20.f, 0.f);
//        mChart.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
        mChart.getDescription().setEnabled(false);

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
//        mChart.setCenterText(generateCenterCircleText());
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
    }

    private void setData(){
        ArrayList<PieEntry> entries = new ArrayList<>();

        for (int i = 0; i < mSumPerCategory.size(); i++) {
            //convert long sum into float
            long d = mSumPerCategory.get(i);
            float f = (float)d;
            entries.add(new PieEntry(f, mCategoriesNames.get(i)));
        }

        PieDataSet dataSet = new PieDataSet(entries, "My Data set string label");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);

        //legend turned off
        mChart.getLegend().setEnabled(false);

        // add a lot of colors
//        ArrayList<Integer> colors = new ArrayList<Integer>();
//
//        for (int c : ColorTemplate.VORDIPLOM_COLORS)
//            colors.add(c);
//
//        for (int c : ColorTemplate.JOYFUL_COLORS)
//            colors.add(c);
//
//        for (int c : ColorTemplate.COLORFUL_COLORS)
//            colors.add(c);
//
//        for (int c : ColorTemplate.LIBERTY_COLORS)
//            colors.add(c);
//
//        for (int c : ColorTemplate.PASTEL_COLORS)
//            colors.add(c);
//
//        colors.add(ColorTemplate.getHoloBlue());
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
        mChart.setData(data);

        // undo all highlights
        mChart.highlightValues(null);

        mChart.animateXY(1400, 1400);
        mChart.invalidate();
    }

    private SpannableString generateCenterCircleText(){
        long sumPerMonth = 0;
        SpannableString s;
        for (Long sumOfCategory: mSumPerCategory) {
            sumPerMonth += sumOfCategory;
        }
        if(String.valueOf(sumPerMonth).length() < 13){
            s = new SpannableString(getString(R.string.pie_chart_center_text) + StringUtils.formattedCost(sumPerMonth));
        }else {
            s = new SpannableString(getString(R.string.pie_chart_center_text) + getString(R.string.too_much));
        }
        s.setSpan(new RelativeSizeSpan(0.6f), 0, 20, 0);
        return s;
    }
}
