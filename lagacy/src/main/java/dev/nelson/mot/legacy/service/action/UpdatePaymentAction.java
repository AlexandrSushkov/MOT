package dev.nelson.mot.legacy.service.action;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import dev.nelson.mot.legacy.db.model.PaymentsProvider;
import dev.nelson.mot.legacy.utils.StringUtils;


public class UpdatePaymentAction implements DataOperationAction {

    private Context mContext;
    public UpdatePaymentAction(Context context) {
        mContext = context;
    }

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
        mContext.getContentResolver().update(uri, cv, null, null);
    }
}
