package dev.nelson.mot.legacy.db.model;

import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import dev.nelson.mot.legacy.utils.Constants;

public class MultipleTablesProvider extends SQLiteTableProvider {

    public static final String MULTIPLE_TABLES = PaymentsProvider.TABLE_NAME + ", " + CategoriesProvider.TABLE_NAME;
    public static final Uri URI = Uri.parse(Constants.SQL_AUTHORITY + "/" + MULTIPLE_TABLES);

    public MultipleTablesProvider() {
        super(MULTIPLE_TABLES);
    }

    @Override
    public Uri getBaseUri() {
        return null;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }
}
