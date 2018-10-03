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
import dev.nelson.mot.R;
import dev.nelson.mot.legacy.callback.EmptyCursorCallback;
import dev.nelson.mot.legacy.callback.StatisticByCategoriesCallback;
import dev.nelson.mot.legacy.db.model.CategoriesProvider;
import dev.nelson.mot.legacy.db.model.PaymentsProvider;
import dev.nelson.mot.legacy.loader.RawQueryCursorLoader;
import dev.nelson.mot.legacy.utils.Constants;
import dev.nelson.mot.legacy.utils.DateUtils;

public class StatisticByCategoriesLoaderCallbacks implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final int LOADER_ID = 34;

    private Context mContext;
    private StatisticByCategoriesCallback mStatisticByCategoriesCallback;
    private EmptyCursorCallback mEmptyCursorCallback;

    public StatisticByCategoriesLoaderCallbacks(Context context, StatisticByCategoriesCallback statisticByCategoriesCallback, EmptyCursorCallback emptyCursorCallback) {
        mContext = context;
        mStatisticByCategoriesCallback = statisticByCategoriesCallback;
        mEmptyCursorCallback = emptyCursorCallback;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (id == LOADER_ID) {
//        SELECT categories._id, categories.category_name, strftime('%Y', payments.date) AS year, strftime('%m', payments.date) AS month, sum(payments.cost) AS cost
//        FROM payments
//        LEFT JOIN
//        categories ON payments.category_id = categories._id
//        GROUP BY strftime('%Y', payments.date), strftime('%m', payments.date), categories._id
//        ORDER BY  categories._id

            String rawQuery = "SELECT "
                    + CategoriesProvider.TABLE_NAME + "." + CategoriesProvider.Columns._ID + ", "
                    + CategoriesProvider.TABLE_NAME + "." + CategoriesProvider.Columns.CATEGORY_NAME + ", "
                    + "strftime('%Y', " + PaymentsProvider.TABLE_NAME + "." + PaymentsProvider.Columns.DATE + ") AS " + Constants.YEAR + ", "
                    + "strftime('%m', " + PaymentsProvider.TABLE_NAME + "." + PaymentsProvider.Columns.DATE + ") AS " + Constants.MONTH + ", "
                    + "sum(" + PaymentsProvider.TABLE_NAME + "." + PaymentsProvider.Columns.COST + ")" + " AS " + PaymentsProvider.Columns.COST
                    + " FROM " + PaymentsProvider.TABLE_NAME
                    + " LEFT JOIN " + CategoriesProvider.TABLE_NAME
                    + " ON " + PaymentsProvider.TABLE_NAME + "." + PaymentsProvider.Columns.CATEGORY_ID + "=" + CategoriesProvider.TABLE_NAME + "." + CategoriesProvider.Columns._ID
                    + " GROUP BY " + "strftime('%Y'," + PaymentsProvider.TABLE_NAME + "." + PaymentsProvider.Columns.DATE + "), "
                    + "strftime('%m'," + PaymentsProvider.TABLE_NAME + "." + PaymentsProvider.Columns.DATE + "), "
                    + CategoriesProvider.TABLE_NAME + "." + CategoriesProvider.Columns._ID
                    + " ORDER BY " + CategoriesProvider.TABLE_NAME + "." + CategoriesProvider.Columns._ID;

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
            int categoryIdKeeper = -1;
            String categoryNameKeeper = "";

            ArrayList<LineData> categories = new ArrayList<>();
            ArrayList<Entry> entries = null;
            String categoryName;
            int year = -1;
            String month;
            int categoryId;
            long cost;
            while (!data.isAfterLast()) {
                //get data from cursor
                if (data.getString(data.getColumnIndex(CategoriesProvider.Columns.CATEGORY_NAME)) == null) {
                    categoryName = mContext.getString(R.string.no_category_category_name);
                } else {
                    categoryName = data.getString(data.getColumnIndex(CategoriesProvider.Columns.CATEGORY_NAME));
                }
                if (data.getString(data.getColumnIndex(CategoriesProvider.Columns._ID)) == null) {
                    categoryId = 0;
                } else {
                    categoryId = data.getInt(data.getColumnIndex(CategoriesProvider.Columns._ID));
                }
                year = data.getInt(data.getColumnIndex(Constants.YEAR));
                month = data.getString(data.getColumnIndex(Constants.MONTH));
                cost = data.getLong(data.getColumnIndex(PaymentsProvider.Columns.COST));

                //fill up months with data
                if (categoryIdKeeper == -1) {
                    categoryIdKeeper = categoryId;
                    categoryNameKeeper = categoryName;
                    entries = new ArrayList<>();
                    entries.add(new Entry(xAxis, (float) cost, DateUtils.months.get(month) + " " + year));
                    xAxis++;
                } else {
                    if (categoryIdKeeper == categoryId) {
                        entries.add(new Entry(xAxis, (float) cost, DateUtils.months.get(month) + " " + year));
                        xAxis++;
                    } else {
                        //add entries into months list
                        LineDataSet set = new LineDataSet(entries, categoryNameKeeper);
                        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
                        dataSets.add(set);
                        LineData barData = new LineData(dataSets);
                        categories.add(barData);
                        xAxis = 0;
                        //create new entries
                        categoryIdKeeper = categoryId;
                        categoryNameKeeper = categoryName;
                        entries = new ArrayList<>();
                        entries.add(new Entry(xAxis, cost, DateUtils.months.get(month) + " " + year));
                        xAxis++;
                    }
                }
                data.moveToNext();
            }
            LineDataSet set = new LineDataSet(entries, categoryNameKeeper);
            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
            dataSets.add(set);
            LineData barData = new LineData(dataSets);
            categories.add(barData);

            mStatisticByCategoriesCallback.getDataFromStatisticByCategoriesLoaderCallback(categories);
        }else {
            mEmptyCursorCallback.showNoDataAnnouncement();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
