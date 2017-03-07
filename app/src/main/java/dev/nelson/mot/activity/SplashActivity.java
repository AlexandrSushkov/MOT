package dev.nelson.mot.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import dev.nelson.mot.utils.Constants;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        Intent intent1 = new Intent(this, DataOperationService.class);
//        intent1.setAction(DataOperationFabric.INSERT_PAYMENT);
//        intent1.putExtra(PaymentsProvider.Columns.TITLE, "test");
//
//        intent1.putExtra(PaymentsProvider.Columns.SUMMARY, "summary");
//        intent1.putExtra(PaymentsProvider.Columns.CATEGORY_ID, 2);
//        intent1.putExtra(PaymentsProvider.Columns.COST, 34.50);
//        startService(intent1);

//        if (isCurrencySet()) {
//            startMainActivity();
//        } else {
//          start choose currency activity
            Intent intent = new Intent(this, ChooseCurrencyActivity.class);
            startActivity(intent);
            finish();
//        }
    }

    private void startMainActivity() {
        Intent mainActivityIntent = new Intent(this, MainActivity.class);
        startActivity(mainActivityIntent);
        finish();
    }

    private boolean isCurrencySet() {
        SharedPreferences settings = getSharedPreferences(Constants.SHARED_PREFERENCES_NAME, MODE_PRIVATE);
        settings.getString(Constants.LANGUAGE_KEY, Constants.NO_LANGUAGE);
        return !(settings.getString(Constants.LANGUAGE_KEY, Constants.NO_LANGUAGE).equals(Constants.NO_LANGUAGE) ||
                settings.getString(Constants.COUNTRY_KEY, Constants.NO_COUNTRY).equals(Constants.NO_COUNTRY));
    }
}
