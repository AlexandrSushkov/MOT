package dev.nelson.mot.utils.valueformatter;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;


public class LineDataXAxisValueFormatter implements IAxisValueFormatter {

    private ILineDataSet mDataSet;

    public LineDataXAxisValueFormatter(ILineDataSet dataSet) {
        mDataSet = dataSet;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        return (String) mDataSet.getEntryForXValue(value, value).getData();
    }
}
