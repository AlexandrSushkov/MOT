package dev.nelson.mot.utils;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.lang.ref.WeakReference;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

public class CurrencyTextWatcher implements TextWatcher {

    private final WeakReference<EditText> editTextWeakReference;
    private Locale myLocale;
    private int stringLengthBeforeChanges = 0;
    private int stringLengthAfterChanges = 0;
    private boolean isLastCharacterDigit;

    public CurrencyTextWatcher(EditText editText) {
        editTextWeakReference = new WeakReference<EditText>(editText);
        myLocale = LocaleUtils.getLocaleForChosenCurrency();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        stringLengthBeforeChanges = s.length();
        if(s.length() > 0){
            isLastCharacterDigit = StringUtils.isLastCharacterDigit(s.toString());
        }
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable editable) {
        EditText editText = editTextWeakReference.get();
        if (editText == null) return;
        String s = editable.toString();
        stringLengthAfterChanges = s.length();
        editText.removeTextChangedListener(this);
        String cleanString = StringUtils.cleanString(s);
        //this logic block used to correct removal digits when currency symbol after digits
        if(stringLengthBeforeChanges > stringLengthAfterChanges && !isLastCharacterDigit && cleanString.length() > 0){
            cleanString = cleanString.substring(0, cleanString.length()-1);
        }
        BigDecimal value;
        if(cleanString != null){
            value = cleanString.length() == 0 ? BigDecimal.ZERO : new BigDecimal(cleanString);
        } else {
            value = BigDecimal.ZERO;
        }
        BigDecimal parsed = value.setScale(2, BigDecimal.ROUND_FLOOR).divide(new BigDecimal(100), BigDecimal.ROUND_FLOOR);
        String formatted = NumberFormat.getCurrencyInstance(myLocale).format(parsed);
        editText.setText(formatted);
        editText.setSelection(formatted.length());
        editText.addTextChangedListener(this);
    }
}
