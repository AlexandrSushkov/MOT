package dev.nelson.mot.loadercalback;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;

import dev.nelson.mot.callback.StatisticByMonthsCallback;
import dev.nelson.mot.db.model.CategoriesProvider;
import dev.nelson.mot.db.model.PaymentsProvider;
import dev.nelson.mot.loader.RawQueryCursorLoader;
import dev.nelson.mot.utils.Constants;
import dev.nelson.mot.utils.DateUtils;
import dev.nelson.mot.utils.StringUtils;

public class StatisticByMonthsLoaderCallbacks implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final int LOADER_ID = 31;

    private Context mContext;
    private StatisticByMonthsCallback mCallback;

    public StatisticByMonthsLoaderCallbacks(Context context, StatisticByMonthsCallback callback) {
        mContext = context;
        mCallback = callback;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (id == LOADER_ID) {
//            SELECT  strftime('%Y', payments.date) AS year, strftime('%m', payments.date) AS month, sum(payments.cost) AS cost
//            FROM payments
//            LEFT JOIN
//            categories ON payments.category_id = categories._id
//            GROUP BY strftime('%Y', payments.date), strftime('%m', payments.date)
//            ORDER BY  strftime('%Y', payments.date) DESC, strftime('%m', payments.date) DESC

            String rawQuery = "SELECT "
                    + "strftime('%Y', " + PaymentsProvider.TABLE_NAME + "." + PaymentsProvider.Columns.DATE + ") AS " + Constants.YEAR + ", "
                    + "strftime('%m', " + PaymentsProvider.TABLE_NAME + "." + PaymentsProvider.Columns.DATE + ") AS " + Constants.MONTH + ", "
                    + "sum(" + PaymentsProvider.TABLE_NAME + "." + PaymentsProvider.Columns.COST + ")" + " AS " + PaymentsProvider.Columns.COST
                    + " FROM " + PaymentsProvider.TABLE_NAME
                    + " LEFT JOIN " + CategoriesProvider.TABLE_NAME
                    + " ON " + PaymentsProvider.TABLE_NAME + "." + PaymentsProvider.Columns.CATEGORY_ID + "=" + CategoriesProvider.TABLE_NAME + "." + CategoriesProvider.Columns._ID
                    + " GROUP BY " + "strftime('%Y'," + PaymentsProvider.TABLE_NAME + "." + PaymentsProvider.Columns.DATE + "), "
                    + "strftime('%m'," + PaymentsProvider.TABLE_NAME + "." + PaymentsProvider.Columns.DATE + ")";
            return new RawQueryCursorLoader(mContext, rawQuery, null);
        } else {
            throw new IllegalArgumentException(getClass().getName() + " Wrong loader id: " + id);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data != null && data.moveToFirst()) {
            //initial data
            float xAxis = 0;
            long totalCost = 0;

            ArrayList<Entry> entries = new ArrayList<>();
            String year;
            String month;
            long cost;
            while (!data.isAfterLast()) {
                //get data from cursor
                year = data.getString(data.getColumnIndex(Constants.YEAR));
                month = data.getString(data.getColumnIndex(Constants.MONTH));
                cost = data.getLong(data.getColumnIndex(PaymentsProvider.Columns.COST));

                entries.add(new Entry(xAxis, cost, DateUtils.months.get(month) + " " + year));
                totalCost += cost;
                xAxis++;
                data.moveToNext();
            }

            LineDataSet set = new LineDataSet(entries, "Total: " + StringUtils.formattedCost(totalCost));
            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
            dataSets.add(set);
            LineData lineData = new LineData(dataSets);

            mCallback.getDataFromStatisticByMonthsLoaderCallbacks(lineData);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}