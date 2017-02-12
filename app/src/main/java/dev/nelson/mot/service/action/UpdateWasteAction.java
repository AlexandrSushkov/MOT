package dev.nelson.mot.service.action;

import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;

import dev.nelson.mot.db.model.SpendingProvider;
import dev.nelson.mot.utils.MyApplication;
import dev.nelson.mot.utils.SqlUtils;


public class UpdateWasteAction implements DataOperationAction {

    @Override
    public void perform(Bundle bundle) {
        Uri uri = Uri.parse(bundle.getString(SqlUtils.URI_KEY));
        ContentValues cv = new ContentValues();
        if (!bundle.getString(SpendingProvider.Columns.TITLE, "null").equals("null")){
            cv.put(SpendingProvider.Columns.TITLE, bundle.getString(SpendingProvider.Columns.TITLE));
        }
        if (!bundle.getString(SpendingProvider.Columns.SUMMARY, "null").equals("null")){
            cv.put(SpendingProvider.Columns.SUMMARY, bundle.getString(SpendingProvider.Columns.SUMMARY));
        }
        if (bundle.getInt(SpendingProvider.Columns.CATEGORY_ID, -1) != -1){
            cv.put(SpendingProvider.Columns.CATEGORY_ID, bundle.getString(SpendingProvider.Columns.CATEGORY_ID));
        }
        if (bundle.getDouble(SpendingProvider.Columns.DATE, -1) != -1){
            cv.put(SpendingProvider.Columns.DATE, bundle.getDouble(SpendingProvider.Columns.DATE));
        }
        if (bundle.getDouble(SpendingProvider.Columns.COST, -1) != -1){
            cv.put(SpendingProvider.Columns.COST, bundle.getDouble(SpendingProvider.Columns.COST));
        }
        MyApplication.getContext().getContentResolver().update(uri, cv, null, null);
    }
}
