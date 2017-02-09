package dev.nelson.mot.service.action;

import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;

import dev.nelson.mot.db.model.SpendingProvider;
import dev.nelson.mot.utils.MyApplication;
import dev.nelson.mot.utils.SqlUtils;

class InsertWasteAction implements DataOperationAction {

    @Override
    public void perform(Bundle bundle) {
        Uri uri = Uri.parse(bundle.getString(SqlUtils.URI_KEY));
        String wasteTitle = bundle.getString(SpendingProvider.Columns.TITLE);
        String summary = bundle.getString(SpendingProvider.Columns.SUMMARY);
        int categoryId = bundle.getInt(SpendingProvider.Columns.CATEGORY_ID, -1);
        double date = bundle.getDouble(SpendingProvider.Columns.DATE, -1);
        double cost = bundle.getDouble(SpendingProvider.Columns.COST, -1);
        ContentValues cv = new ContentValues();
        cv.put(SpendingProvider.Columns.TITLE, wasteTitle);
        cv.put(SpendingProvider.Columns.SUMMARY, summary);
        cv.put(SpendingProvider.Columns.CATEGORY_ID, categoryId);
        cv.put(SpendingProvider.Columns.DATE, date);
        cv.put(SpendingProvider.Columns.COST, cost);
        MyApplication.getContext().getContentResolver().insert(uri, cv);
    }
}
