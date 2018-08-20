package dev.nelson.mot.loadercallback;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;

import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import java.util.ArrayList;

import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import dev.nelson.mot.R;
import dev.nelson.mot.callback.EmptyCursorCallback;
import dev.nelson.mot.callback.StatisticByMonthWithCategoriesCallback;
import dev.nelson.mot.db.model.CategoriesProvider;
import dev.nelson.mot.db.model.PaymentsProvider;
import dev.nelson.mot.loader.RawQueryCursorLoader;
import dev.nelson.mot.utils.Constants;
import dev.nelson.mot.utils.DateUtils;

public class StatisticByMonthWithCategoriesLoaderCallbacks implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final int LOADER_ID = 32;
    private Context mContext;
    private StatisticByMonthWithCategoriesCallback mStatisticByMonthWithCategoriesCallback;
    private EmptyCursorCallback mEmptyCursorCallback;

    public StatisticByMonthWithCategoriesLoaderCallbacks(Context context, StatisticByMonthWithCategoriesCallback statisticByMonthWithCategoriesCallback, EmptyCursorCallback emptyCursorCallback) {
        mContext = context;
        mStatisticByMonthWithCategoriesCallback = statisticByMonthWithCategoriesCallback;
        mEmptyCursorCallback = emptyCursorCallback;
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        if (id == LOADER_ID) {
//            SELECT categories._id, categories.category_name, strftime('%Y', payments.date) AS year, strftime('%m', payments.date) AS month, sum(payments.cost) AS cost
//            FROM payments
//            LEFT JOIN
//            categories ON payments.category_id = categories._id
//            GROUP BY strftime('%Y', payments.date), strftime('%m', payments.date), categories._id
//            ORDER BY  strftime('%Y', payments.date) DESC, strftime('%m', payments.date) DESC
            String rawQuery =
                    "SELECT " + CategoriesProvider.TABLE_NAME + "." +CategoriesProvider.Columns._ID + ", "
                        + CategoriesProvider.TABLE_NAME + "." + CategoriesProvider.Columns.CATEGORY_NAME + ", "
                        + "strftime('%Y', " + PaymentsProvider.TABLE_NAME + "." + PaymentsProvider.Columns.DATE + ") AS " + Constants.YEAR + ", "
                        + "strftime('%m', " + PaymentsProvider.TABLE_NAME + "." + PaymentsProvider.Columns.DATE + ") AS " + Constants.MONTH + ", "
                        + "sum(" + PaymentsProvider.TABLE_NAME + "." + PaymentsProvider.Columns.COST + ")" + " AS " + PaymentsProvider.Columns.COST
                    + " FROM " + PaymentsProvider.TABLE_NAME
                    + " LEFT JOIN " + CategoriesProvider.TABLE_NAME
                    + " ON " + PaymentsProvider.TABLE_NAME + "." + PaymentsProvider.Columns.CATEGORY_ID + "=" + CategoriesProvider.TABLE_NAME + "." + CategoriesProvider.Columns._ID
                    + " GROUP BY " + "strftime('%Y'," + PaymentsProvider.TABLE_NAME + "." + PaymentsProvider.Columns.DATE + "), "
                                  + "strftime('%m'," + PaymentsProvider.TABLE_NAME + "." + PaymentsProvider.Columns.DATE + "), "
                                  + CategoriesProvider.TABLE_NAME + "." +CategoriesProvider.Columns._ID
                    + " ORDER BY " + "strftime('%Y'," + PaymentsProvider.TABLE_NAME + "." + PaymentsProvider.Columns.DATE + ") DESC, "
                                  + "strftime('%m'," + PaymentsProvider.TABLE_NAME + "." + PaymentsProvider.Columns.DATE + ") DESC";

            return new RawQueryCursorLoader(mContext, rawQuery, null);
        } else {
            throw new IllegalArgumentException(getClass().getName() + " Wrong loader id: " + id);
        }
    }

    @Override
    public void onLoadFinished(Loader loader, Cursor data) {
        if(data != null && data.moveToFirst()){
            //initial data
            float xAxis = 0;
            int yearKeeper = 0;
            String monthKeeper = "";

            ArrayList<BarData> months = new ArrayList<>();
            ArrayList<BarEntry> entries = null;
            String categoryName;
            int year = -1;
            String month;
            long cost;
            while (!data.isAfterLast()){
                //get data from cursor
                if(data.getString(data.getColumnIndex(CategoriesProvider.Columns.CATEGORY_NAME)) == null){
                    categoryName = mContext.getString(R.string.no_category_category_name);
                }else {
                    categoryName = data.getString(data.getColumnIndex(CategoriesProvider.Columns.CATEGORY_NAME));
                }
                year = data.getInt(data.getColumnIndex(Constants.YEAR));
                month = data.getString(data.getColumnIndex(Constants.MONTH));
                cost = data.getLong(data.getColumnIndex(PaymentsProvider.Columns.COST));

                //fill up months with data
                if(yearKeeper == 0 && monthKeeper.equals("")){
                    yearKeeper = year;
                    monthKeeper = month;
                    entries = new ArrayList<>();
                    entries.add(new BarEntry(xAxis, (float)cost, categoryName));
                    xAxis++;
                }else {
                    if (yearKeeper == year && monthKeeper.equals(month)){
                        entries.add(new BarEntry(xAxis, (float)cost, categoryName));
                        xAxis++;
                    }else {
                        //add entries into months list
                        BarDataSet set = new BarDataSet(entries, DateUtils.months.get(monthKeeper) + " " + yearKeeper);
                        ArrayList<IBarDataSet> dataSets = new ArrayList<>();
                        dataSets.add(set);
                        BarData barData = new BarData(dataSets);
                        months.add(barData);
                        xAxis = 0;
                        //create new entries
                        yearKeeper = year;
                        monthKeeper = month;
                        entries = new ArrayList<>();
                        entries.add(new BarEntry(xAxis, cost, categoryName));
                        xAxis++;
                    }
                }
                data.moveToNext();
            }
                BarDataSet set = new BarDataSet(entries, DateUtils.months.get(monthKeeper) + " " + yearKeeper);
                ArrayList<IBarDataSet> dataSets = new ArrayList<>();
                dataSets.add(set);
                BarData barData = new BarData(dataSets);
                months.add(barData);

            mStatisticByMonthWithCategoriesCallback.getDataFromStatisticLoaderCallbacks(months);
        }else {
            mEmptyCursorCallback.showNoDataAnnouncement();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        loader = null;
    }

}
