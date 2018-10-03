package dev.nelson.mot.legacy.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Locale;

public class LocaleUtils {

    public static Locale getLocaleForChosenCurrency(Context context){
        SharedPreferences settings = context.getSharedPreferences(Constants.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        String language = settings.getString(Constants.LANGUAGE_KEY, Constants.NO_LANGUAGE);
        String country = settings.getString(Constants.COUNTRY_KEY, Constants.NO_COUNTRY);
        return new Locale(language, country);
    }
}
