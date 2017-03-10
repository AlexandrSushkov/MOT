package dev.nelson.mot.observer;

import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;

import dev.nelson.mot.callback.DatabaseChangesCallback;

public class DatabaseChangesObserver extends ContentObserver {

    private DatabaseChangesCallback mDatabaseChangesCallback;
    /**
     * Creates a content observer.
     *
     * @param handler The handler to run {@link #onChange} on, or null if none.
     */
    public DatabaseChangesObserver(Handler handler) {
        super(handler);
    }

    public DatabaseChangesObserver(Handler handler, DatabaseChangesCallback callback) {
        super(handler);
        mDatabaseChangesCallback = callback;
    }

    @Override
    public void onChange(boolean selfChange) {
        super.onChange(selfChange);
    }

    @Override
    public void onChange(boolean selfChange, Uri uri) {
        super.onChange(selfChange, uri);
        String lastPathSegment = uri.getLastPathSegment();
        if (lastPathSegment.equals("updated") || lastPathSegment.equals("deleted")){
            mDatabaseChangesCallback.updateDataFromDB();
        }else {
            mDatabaseChangesCallback.lastInsertedRow(Integer.valueOf(lastPathSegment));
        }
        Log.d("tag", "===================> Last path segment: " + uri.getLastPathSegment());
    }
}
