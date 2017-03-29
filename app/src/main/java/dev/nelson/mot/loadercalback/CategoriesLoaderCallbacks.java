package dev.nelson.mot.loadercalback;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import dev.nelson.mot.adapter.CategoriesAdapter;
import dev.nelson.mot.db.model.CategoriesProvider;

public class CategoriesLoaderCallbacks implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final int LOADER_ID = 20;

    private CategoriesAdapter mAdapter;
    private Context mContext;

    //constructor for loader for categories fragment, and choose category activity
    public CategoriesLoaderCallbacks(Context context, CategoriesAdapter adapter) {
        mAdapter = adapter;
        mContext = context;
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        if (id == LOADER_ID) {
            return new CursorLoader(mContext, CategoriesProvider.URI, null, null, null, null);
        } else {
            throw new IllegalArgumentException(getClass().getName() + "Wrong loader id: " + id);
        }
    }

    @Override
    public void onLoadFinished(Loader loader, Cursor data) {
            mAdapter.swapCursor(data);
            mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader loader) {
        mAdapter.swapCursor(null);
        mAdapter.notifyDataSetChanged();
    }
}
