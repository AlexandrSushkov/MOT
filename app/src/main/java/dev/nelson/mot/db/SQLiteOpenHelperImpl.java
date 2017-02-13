package dev.nelson.mot.db;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import dev.nelson.mot.R;
import dev.nelson.mot.db.model.CategoriesProvider;
import dev.nelson.mot.db.model.SQLiteTableProvider;
import dev.nelson.mot.service.DataOperationService;
import dev.nelson.mot.service.action.DataOperationFabric;
import dev.nelson.mot.utils.MyApplication;

public class SQLiteOpenHelperImpl extends SQLiteOpenHelper {
    
    public static final String DATABASE_NAME = "mot.db";
    private static final int DATABASE_VERSION = 1;
    private Context mContext;
    
    public SQLiteOpenHelperImpl(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);;
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.beginTransactionNonExclusive();
        try{
            for (SQLiteTableProvider provider : SQLiteContentProvider.SCHEMA.values()) {
                provider.onCreate(db);
            }
            db.setTransactionSuccessful();
        }finally {
            db.endTransaction();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.beginTransactionNonExclusive();
        try{
            for (SQLiteTableProvider provider : SQLiteContentProvider.SCHEMA.values()) {
                provider.onUpdate(db);
            }
            db.endTransaction();
        }finally {
            db.setTransactionSuccessful();
        }
    }


}
