package dev.nelson.mot.service.action;

import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;

import dev.nelson.mot.db.model.PaymentsProvider;
import dev.nelson.mot.utils.MyApplication;
import dev.nelson.mot.utils.StringUtils;


public class UpdatePaymentAction implements DataOperationAction {

    @Override
    public void perform(Bundle bundle) {
        Uri uri = Uri.withAppendedPath(PaymentsProvider.URI, String.valueOf(bundle.getInt(PaymentsProvider.Columns._ID, -1)));
        ContentValues cv = new ContentValues();
        cv.put(PaymentsProvider.Columns.TITLE, StringUtils.capitalizeFirstCharacter(bundle.getString(PaymentsProvider.Columns.TITLE)));
        if (bundle.getInt(PaymentsProvider.Columns.CATEGORY_ID) != -1 && bundle.getInt(PaymentsProvider.Columns.CATEGORY_ID) != 0) {
            cv.put(PaymentsProvider.Columns.CATEGORY_ID, bundle.getInt(PaymentsProvider.Columns.CATEGORY_ID));
        }
        cv.put(PaymentsProvider.Columns.COST, bundle.getLong(PaymentsProvider.Columns.COST));
        cv.put(PaymentsProvider.Columns.SUMMARY, StringUtils.capitalizeFirstCharacter(bundle.getString(PaymentsProvider.Columns.SUMMARY)));
        MyApplication.getContext().getContentResolver().update(uri, cv, null, null);
    }
}
