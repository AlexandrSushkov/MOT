package dev.nelson.mot.service.action;

import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;

import dev.nelson.mot.db.model.PaymentsProvider;
import dev.nelson.mot.utils.MyApplication;


public class UpdatePaymentAction implements DataOperationAction {

    @Override
    public void perform(Bundle bundle) {
        Uri uri = Uri.withAppendedPath(PaymentsProvider.URI, String.valueOf(bundle.getInt(PaymentsProvider.Columns._ID, -1)));
        ContentValues cv = new ContentValues();
//        if (!bundle.getString(PaymentsProvider.Columns.TITLE, "null").equals("null")){
            cv.put(PaymentsProvider.Columns.TITLE, bundle.getString(PaymentsProvider.Columns.TITLE));
//        }
//        if (bundle.getInt(PaymentsProvider.Columns.CATEGORY_ID, -1) != -1){
            cv.put(PaymentsProvider.Columns.CATEGORY_ID, bundle.getInt(PaymentsProvider.Columns.CATEGORY_ID));
//        }
//        if (bundle.getDouble(PaymentsProvider.Columns.COST, -1) != -1){
            cv.put(PaymentsProvider.Columns.COST, bundle.getDouble(PaymentsProvider.Columns.COST));
//        }
//        if (!bundle.getString(PaymentsProvider.Columns.SUMMARY, "null").equals("null")){
            cv.put(PaymentsProvider.Columns.SUMMARY, bundle.getString(PaymentsProvider.Columns.SUMMARY));
//        }
        MyApplication.getContext().getContentResolver().update(uri, cv, null, null);
    }
}
