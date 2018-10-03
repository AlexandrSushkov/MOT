package dev.nelson.mot.legacy.callback;

import com.github.mikephil.charting.data.LineData;

import java.util.ArrayList;

/**
 * Created by Nelson on 3/28/17.
 */

public interface StatisticByCategoriesCallback {
    public void getDataFromStatisticByCategoriesLoaderCallback(ArrayList<LineData> categories );
}
