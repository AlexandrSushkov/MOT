package dev.nelson.mot.loadercalback;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.interfaces.datasets.IPieDataSet;

import java.util.ArrayList;

import dev.nelson.mot.R;
import dev.nelson.mot.callback.StatisticCurrentMonthCallback;
import dev.nelson.mot.db.model.CategoriesProvider;
import dev.nelson.mot.db.model.PaymentsProvider;
import dev.nelson.mot.loader.RawQueryCursorLoader;
import dev.nelson.mot.utils.DateUtils;
import dev.nelson.mot.utils.StringUtils;


public class StatisticCurrentMonthLoaderCallbacks implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final int LOADER_ID = 30;

    private Context mContext;
    private StatisticCurrentMonthCallback mCallbackObj;

    public StatisticCurrentMonthLoaderCallbacks(Context context, StatisticCurrentMonthCallback callbackObj) {
        mContext = context;
        mCallbackObj = callbackObj;
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        if (id == LOADER_ID) {
//        select categories.category_name, sum(payments.cost) as cost
//        from payments
//        left join categories on payments.category_id = categories._id
//        where payments.date between ? and ?
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
        if (data != null && data.moveToFirst()) {
            ArrayList<PieEntry> entries = new ArrayList<>();
            String categoryName;
            long cost;
            long totalCost = 0;
            Log.d("tag", "CURSOR DATA");
            while (!data.isAfterLast()) {
                if(data.getString(data.getColumnIndex(CategoriesProvider.Columns.CATEGORY_NAME)) == null){
                    categoryName = mContext.getString(R.string.no_category_category_name);
                }else {
                    categoryName = data.getString(data.getColumnIndex(CategoriesProvider.Columns.CATEGORY_NAME));
                }
                cost = data.getLong(data.getColumnIndex(PaymentsProvider.Columns.COST));
                entries.add(new PieEntry(cost, categoryName));
                totalCost += cost;
                data.moveToNext();
            }
            PieDataSet dataSet = new PieDataSet(entries, StringUtils.formattedCost(totalCost));
            PieData pieData = new PieData(dataSet);

            mCallbackObj.setDataFromStatisticCurrentMonthLoaderCallbacks(pieData);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        loader = null;
    }
}
