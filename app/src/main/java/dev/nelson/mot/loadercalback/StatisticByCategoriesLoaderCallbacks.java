package dev.nelson.mot.loadercalback;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;

import dev.nelson.mot.R;
import dev.nelson.mot.callback.StatisticByCategoriesCallback;
import dev.nelson.mot.db.model.CategoriesProvider;
import dev.nelson.mot.db.model.PaymentsProvider;
import dev.nelson.mot.loader.RawQueryCursorLoader;
import dev.nelson.mot.utils.DateUtils;
import dev.nelson.mot.utils.LocaleUtils;
import dev.nelson.mot.utils.StringUtils;

import static android.R.attr.commitIcon;
import static android.R.attr.contextClickable;
import static android.R.attr.id;

public class StatisticByCategoriesLoaderCallbacks implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final int LOADER_ID = 34;

    private Context mContext;
    private StatisticByCategoriesCallback mCallback;

    public StatisticByCategoriesLoaderCallbacks(Context context, StatisticByCategoriesCallback callback) {
        mContext = context;
        mCallback = callback;
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
                    + "strftime('%Y', " + PaymentsProvider.TABLE_NAME + "." + PaymentsProvider.Columns.DATE + ") AS year, "
                    + "strftime('%m', " + PaymentsProvider.TABLE_NAME + "." + PaymentsProvider.Columns.DATE + ") AS month, "
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
            long totalCost = 0;

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
                year = data.getInt(data.getColumnIndex("year"));
                month = data.getString(data.getColumnIndex("month"));
                cost = data.getLong(data.getColumnIndex(PaymentsProvider.Columns.COST));

                //fill up months with data
                if (categoryIdKeeper == -1) {
                    categoryIdKeeper = categoryId;
                    categoryNameKeeper = categoryName;
                    entries = new ArrayList<>();
                    entries.add(new Entry(xAxis, (float) cost, DateUtils.months.get(month) + " " + year));
                    totalCost += cost;
                    xAxis++;
                } else {
                    if (categoryIdKeeper == categoryId) {
                        entries.add(new Entry(xAxis, (float) cost, DateUtils.months.get(month) + " " + year));
                        totalCost += cost;
                        xAxis++;
                    } else {
                        //add entries into months list
                        LineDataSet set = new LineDataSet(entries, categoryNameKeeper + " Total: " + StringUtils.formattedCost(totalCost));
                        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
                        dataSets.add(set);
                        LineData barData = new LineData(dataSets);
                        categories.add(barData);
                        totalCost = 0;
                        xAxis = 0;
                        //create new entries
                        categoryIdKeeper = categoryId;
                        categoryNameKeeper = categoryName;
                        entries = new ArrayList<>();
                        entries.add(new Entry(xAxis, cost, DateUtils.months.get(month) + " " + year));
                        totalCost += cost;
                        xAxis++;
                    }
                }
                data.moveToNext();
            }
            LineDataSet set = new LineDataSet(entries, categoryNameKeeper + "   Total: " + StringUtils.formattedCost(totalCost));
            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
            dataSets.add(set);
            LineData barData = new LineData(dataSets);
            categories.add(barData);

            mCallback.getDataFromStatisticByCategoriesLoaderCallback(categories);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
