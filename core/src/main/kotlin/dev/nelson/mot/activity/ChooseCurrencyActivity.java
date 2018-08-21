package dev.nelson.mot.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import dev.nelson.mot.R;
import dev.nelson.mot.utils.Constants;

public class ChooseCurrencyActivity extends AppCompatActivity {

    public static final String LANGUAGE_KEY = "language_key";
    public static final String COUNTRY_KEY = "country_key";

    RadioButton mEuro = findViewById(R.id.radio_euro);
    RadioButton mPound= findViewById(R.id.radio_pound);
    RadioButton mRuble= findViewById(R.id.radio_ruble);
    RadioButton mHryvnia= findViewById(R.id.radio_hryvnia);
    RadioButton mDollar= findViewById(R.id.radio_dollar);
    TextView mBtnNext= findViewById(R.id.activity_choose_currency_button_choose);

    private String mLanguage = null;
    private String mCountry = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_currency);

        mEuro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLanguage = getString(R.string.currency_locale_euro_language);
                mCountry = getString(R.string.currency_locale_euro_country);
            }
        });

        mPound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLanguage = getString(R.string.currency_locale_pound_language);
                mCountry = getString(R.string.currency_locale_pound_country);
            }
        });

        mRuble.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLanguage = getString(R.string.currency_locale_ruble_language);
                mCountry = getString(R.string.currency_locale_ruble_country);
            }
        });

        mHryvnia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLanguage = getString(R.string.currency_locale_hryvnia_language);
                mCountry = getString(R.string.currency_locale_hryvnia_country);
            }
        });


        mDollar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLanguage = getString(R.string.currency_locale_dollar_language);
                mCountry = getString(R.string.currency_locale_dollar_county);
            }
        });

        mBtnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mLanguage != null && mCountry != null){
                    SharedPreferences settings = getSharedPreferences(Constants.SHARED_PREFERENCES_NAME, MODE_PRIVATE);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putString(Constants.LANGUAGE_KEY, mLanguage);
                    editor.putString(Constants.COUNTRY_KEY, mCountry);
                    editor.apply();
                    //start main activity
                    startActivity(new Intent(ChooseCurrencyActivity.this, ChooseVersionActivity.class));

                    finish();
                } else {
                    Toast.makeText(ChooseCurrencyActivity.this, getString(R.string.choose_currency_toast_message), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(LANGUAGE_KEY, mLanguage);
        outState.putString(COUNTRY_KEY, mCountry);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mLanguage = savedInstanceState.getString(LANGUAGE_KEY);
        mCountry = savedInstanceState.getString(COUNTRY_KEY);
    }
}
