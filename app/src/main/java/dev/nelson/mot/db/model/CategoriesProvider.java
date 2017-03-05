package dev.nelson.mot.db.model;

import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.BaseColumns;

import dev.nelson.mot.utils.Constants;

public class CategoriesProvider extends SQLiteTableProvider {
    public static final String TABLE_NAME = "categories";

    public static final Uri URI = Uri.parse(Constants.SQL_AUTHORITY + "/" + TABLE_NAME);

    public CategoriesProvider() {
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
                + Columns.CATEGORY_NAME + " varchar(30)"
                +")");

    }

    public interface Columns extends BaseColumns{
        String CATEGORY_NAME = "category_name";
    }
}
