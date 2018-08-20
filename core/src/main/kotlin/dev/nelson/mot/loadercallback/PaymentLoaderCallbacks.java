package dev.nelson.mot.loadercallback;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;

import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import dev.nelson.mot.callback.SetDataFromPaymentLoaderCallbacks;
import dev.nelson.mot.db.model.CategoriesProvider;
import dev.nelson.mot.db.model.PaymentsProvider;
import dev.nelson.mot.loader.RawQueryCursorLoader;

public class PaymentLoaderCallbacks implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final int LOADER_ID = 11;

    private Context mContext;
    private int mPaymentId;
    private SetDataFromPaymentLoaderCallbacks mCallbackObj;

    public PaymentLoaderCallbacks(Context context, SetDataFromPaymentLoaderCallbacks mCallbackObj, int paymentId) {
        mContext = context;
        mPaymentId = paymentId;
        this.mCallbackObj = mCallbackObj;
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        if (id == LOADER_ID) {
//            select payments.title, payments.category_id, categories.category_name, payments.cost, payments.summary
//            from payments
//            left join categories on payments.category_id = categories._id
//            where payments._id = 5

            String rawQuery = "select " + PaymentsProvider.TABLE_NAME + "." + PaymentsProvider.Columns.TITLE + ", "
                    + PaymentsProvider.TABLE_NAME + "." + PaymentsProvider.Columns.CATEGORY_ID + ", "
                    + CategoriesProvider.TABLE_NAME + "." + CategoriesProvider.Columns.CATEGORY_NAME + ", "
                    + PaymentsProvider.TABLE_NAME + "." + PaymentsProvider.Columns.COST + ", "
                    + PaymentsProvider.TABLE_NAME + "." + PaymentsProvider.Columns.SUMMARY
                    + " from " + PaymentsProvider.TABLE_NAME
                    + " left join " + CategoriesProvider.TABLE_NAME
                    + " on " + PaymentsProvider.TABLE_NAME + "." + PaymentsProvider.Columns.CATEGORY_ID + "=" + CategoriesProvider.TABLE_NAME + "." + CategoriesProvider.Columns._ID
                    + " where " + PaymentsProvider.TABLE_NAME + "." + PaymentsProvider.Columns._ID + "=?";

            String[] selectedArgs = new String[]{String.valueOf(mPaymentId)};
            return new RawQueryCursorLoader(mContext, rawQuery, selectedArgs);
        } else {
            throw new IllegalArgumentException(getClass().getName() + " Wrong loader id: " + id);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        String title = "";
        int categoryId = -1;
        String categoryName = "";
        long cost = 0;
        String summary = "";
        if (data != null && data.getCount() > 0) {
            data.moveToFirst();
            title = data.getString(data.getColumnIndex(PaymentsProvider.Columns.TITLE));
            if (data.getInt(data.getColumnIndex(PaymentsProvider.Columns.CATEGORY_ID)) != 0) {
                categoryId = data.getInt(data.getColumnIndex(PaymentsProvider.Columns.CATEGORY_ID));
            }
            if (data.getString(data.getColumnIndex(CategoriesProvider.Columns.CATEGORY_NAME)) != null) {
                categoryName = data.getString(data.getColumnIndex(CategoriesProvider.Columns.CATEGORY_NAME));
            }
            cost = data.getLong(data.getColumnIndex(PaymentsProvider.Columns.COST));
            summary = data.getString(data.getColumnIndex(PaymentsProvider.Columns.SUMMARY));
        }
        mCallbackObj.fillPaymentInitialStateWithData(title, categoryId, categoryName, cost, summary);
    }

    @Override
    public void onLoaderReset(Loader loader) {
        loader = null;
    }
}
