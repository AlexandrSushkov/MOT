package dev.nelson.mot.utils;

import android.content.Context;
import android.content.SharedPreferences;
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

    public CurrencyTextWatcher(EditText editText) {
        editTextWeakReference = new WeakReference<EditText>(editText);
        SharedPreferences settings = MyApplication.getContext().getSharedPreferences(Constants.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        String language = settings.getString(Constants.LANGUAGE_KEY, Constants.NO_LANGUAGE);
        String country = settings.getString(Constants.COUNTRY_KEY, Constants.NO_COUNTRY);
        myLocale = new Locale(language, country);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        stringLengthBeforeChanges = s.length();

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
        if(stringLengthBeforeChanges > stringLengthAfterChanges){
            cleanString = cleanString.substring(0, cleanString.length()-1);
        }
        BigDecimal parsed = new BigDecimal(cleanString).setScale(2, BigDecimal.ROUND_FLOOR).divide(new BigDecimal(100), BigDecimal.ROUND_FLOOR);
        String formatted = NumberFormat.getCurrencyInstance(myLocale).format(parsed);
        editText.setText(formatted);
        editText.setSelection(formatted.length());
        editText.addTextChangedListener(this);
    }
}
