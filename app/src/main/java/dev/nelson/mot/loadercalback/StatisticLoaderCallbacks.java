package dev.nelson.mot.loadercalback;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;

import java.util.ArrayList;

import dev.nelson.mot.R;
import dev.nelson.mot.callback.SetDataFromStatisticLoader;
import dev.nelson.mot.db.model.CategoriesProvider;
import dev.nelson.mot.db.model.PaymentsProvider;
import dev.nelson.mot.loader.RawQueryCursorLoader;
import dev.nelson.mot.utils.DateUtils;


public class StatisticLoaderCallbacks implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final int LOADER_ID = 30;

    private Context mContext;
    private SetDataFromStatisticLoader mCallbackObj;

    public StatisticLoaderCallbacks(Context context, SetDataFromStatisticLoader callbackObj) {
        mContext = context;
        mCallbackObj = callbackObj;
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        if (id == LOADER_ID) {
//        select categories.category_name, sum(payments.cost) as cost
//        from payments
//        left join categories on payments.category_id = categories._id
//        group by payments.category_id
            String rawQuery = "select " + CategoriesProvider.TABLE_NAME + "." + CategoriesProvider.Columns.CATEGORY_NAME + ", "
                    + " sum(" + PaymentsProvider.Columns.COST + ")" + " as " + PaymentsProvider.Columns.COST
                    + " from " + PaymentsProvider.TABLE_NAME
                    + " left join " + CategoriesProvider.TABLE_NAME
                    + " on " + PaymentsProvider.TABLE_NAME + "." + PaymentsProvider.Columns.CATEGORY_ID + "=" + CategoriesProvider.TABLE_NAME + "." + CategoriesProvider.Columns._ID
                    + " where " + PaymentsProvider.TABLE_NAME + "." + PaymentsProvider.Columns.DATE + " between ? and ? "
                    + " group by " + PaymentsProvider.TABLE_NAME + "." + PaymentsProvider.Columns.CATEGORY_ID;
            String[] selectionArgs = new String[]{DateUtils.getFirstDayOfCurrentMonth(), DateUtils.getLastDayOfCurrentMonth()};
            return new RawQueryCursorLoader(mContext, rawQuery, selectionArgs);
        } else {
            throw new IllegalArgumentException(getClass().getName() + " Wrong loader id: " + id);
        }
    }

    @Override
    public void onLoadFinished(Loader loader, Cursor data) {
        if (data != null) {
            ArrayList<String> categoryNames = new ArrayList<>();
            ArrayList<Long> categorySum = new ArrayList<>();
            String categoryName;
            Log.d("tag", "CURSOR DATA");
            while (data.moveToNext()) {
                categoryName = data.getString(data.getColumnIndex(CategoriesProvider.Columns.CATEGORY_NAME));
                if (categoryName == null) {
                    categoryNames.add(mContext.getString(R.string.no_category_category_name));
                } else {
                    categoryNames.add(categoryName);
                }
                categorySum.add(data.getLong(data.getColumnIndex(PaymentsProvider.Columns.COST)));
            }
            mCallbackObj.setDataFromStatisticLoader(categoryNames, categorySum);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        loader = null;
    }
}
