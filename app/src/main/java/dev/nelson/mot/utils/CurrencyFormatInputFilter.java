package dev.nelson.mot.utils;

import android.text.InputFilter;
import android.text.Spanned;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class CurrencyFormatInputFilter implements InputFilter {
//    private Pattern mPattern = Pattern.compile("[0-9]*\\.[0-9]{0,2}");
    private Pattern mPattern = Pattern.compile("[0-4]{1,4}");
//    Pattern mPattern = Pattern.compile("^(\\$)?([1-9]{1}[0-9]{0,2})(\\,\\d{3})*(\\.\\d{2})?$|^(\\$)?([1-9]{1}[0-9]{0,2})(\\d{3})*(\\.\\d{2})?$|^(0)?(\\.\\d{2})?$|^(\\$0)?(\\.\\d{2})?$|^(\\$\\.)(\\d{2})?$");

    @Override
    public CharSequence filter(
            CharSequence source,
            int start,
            int end,
            Spanned dest,
            int dstart,
            int dend) {

        String result = dest.subSequence(0, dstart)
                        + source.toString()
                        + dest.subSequence(dend, dest.length());

        Matcher matcher = mPattern.matcher(result);

        if (!matcher.matches()) return dest.subSequence(dstart, dend);

        return null;
    }
}
