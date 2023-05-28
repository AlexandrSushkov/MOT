package dev.nelson.mot.legacy.loadercallback;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;

import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import dev.nelson.mot.legacy.callback.EmptyCursorCallback;
import dev.nelson.mot.legacy.callback.StatisticByMonthsCallback;
import dev.nelson.mot.legacy.db.model.CategoriesProvider;
import dev.nelson.mot.legacy.db.model.PaymentsProvider;
import dev.nelson.mot.legacy.loader.RawQueryCursorLoader;
import dev.nelson.mot.legacy.utils.Constants;
import dev.nelson.mot.legacy.utils.DateUtils;
import dev.nelson.mot.legacy.utils.StringUtils;

public class StatisticByMonthsLoaderCallbacks implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final int LOADER_ID = 31;

    private Context mContext;
    private StatisticByMonthsCallback mStatisticByMonthCallback;
    private EmptyCursorCallback mEmptyCursorCallback;


    public StatisticByMonthsLoaderCallbacks(Context context, StatisticByMonthsCallback statisticByMonthsCallback, EmptyCursorCallback emptyCursorCallback) {
        mContext = context;
        mStatisticByMonthCallback = statisticByMonthsCallback;
        mEmptyCursorCallback = emptyCursorCallback;
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

            LineDataSet set = new LineDataSet(entries, "Total: " + StringUtils.formattedCost(mContext, totalCost));
            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
            dataSets.add(set);
            LineData lineData = new LineData(dataSets);

            mStatisticByMonthCallback.getDataFromStatisticByMonthsLoaderCallbacks(lineData);
        }else {
            mEmptyCursorCallback.showNoDataAnnouncement();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}