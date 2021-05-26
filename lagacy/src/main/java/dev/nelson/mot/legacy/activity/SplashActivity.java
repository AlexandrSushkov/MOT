package dev.nelson.mot.legacy.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import dev.nelson.mot.legacy.utils.Constants;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (isCurrencySet()) {
            startActivity(new Intent(this, MainActivity.class));
        } else {
            startActivity(new Intent(this, ChooseCurrencyActivity.class));
        }
        finish();
    }

    private boolean isCurrencySet() {
        SharedPreferences settings = getSharedPreferences(Constants.SHARED_PREFERENCES_NAME, MODE_PRIVATE);
        settings.getString(Constants.LANGUAGE_KEY, Constants.NO_LANGUAGE);
        return !(settings.getString(Constants.LANGUAGE_KEY, Constants.NO_LANGUAGE).equals(Constants.NO_LANGUAGE)
                || settings.getString(Constants.COUNTRY_KEY, Constants.NO_COUNTRY).equals(Constants.NO_COUNTRY));
    }
}