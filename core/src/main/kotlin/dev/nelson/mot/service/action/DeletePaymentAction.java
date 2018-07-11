package dev.nelson.mot.service.action;

import android.net.Uri;
import android.os.Bundle;

import dev.nelson.mot.db.model.PaymentsProvider;
import dev.nelson.mot.MotApplication;


public class DeletePaymentAction implements DataOperationAction {
    @Override
    public void perform(Bundle bundle) {
        int paymentId = bundle.getInt(PaymentsProvider.Columns._ID);
        Uri uri = Uri.withAppendedPath(PaymentsProvider.URI, String.valueOf(paymentId));
        MotApplication.Companion.getContext().getContentResolver().delete(uri, null, null);
    }
}
