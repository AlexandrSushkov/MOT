package dev.nelson.mot.loadercallback;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
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

    @NonNull
    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        if (id == LOADER_ID) {
            return new CursorLoader(mContext, CategoriesProvider.URI, null, null, null, null);
        } else {
            throw new IllegalArgumentException(getClass().getName() + "Wrong loader id: " + id);
        }
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
        mAdapter.notifyDataSetChanged();
    }
}
