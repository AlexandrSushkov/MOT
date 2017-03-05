package dev.nelson.mot.loadercalback;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import dev.nelson.mot.callback.SetDataFromStatisticThisMonthTotalLoader;
import dev.nelson.mot.db.model.PaymentsProvider;

public class StatisticThisMonthTotalLoaderCallbacks implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final int LOADER_ID = 30;

    private Context mContext;
    private SetDataFromStatisticThisMonthTotalLoader mCallback;

    public StatisticThisMonthTotalLoaderCallbacks(Context context, SetDataFromStatisticThisMonthTotalLoader callbackObj) {
        mContext = context;
        this.mCallback = callbackObj;
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        String[] projection = new String[]{"SUM(" + PaymentsProvider.Columns.COST + ")"};
        // TODO: 2/23/17 change request parameters on first and last day of the current month
        String selection = PaymentsProvider.Columns.DATE + " between '2017-02-01' and '2017-02-31'";
        return new CursorLoader(mContext, PaymentsProvider.URI, projection, selection, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data != null) {
            data.moveToFirst();
            mCallback.setValues(data.getDouble(data.getColumnIndex("SUM(cost)")));

        }
    }


    @Override
    public void onLoaderReset(Loader loader) {

    }
}
