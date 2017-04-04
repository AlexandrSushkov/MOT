package dev.nelson.mot.utils.marker;

import android.content.Context;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;

import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import dev.nelson.mot.R;
import dev.nelson.mot.utils.StringUtils;

public class CustomMarker extends MarkerView {

    @BindView(R.id.custom_marker_text)
    TextView mText;
    private DecimalFormat format;

    public CustomMarker(Context context) {
        super(context, R.layout.custom_marker);
        ButterKnife.bind(this);
    }

    // callbacks everytime the MarkerView is redrawn, can be used to update the
    // content (user-interface)
    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        mText.setText(e.getData() + ": " + String.valueOf(StringUtils.formattedCost((long) e.getY())));
        super.refreshContent(e, highlight);
    }

    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth() / 2), -getHeight());
    }
}
