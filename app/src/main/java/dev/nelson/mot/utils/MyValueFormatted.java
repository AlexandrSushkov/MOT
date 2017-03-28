package dev.nelson.mot.utils;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.NumberFormat;

/**
 * Created by Nelson on 3/19/17.
 */

public class MyValueFormatted implements IValueFormatter {
    @Override
    public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
        return NumberFormat.getCurrencyInstance(LocaleUtils.getLocaleForChoosenCurrency()).format(value);
    }
}
