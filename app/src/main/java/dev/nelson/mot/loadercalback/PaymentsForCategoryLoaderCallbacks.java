package dev.nelson.mot.loadercalback;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import dev.nelson.mot.adapter.PaymentsAdapter;
import dev.nelson.mot.callback.EmptyCursorCallback;
import dev.nelson.mot.db.model.PaymentsProvider;


public class PaymentsForCategoryLoaderCallbacks implements LoaderManager.LoaderCallbacks<Cursor> {
    public static final int LOADER_ID = 12;

    private Context mContext;
    private PaymentsAdapter mAdapter;
    private int mCategoryId;
    private EmptyCursorCallback mEmptyCursorCallback;

    public PaymentsForCategoryLoaderCallbacks(Context context, PaymentsAdapter adapter, int categoryId, EmptyCursorCallback callback) {
        mContext = context;
        mAdapter = adapter;
        mCategoryId = categoryId;
        mEmptyCursorCallback = callback;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (id == LOADER_ID){
            String[] projection1 = {
                    PaymentsProvider.TABLE_NAME + "." + PaymentsProvider.Columns._ID,
                    PaymentsProvider.TABLE_NAME + "." + PaymentsProvider.Columns.TITLE,
                    PaymentsProvider.TABLE_NAME + "." + PaymentsProvider.Columns.COST,
                    PaymentsProvider.TABLE_NAME + "." + PaymentsProvider.Columns.DATE,
                    PaymentsProvider.TABLE_NAME + "." + PaymentsProvider.Columns.SUMMARY,
            };
            String selection1 = PaymentsProvider.TABLE_NAME + "." + PaymentsProvider.Columns.CATEGORY_ID + "=?";
            String[] selectionArgs1 = {String.valueOf(mCategoryId)};
            return new CursorLoader(mContext, PaymentsProvider.URI, projection1, selection1, selectionArgs1, null);
        }else {
            throw new IllegalArgumentException(getClass().getName() + " Wrong loader id: " + id);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
        mAdapter.notifyDataSetChanged();
        if (!data.moveToFirst()){
            mEmptyCursorCallback.showNoDataAnnouncement();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
        mAdapter.notifyDataSetChanged();
    }
}
