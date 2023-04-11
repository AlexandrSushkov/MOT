package dev.nelson.mot.legacy.utils.valueformatter;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

public class BarDataXAxisValueFormatter extends ValueFormatter {

    private IBarDataSet mDataSet;

    public BarDataXAxisValueFormatter(IBarDataSet data) {
        mDataSet = data;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        return (String) mDataSet.getEntryForXValue(value, value).getData();
    }
}
