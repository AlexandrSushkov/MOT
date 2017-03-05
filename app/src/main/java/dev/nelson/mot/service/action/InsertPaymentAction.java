package dev.nelson.mot.service.action;

import android.content.ContentValues;
import android.os.Bundle;

import dev.nelson.mot.db.model.PaymentsProvider;
import dev.nelson.mot.utils.MyApplication;

class InsertPaymentAction implements DataOperationAction{

    @Override
    public void perform(Bundle bundle) {
        String wasteTitle = bundle.getString(PaymentsProvider.Columns.TITLE);
        String summary = bundle.getString(PaymentsProvider.Columns.SUMMARY);
        int categoryId = bundle.getInt(PaymentsProvider.Columns.CATEGORY_ID, -1);
        double cost = bundle.getDouble(PaymentsProvider.Columns.COST, -1);
        ContentValues cv = new ContentValues();
        cv.put(PaymentsProvider.Columns.TITLE, wasteTitle);
        cv.put(PaymentsProvider.Columns.SUMMARY, summary);
        cv.put(PaymentsProvider.Columns.CATEGORY_ID, categoryId);
        cv.put(PaymentsProvider.Columns.COST, cost);
        MyApplication.getContext().getContentResolver().insert(PaymentsProvider.URI, cv);
    }
}
