package dev.nelson.mot.service.action;

import android.net.Uri;
import android.os.Bundle;

import dev.nelson.mot.db.model.PaymentsProvider;
import dev.nelson.mot.utils.MyApplication;
import dev.nelson.mot.utils.Constants;


public class DeletePaymentAction implements DataOperationAction {
    @Override
    public void perform(Bundle bundle) {
        int paymentId = bundle.getInt(PaymentsProvider.Columns._ID);
        Uri uri = Uri.withAppendedPath(PaymentsProvider.URI, String.valueOf(paymentId));
        MyApplication.getContext().getContentResolver().delete(uri, null, null);
    }
}
