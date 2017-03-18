package dev.nelson.mot.fragment;

import android.annotation.SuppressLint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import dev.nelson.mot.R;
import dev.nelson.mot.callback.SetDataFromStatisticYearLoaderCallbacks;
import dev.nelson.mot.loadercalback.StatisticYearLoaderCallbacks;
import dev.nelson.mot.utils.MyValueFormatted;
import dev.nelson.mot.utils.MyXAxisValueFormatter;
import dev.nelson.mot.utils.MyYAxisValueFormatter;
import dev.nelson.mot.utils.StringUtils;

public class StatisticYearFragment extends Fragment implements SetDataFromStatisticYearLoaderCallbacks,
        OnChartValueSelectedListener{

    public static final String FRAGMENT_TAG = StatisticYearFragment.class.getName();

//    @BindView(R.id.fragment_statistic_year_list_view)
//    ListView mListView;

    @BindView(R.id.statistic_bar_chart)
    BarChart mChart;

    private ArrayList<String> mCategories = new ArrayList<>();
    private RectF mOnValueSelectedRectF = new RectF();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_satistic_year, container, false);
        ButterKnife.bind(this, view);
        StatisticYearLoaderCallbacks loaderCallbacks = new StatisticYearLoaderCallbacks(getContext(), this);
        getActivity().getSupportLoaderManager().initLoader(StatisticYearLoaderCallbacks.LOADER_ID, null, loaderCallbacks);
        if (mCategories.size() == 0){
            getActivity().getSupportLoaderManager().restartLoader(StatisticYearLoaderCallbacks.LOADER_ID, null, loaderCallbacks);
        }
        return view;
    }

    private void initBarChart(ArrayList<String> categoriesNames, ArrayList<BarEntry> enries) {
        mChart.setDrawBarShadow(false);
        mChart.setDrawValueAboveBar(true);

//          implment interface firstly
        mChart.setOnChartValueSelectedListener(this);

//          turn off discription
//        mChart.getDescription().setText("discrtipotion");

        // scaling can now only be done on x- and y-axis separately
        mChart.setPinchZoom(false);
        mChart.setDoubleTapToZoomEnabled(false);
        mChart.setScaleYEnabled(false);

        mChart.setDrawGridBackground(false);

        mChart.getDescription().setEnabled(false);

        // scaling can now only be done on x- and y-axis separately
        mChart.setPinchZoom(false);

        mChart.setDrawGridBackground(false);
        // mChart.setDrawYLabels(false);

        ArrayList<String> categories = categoriesNames;

        //set x axis
        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new MyXAxisValueFormatter(categories));
        xAxis.setGranularity(1f);
        xAxis.setDrawGridLines(false);
        xAxis.setLabelCount(4);

        // set left y axis
        YAxis leftAxis = mChart.getAxisLeft();
//        leftAxis.setDrawGridLines(false);
        leftAxis.setValueFormatter(new MyYAxisValueFormatter(MyYAxisValueFormatter.LEFT));

        // set right y axis
        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setDrawGridLines(false);
        rightAxis.setValueFormatter(new MyYAxisValueFormatter(MyYAxisValueFormatter.RIGHT));

        //set legend


        setData(enries);
        mChart.animateY(800);
    }

    private void setData(ArrayList<BarEntry> enries) {
        ArrayList<BarEntry> entries = enries;
//                new ArrayList<>();
//        entries.add(new BarEntry(1f, 10));
//        entries.add(new BarEntry(2f, 20));
//        entries.add(new BarEntry(3f, 30));
//        entries.add(new BarEntry(4f, 32));
//        entries.add(new BarEntry(5f, 25));
//        entries.add(new BarEntry(6f, 15));

        Typeface mTfLight = Typeface.createFromAsset(getContext().getAssets(), "OpenSans-Light.ttf");


        BarDataSet set = new BarDataSet(entries, "March 2017");

        //set colors
        set.setColors(ColorTemplate.MATERIAL_COLORS);
        set.setValueFormatter(new MyValueFormatted());

        ArrayList<IBarDataSet> dataSets = new ArrayList<>();
        dataSets.add(set);

        BarData data = new BarData(dataSets);
//        data.setValueTypeface(mTfLight);
        data.setValueTextSize(11f);


        mChart.setData(data);
    }


    @Override
    public void setDataFromStatisticLoaderCallbacks(ArrayList<String> categoriesNames, ArrayList<BarEntry> enries) {
        mCategories = categoriesNames;
        initBarChart(categoriesNames, enries);

    }

    @SuppressLint("NewApi")
    @Override
    public void onValueSelected(Entry e, Highlight h) {

        if (e == null)
            return;

        RectF bounds = mOnValueSelectedRectF;
        mChart.getBarBounds((BarEntry) e, bounds);
        MPPointF position = mChart.getPosition(e, YAxis.AxisDependency.LEFT);

        Log.i("bounds", bounds.toString());
        Log.i("position", position.toString());

        Log.i("x-index",
                "low: " + mChart.getLowestVisibleX() + ", high: "
                        + mChart.getHighestVisibleX());

        MPPointF.recycleInstance(position);

        Log.i("VAL SELECTED",
                "Value: " + e.getY() + ", index: " + h.getX()
                        + ", DataSet index: " + h.getDataSetIndex());

        String formattedCost = String.valueOf(StringUtils.formattedCost((long)e.getY()));
        int categoryIndex = (int) h.getX() -1 ;
        Toast.makeText(getActivity(), mCategories.get(categoryIndex) + ": " + formattedCost, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected() {

    }
}
