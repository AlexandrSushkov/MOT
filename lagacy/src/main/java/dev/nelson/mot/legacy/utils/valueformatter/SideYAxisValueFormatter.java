package dev.nelson.mot.legacy.utils.valueformatter;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;

public class SideYAxisValueFormatter extends ValueFormatter {

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        return ValueFormatterUtils.formatSideYAxisValue(value);
    }
}
