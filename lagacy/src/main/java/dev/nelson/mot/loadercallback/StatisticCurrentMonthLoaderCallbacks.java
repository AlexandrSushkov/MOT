package dev.nelson.mot.loadercallback;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;

import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;

import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import dev.nelson.mot.R;
import dev.nelson.mot.callback.EmptyCursorCallback;
import dev.nelson.mot.callback.StatisticCurrentMonthCallback;
import dev.nelson.mot.db.model.CategoriesProvider;
import dev.nelson.mot.db.model.PaymentsProvider;
import dev.nelson.mot.loader.RawQueryCursorLoader;
import dev.nelson.mot.utils.DateUtils;
import dev.nelson.mot.utils.StringUtils;


public class StatisticCurrentMonthLoaderCallbacks implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final int LOADER_ID = 30;

    private Context mContext;
    private StatisticCurrentMonthCallback mStatisticCurrentMonthCallback;
    private EmptyCursorCallback mEmptyCursorCallback;

    public StatisticCurrentMonthLoaderCallbacks(Context context, StatisticCurrentMonthCallback statisticCurrentMonthCallback, EmptyCursorCallback emptyCursorCallback) {
        mContext = context;
        mStatisticCurrentMonthCallback = statisticCurrentMonthCallback;
        this.mEmptyCursorCallback = emptyCursorCallback;

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
            PieDataSet dataSet = new PieDataSet(entries, StringUtils.formattedCost(mContext, totalCost));
            PieData pieData = new PieData(dataSet);

            mStatisticCurrentMonthCallback.setDataFromStatisticCurrentMonthLoaderCallbacks(pieData);
        }else {
            mEmptyCursorCallback.showNoDataAnnouncement();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        loader = null;
    }
}
