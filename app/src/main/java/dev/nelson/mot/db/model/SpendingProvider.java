package dev.nelson.mot.db.model;

import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.BaseColumns;

import dev.nelson.mot.utils.SqlUtils;

/**
 * Created by Nelson on 2/8/17.
 */

public class SpendingProvider extends SQLiteTableProvider {
    public static final String TABLE_NAME = "spending";

    public static final Uri URI = Uri.parse(SqlUtils.SQL_AUTHORITY + "/" + TABLE_NAME);

    public SpendingProvider() {
        super(TABLE_NAME);
    }

    @Override
    public Uri getBaseUri() {
        return URI;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME
            +"(" + Columns._ID + " INTEGER PRIMARY KEY, "
            + Columns.TITLE + " varchar(255), "
            + Columns.SUMMARY + " varchar(500), "
            + Columns.CATEGORY_ID + " integer, "
            + Columns.DATE + " date, "
            + Columns.COST + " double"
            + ")");

    }

    public interface Columns extends BaseColumns{
        String TITLE = "title";
        String SUMMARY = "summary";
        String CATEGORY_ID = "category_id";
        String DATE = "date";
        String COST = "cost";
    }
}
