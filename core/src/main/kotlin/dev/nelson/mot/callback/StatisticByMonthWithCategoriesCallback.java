package dev.nelson.mot.callback;

import com.github.mikephil.charting.data.BarData;

import java.util.ArrayList;

public interface StatisticByMonthWithCategoriesCallback {

    public void getDataFromStatisticLoaderCallbacks(ArrayList<BarData> months);
}
