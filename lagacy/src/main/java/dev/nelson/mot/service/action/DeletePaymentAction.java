package dev.nelson.mot.service.action;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import dev.nelson.mot.db.model.PaymentsProvider;


public class DeletePaymentAction implements DataOperationAction {

    private Context mContext;
    public DeletePaymentAction(Context context) {
        mContext = context;
    }

    @Override
    public void perform(Bundle bundle) {
        int paymentId = bundle.getInt(PaymentsProvider.Columns._ID);
        Uri uri = Uri.withAppendedPath(PaymentsProvider.URI, String.valueOf(paymentId));
        mContext.getContentResolver().delete(uri, null, null);
    }
}
