package dev.nelson.mot.fragment;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import java.util.LinkedList;

import butterknife.BindView;
import butterknife.ButterKnife;
import dev.nelson.mot.R;
import dev.nelson.mot.callback.SetDataFromStatisticLoader;
import dev.nelson.mot.db.SQLiteOpenHelperImpl;
import dev.nelson.mot.db.model.CategoriesProvider;
import dev.nelson.mot.loadercalback.StatisticLoaderCallbacks;
import dev.nelson.mot.utils.DateUtils;


public class StatisticFragment extends Fragment implements SetDataFromStatisticLoader, OnChartValueSelectedListener{

    @BindView(R.id.pie_chart)
    PieChart mChart;
    //xData
    private LinkedList<String> mCategoriesNames = new LinkedList<>();
    //yData
    private LinkedList<Double> mSumPerCategory = new LinkedList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_statistic, container, false);
        ButterKnife.bind(this, view);
        StatisticLoaderCallbacks mLoaderCallbacks = new StatisticLoaderCallbacks(getContext(), this);
        getActivity().getSupportLoaderManager().initLoader(StatisticLoaderCallbacks.LOADER_ID, null, mLoaderCallbacks);
        initPieChart();
        // i don't like this, it looks like crunch, but i don't know how to fix this in other way.
        // without this check after changing fragment xData and yData arrays are empty. and loader doesn't load data automatically
        if (mSumPerCategory.size() == 0 && mCategoriesNames.size() == 0){
            getActivity().getSupportLoaderManager().restartLoader(StatisticLoaderCallbacks.LOADER_ID, null, mLoaderCallbacks);
        }
        return view;
    }

    @Override
    public void setDataFromStatisticLoader(LinkedList<String> categoriesNames, LinkedList<Double> sumPerCategory) {
        mCategoriesNames = categoriesNames;
        mSumPerCategory = sumPerCategory;
        mChart.setCenterText(generateCenterCircleText());
        setData();
    }

    // OnChartValueSelectedListener methods
    @Override
    public void onValueSelected(Entry e, Highlight h) {
        if (e == null){
            return;
        }
        Log.i("VAL SELECTED",
                "Value: " + e.getY() + ", index: " + h.getX()
                        + ", DataSet index: " + h.getDataSetIndex());
        Toast.makeText(getActivity(), String.valueOf(e.getY()), Toast.LENGTH_SHORT).show();

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
        mChart.setCenterTextSize(24);

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
            //convert double sum into float
            double d = mSumPerCategory.get(i);
            float f = (float)d;
            entries.add(new PieEntry(f, mCategoriesNames.get(i)));
        }

        PieDataSet dataSet = new PieDataSet(entries, "My Data set string label");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);

        //legend turned off
        mChart.getLegend().setEnabled(false);

        // add a lot of colors
        ArrayList<Integer> colors = new ArrayList<Integer>();

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());
        dataSet.setColors(colors);

        dataSet.setValueLinePart1OffsetPercentage(80.f);
        dataSet.setValueLinePart1Length(0.2f);
        dataSet.setValueLinePart2Length(0.4f);
//        dataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
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
        double sumPerMonth = 0;
        for (Double sumOfCategory: mSumPerCategory) {
            sumPerMonth += sumOfCategory;
        }
        SpannableString s = new SpannableString(getString(R.string.pie_chart_center_text) + sumPerMonth);
        s.setSpan(new RelativeSizeSpan(0.6f), 0, 20, 0);
        return s;
    }

    private void queryTest(){
        SQLiteOpenHelperImpl helper = new SQLiteOpenHelperImpl(getContext());
        SQLiteDatabase db = helper.getReadableDatabase();

//        String rawQuery = "SELECT " + CategoriesProvider.TABLE_NAME + "." + CategoriesProvider.Columns.CATEGORY_NAME + ", "
//                + " SUM(" + PaymentsProvider.TABLE_NAME + "." + PaymentsProvider.Columns.COST + ")"
//                + " FROM " + PaymentsProvider.TABLE_NAME
//                + " LEFT JOIN " + CategoriesProvider.TABLE_NAME
//                + " ON " + PaymentsProvider.TABLE_NAME + "." + PaymentsProvider.Columns.CATEGORY_ID + "=" + CategoriesProvider.TABLE_NAME + "." + CategoriesProvider.Columns._ID
//                + " GROUP BY " + PaymentsProvider.TABLE_NAME + "." + PaymentsProvider.Columns.CATEGORY_ID;
//        String rawQuery = "select sum(cost) from payments where category_id = 1 ";
        String rawQuery = "select categories.category_name, sum(cost) from payments left join categories on payments.category_id = categories._id group by payments.category_id ";
        Cursor cursor = db.rawQuery(rawQuery, null);
        LinkedList<String> categoryNames = new LinkedList<>();
        LinkedList<Double> categorySum = new LinkedList<>();
            while (cursor.moveToNext()) {
                mCategoriesNames.add(cursor.getString(cursor.getColumnIndex(CategoriesProvider.Columns.CATEGORY_NAME)));
                mSumPerCategory.add(cursor.getDouble(cursor.getColumnIndex("sum(cost)")));
            }
        cursor.close();
        setData();
    }
}
