package dev.nelson.mot.loadercalback;


import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;

import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;

import dev.nelson.mot.R;
import dev.nelson.mot.callback.SetDataFromStatisticYearLoaderCallbacks;
import dev.nelson.mot.db.model.CategoriesProvider;
import dev.nelson.mot.db.model.PaymentsProvider;
import dev.nelson.mot.loader.RawQueryCursorLoader;
import dev.nelson.mot.utils.DateUtils;

public class StatisticYearLoaderCallbacks implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final int LOADER_ID = 31;
    private Context mContext;
    private SetDataFromStatisticYearLoaderCallbacks mCallback;

    public StatisticYearLoaderCallbacks(Context context, SetDataFromStatisticYearLoaderCallbacks callback) {
        mContext = context;
        mCallback = callback;
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        if (id == LOADER_ID) {
            String rawQuery = "select categories.category_name, sum(payments.cost) as cost " +
                    "from payments " +
                    "left join categories on payments.category_id = categories._id " +
                    "where date between '2017-03-01' and '2017-03-30' " +
                    "group by categories.category_name";
            return new RawQueryCursorLoader(mContext, rawQuery, null);
        } else {
            throw new IllegalArgumentException(getClass().getName() + " Wrong loader id: " + id);
        }
    }

    @Override
    public void onLoadFinished(Loader loader, Cursor data) {
        float x = 1;
        if(data != null){
            ArrayList<String> categoriesNames = new ArrayList<>();
            ArrayList<BarEntry> enries = new ArrayList<>();
            String categoryName;
            while (data.moveToNext()){
                categoryName = data.getString(data.getColumnIndex(CategoriesProvider.Columns.CATEGORY_NAME));
                if (categoryName != null){
                    categoriesNames.add(categoryName);
                }else {
                    categoriesNames.add("No category");
                }
                enries.add(new BarEntry(x, data.getFloat(data.getColumnIndex(PaymentsProvider.Columns.COST)) / 100));
                x++;
            }
            mCallback.setDataFromStatisticLoaderCallbacks(categoriesNames, enries);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        loader = null;
    }


    //        SELECT categories._id,
//        categories.category_name,
//                strftime('%Y',payments.date) AS year,
//        strftime('%m',payments.date) AS month,
//        sum(payments.cost) AS cost
//        FROM payments
//        LEFT JOIN
//        categories ON payments.category_id = categories._id
//        GROUP BY strftime('%m', payments.date), strftime('%Y', payments.date), categories._id
//        ORDER BY year, month DESC;

}
