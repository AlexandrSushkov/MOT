package dev.nelson.mot.legacy.callback;

import com.github.mikephil.charting.data.BarData;

import java.util.ArrayList;

public interface StatisticByYearsCallback {
    public void getDataFromStatisticByYearsLoaderCallbacks(ArrayList<BarData> years);
}
