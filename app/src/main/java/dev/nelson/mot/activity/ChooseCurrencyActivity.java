package dev.nelson.mot.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dev.nelson.mot.R;
import dev.nelson.mot.utils.Constants;

public class ChooseCurrencyActivity extends AppCompatActivity {

    public static final String LANGUAGE_KEY = "locale_key";
    public static final String COUNTRY_KEY = "country_key";

    @BindView(R.id.radio_euro)
    RadioButton mEuro;
    @BindView(R.id.radio_pound)
    RadioButton mPound;
    @BindView(R.id.radio_ruble)
    RadioButton mRuble;
    @BindView(R.id.radio_hryvnia)
    RadioButton mHryvnia;
    @BindView(R.id.radio_dollar)
    RadioButton mDollar;
    @BindView(R.id.activity_choose_currency_button_next)
    TextView mBtnNext;

    private String mLanguage = null;
    private String mCountry = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_currency);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.radio_euro)
    void onClickmEuro(){
        mLanguage = getString(R.string.currency_locale_euro_language);
        mCountry = getString(R.string.currency_locale_euro_country);
    }

    @OnClick(R.id.radio_pound)
    void onClickmPound(){
        mLanguage = getString(R.string.currency_locale_pound_language);
        mCountry = getString(R.string.currency_locale_pound_country);
    }

    @OnClick(R.id.radio_ruble)
    void onClickmRuble(){
        mLanguage = getString(R.string.currency_locale_ruble_language);
        mCountry = getString(R.string.currency_locale_ruble_country);
    }

    @OnClick(R.id.radio_hryvnia)
    void onClickmHryvnia(){
        mLanguage = getString(R.string.currency_locale_hryvnia_language);
        mCountry = getString(R.string.currency_locale_hryvnia_country);
    }

    @OnClick(R.id.radio_dollar)
    void onClickmDollar(){
        mLanguage = getString(R.string.currency_locale_dollar_language);
        mCountry = getString(R.string.currency_locale_dollar_county);
    }

    @OnClick(R.id.activity_choose_currency_button_next)
    void onClickBtnNext(){
        if(mLanguage != null && mCountry != null){
            SharedPreferences settings = getSharedPreferences(Constants.SHARED_PREFERENCES_NAME, MODE_PRIVATE);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString(Constants.LANGUAGE_KEY, mLanguage);
            editor.putString(Constants.COUNTRY_KEY, mCountry);
            editor.apply();
            //start main activity
            Intent mainActivityIntent = new Intent(this, MainActivity.class);
            startActivity(mainActivityIntent);
            finish();
        } else {
            Toast.makeText(this, getString(R.string.choose_currency_toast_message), Toast.LENGTH_SHORT).show();
        }
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
