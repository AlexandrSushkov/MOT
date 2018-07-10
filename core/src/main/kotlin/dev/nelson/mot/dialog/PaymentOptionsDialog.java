package dev.nelson.mot.dialog;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import dev.nelson.mot.R;
import dev.nelson.mot.activity.PaymentActivity;
import dev.nelson.mot.db.model.PaymentsProvider;
import dev.nelson.mot.service.DataOperationService;
import dev.nelson.mot.service.action.DataOperationFabric;

public class PaymentOptionsDialog extends DialogFragment{

    public static final String PAYMENT_ID_KEY = "payment_id";

    private Context mContext;
    private int paymentId;

    public static PaymentOptionsDialog newInstance(int id) {
        Bundle args = new Bundle();
        PaymentOptionsDialog fragment = new PaymentOptionsDialog();
        args.putInt(PAYMENT_ID_KEY, id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getContext();
        paymentId = getArguments().getInt(PAYMENT_ID_KEY, -1);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.dialog_payment_options_title);
        builder.setItems(R.array.payment_options_dialog, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case 0:
                        //edit payment
                        Intent editIntent = new Intent(mContext, PaymentActivity.class);
                        editIntent.setAction(PaymentActivity.ACTION_EDIT);
                        editIntent.putExtra(PaymentsProvider.Columns._ID, paymentId);
                        mContext.startActivity(editIntent);
                        break;
                    case 1:
                        //delete payment
                        showConfirmDialog();
                        break;
                }
            }
        });
        return builder.create();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(PAYMENT_ID_KEY, paymentId);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null){
            paymentId = savedInstanceState.getInt(PAYMENT_ID_KEY);
        }
    }
    
    private void showConfirmDialog(){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(mContext);
        dialogBuilder.setTitle(R.string.dialog_are_you_sure_title);
        dialogBuilder.setPositiveButton(R.string.dialog_positive_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent deleteIntent = new Intent(mContext, DataOperationService.class);
                deleteIntent.setAction(DataOperationFabric.DELETE_PAYMENT);
                deleteIntent.putExtra(PaymentsProvider.Columns._ID, paymentId);
                mContext.startService(deleteIntent);
            }
        });
        dialogBuilder.setNegativeButton(R.string.dialog_negative_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog areYouSureDialog = dialogBuilder.create();
        areYouSureDialog.show();
    }
}
