package dev.nelson.mot.utils;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import dev.nelson.mot.db.SQLiteOpenHelperImpl;
import dev.nelson.mot.db.model.PaymentsProvider;

public class DataBaseUtils {

    public static int getLastInsertedId(Context context){
        SQLiteOpenHelperImpl helper = new SQLiteOpenHelperImpl(context);
        SQLiteDatabase db = helper.getReadableDatabase();

//        select _id from payments order by _id desc limit 1
        String rawQuery = "select " + PaymentsProvider.Columns._ID + " from " + PaymentsProvider.TABLE_NAME
                + " order by " + PaymentsProvider.Columns._ID + " desc limit 1";
        Cursor cursor = db.rawQuery(rawQuery, null);
        int lastInsertedId = 0;
        if(cursor != null){
            cursor.moveToFirst();
            lastInsertedId = cursor.getInt(cursor.getColumnIndex(PaymentsProvider.Columns._ID));
            cursor.close();
        }
        return lastInsertedId;
    }
}
