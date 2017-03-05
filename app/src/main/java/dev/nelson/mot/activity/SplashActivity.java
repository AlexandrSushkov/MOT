package dev.nelson.mot.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import dev.nelson.mot.db.model.PaymentsProvider;
import dev.nelson.mot.service.DataOperationService;
import dev.nelson.mot.service.action.DataOperationFabric;

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

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
