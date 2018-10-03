package dev.nelson.mot.legacy.observer;

import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;

import dev.nelson.mot.legacy.callback.DatabaseChangesCallback;

public class DatabaseChangesObserver extends ContentObserver {

    private DatabaseChangesCallback mDatabaseChangesCallback;

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
        int lastInsertedRow;
        try {
            lastInsertedRow = Integer.valueOf(uri.getLastPathSegment());
        } catch (NumberFormatException e) {
            lastInsertedRow = -1;
        }
        mDatabaseChangesCallback.dataBaseChanged(lastInsertedRow);
        Log.d("tag", "===================> Last path segment: " + uri.getLastPathSegment());
    }
}
