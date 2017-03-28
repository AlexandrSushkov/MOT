package dev.nelson.mot.utils;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.text.NumberFormat;

public class MyYAxisValueFormatter implements IAxisValueFormatter{

    public static final int LEFT = 1;
    public static final int RIGHT = 2;

    private int mAxis;

    public MyYAxisValueFormatter(int axis) {
        mAxis = axis;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        if (mAxis == LEFT){
            return NumberFormat.getCurrencyInstance(LocaleUtils.getLocaleForChoosenCurrency()).format(value);
        }else{
            return "";
        }
    }
}
