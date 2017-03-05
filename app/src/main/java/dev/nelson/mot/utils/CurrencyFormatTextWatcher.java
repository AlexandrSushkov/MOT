package dev.nelson.mot.utils;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class CurrencyFormatTextWatcher implements TextWatcher {

    private EditText mEditText;

    public CurrencyFormatTextWatcher(EditText editText) {
        mEditText = editText;
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        /***
         * No need to continue the function if there is nothing to
         * format
         ***/
        if (s.length() == 0) {
            return;
        }

        /*** Now the number of digits in price is limited to 8 ***/
        String value = s.toString().replaceAll(",", "");
        if (value.length() > 8) {
            value = value.substring(0, 8);
        }
        String formattedPrice = getFormatedCurrency(value);
        if (!(formattedPrice.equalsIgnoreCase(s.toString()))) {
            /***
             * The below given line will call the function recursively
             * and will ends at this if block condition
             ***/
            mEditText.setText(formattedPrice);
            mEditText.setSelection(mEditText.length());
        }
    }

    /**
     * @param value not formated amount
     * @return Formated string of amount (##,##,##,###).
     */
    private String getFormatedCurrency(String value) {
        try {
            NumberFormat formatter = new DecimalFormat("##,##,##,###");
            return formatter.format(Double.parseDouble(value));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}


