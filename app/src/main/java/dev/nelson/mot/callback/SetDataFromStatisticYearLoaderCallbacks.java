package dev.nelson.mot.callback;

import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;

public interface SetDataFromStatisticYearLoaderCallbacks {

    public void setDataFromStatisticLoaderCallbacks(ArrayList<String> categoriesNames, ArrayList<BarEntry> enries);
}
