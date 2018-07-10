package dev.nelson.mot.loadercallback;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import dev.nelson.mot.adapter.PaymentsAdapter;
import dev.nelson.mot.callback.EmptyCursorCallback;
import dev.nelson.mot.db.model.CategoriesProvider;
import dev.nelson.mot.db.model.PaymentsProvider;
import dev.nelson.mot.loader.RawQueryCursorLoader;

public class RecentPaymentsLoadersCallbacks implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final int LOADER_ID = 10;

    private Context mContext;
    private PaymentsAdapter mAdapter;
    private EmptyCursorCallback mEmptyCursorCallback;


    public RecentPaymentsLoadersCallbacks(Context context, PaymentsAdapter adapter, EmptyCursorCallback callback) {
        mContext = context;
        mAdapter = adapter;
        mEmptyCursorCallback = callback;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

//        select payments._id, payments.title, payments.date, categories.category_name, payments.cost
//        from payments
//        left join categories on payments.category_id = categories._id
//        order by payments._id desc limit 20

        String rawQuery = "select " + PaymentsProvider.TABLE_NAME + "." + PaymentsProvider.Columns._ID + ", "
                + PaymentsProvider.TABLE_NAME + "." + PaymentsProvider.Columns.TITLE + ", "
                + PaymentsProvider.TABLE_NAME + "." + PaymentsProvider.Columns.DATE + ", "
                + CategoriesProvider.TABLE_NAME + "." + CategoriesProvider.Columns.CATEGORY_NAME + ", "
                + PaymentsProvider.TABLE_NAME + "." + PaymentsProvider.Columns.COST
                + " from " + PaymentsProvider.TABLE_NAME
                + " left join " + CategoriesProvider.TABLE_NAME
                + " on " + PaymentsProvider.TABLE_NAME + "." + PaymentsProvider.Columns.CATEGORY_ID
                + "=" + CategoriesProvider.TABLE_NAME + "." + CategoriesProvider.Columns._ID
                + " order by " + PaymentsProvider.TABLE_NAME + "." + PaymentsProvider.Columns._ID + " desc limit 20";

        return new RawQueryCursorLoader(mContext, rawQuery, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
        mAdapter.notifyDataSetChanged();
        if(!data.moveToFirst()){
            mEmptyCursorCallback.showNoDataAnnouncement();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
        mAdapter.notifyDataSetChanged();
    }
}
