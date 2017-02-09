package dev.nelson.mot.db.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public abstract class SQLiteTableProvider {
    protected final String mName;

    public SQLiteTableProvider(String name) {
        mName = name;
    }

    public abstract Uri getBaseUri();

    public abstract void onCreate(SQLiteDatabase db);

    public Cursor query(SQLiteDatabase db, String[] columns, String where, String[] whereArgs, String orderBy){
        return db.query(mName, columns, where, whereArgs, null, null, orderBy);
    }

    public long insert(SQLiteDatabase db, ContentValues contentValues){
        return db.insertWithOnConflict(mName, null, contentValues, SQLiteDatabase.CONFLICT_REPLACE);
    }

    public int delete(SQLiteDatabase db, String where, String[] whereArgs){
        return db.delete(mName, where, whereArgs);
    }

    public int update(SQLiteDatabase db, ContentValues contentValues, String where, String[] whereArgs){
        return db.update(mName, contentValues, where, whereArgs);
    }

    public void onUpdate(SQLiteDatabase db){
        db.execSQL("DROP TABLE IF EXISTS " + mName + ";");
        onCreate(db);
    }
}

