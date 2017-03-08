package dev.nelson.mot.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Locale;

/**
 * Created by Nelson on 3/8/17.
 */

public class LocaleUtils {

    public static Locale getLocaleForChoosenCurrency(){
        SharedPreferences settings = MyApplication.getContext().getSharedPreferences(Constants.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        String language = settings.getString(Constants.LANGUAGE_KEY, Constants.NO_LANGUAGE);
        String country = settings.getString(Constants.COUNTRY_KEY, Constants.NO_COUNTRY);
        return new Locale(language, country);
    }
}
