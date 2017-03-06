package dev.nelson.mot.db.model;

import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.BaseColumns;

import dev.nelson.mot.R;
import dev.nelson.mot.utils.Constants;

public class PaymentsProvider extends SQLiteTableProvider {
    public static final String TABLE_NAME = "payments";

    public static final Uri URI = Uri.parse(Constants.SQL_AUTHORITY + "/" + TABLE_NAME);

    public PaymentsProvider() {
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
            + Columns.TITLE + " VARCHAR("+ R.string.payment_title_max_length +"), "
            + Columns.SUMMARY + " VARCHAR("+ R.string. payment_summary_max_length +"), "
            + Columns.CATEGORY_ID + " INTEGER REFERENCES " + CategoriesProvider.TABLE_NAME + "(" + CategoriesProvider.Columns._ID + "), "
            + Columns.DATE + " DATE DEFAULT CURRENT_DATE, "
            + Columns.COST + " LONG"
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
