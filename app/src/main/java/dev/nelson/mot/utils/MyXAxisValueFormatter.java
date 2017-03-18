package dev.nelson.mot.utils;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nelson on 3/18/17.
 */

public class MyXAxisValueFormatter implements IAxisValueFormatter {

    private ArrayList<String> xAxisValues;

    public MyXAxisValueFormatter(ArrayList<String> values) {
        xAxisValues = values;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        int position = (int) value -1;
        if (position == -1){
            position =0;
        }
        return xAxisValues.get(position);
    }
}
