package dev.nelson.mot.loader;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.loader.content.AsyncTaskLoader;
import dev.nelson.mot.db.SQLiteOpenHelperImpl;
import dev.nelson.mot.db.model.PaymentsProvider;

public class RawQueryCursorLoader extends AsyncTaskLoader<Cursor> {

    private Context mContext;
    private Cursor mCursor;
    private String mRawQuery;
    private String[] mSelectionArgs;

    public RawQueryCursorLoader(Context context, String rawQuery, String[] selectionArgs) {
        super(context);
        mContext = context;
        mRawQuery = rawQuery;
        mSelectionArgs = selectionArgs;
    }

    @Override
    public Cursor loadInBackground() {
        SQLiteOpenHelperImpl helper = new SQLiteOpenHelperImpl(mContext);
        SQLiteDatabase db = helper.getReadableDatabase();
        PaymentsProvider provider = new PaymentsProvider();
        mCursor = provider.rawQuery(db, mRawQuery, mSelectionArgs);
        return mCursor;
    }

    @Override
    public void deliverResult(Cursor cursor) {
        if (isReset()) {
            // An async query came in while the loader is stopped
            if (cursor != null) {
                cursor.close();
            }
            return;
        }
        Cursor oldCursor = mCursor;
        mCursor = cursor;

        if (isStarted()) {
            super.deliverResult(cursor);
        }

        if (oldCursor != null && oldCursor != cursor && !oldCursor.isClosed()) {
            oldCursor.close();
        }
    }

    @Override
    protected void onStartLoading() {
        if (mCursor != null) {
            deliverResult(mCursor);
        }
        if (takeContentChanged() || mCursor == null) {
            forceLoad();
        }
    }

    /**
     * Must be called from the UI thread
     */
    @Override
    protected void onStopLoading() {
        // Attempt to cancel the current load task if possible.
        cancelLoad();
    }

    @Override
    public void onCanceled(Cursor cursor) {
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
    }

    @Override
    protected void onReset() {
        super.onReset();

        // Ensure the loader is stopped
        onStopLoading();

        if (mCursor != null && !mCursor.isClosed()) {
            mCursor.close();
        }
        mCursor = null;
    }
}
