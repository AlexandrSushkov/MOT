package dev.nelson.mot.activity;

import android.content.ContentValues;
import android.content.Intent;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import java.io.File;

import dev.nelson.mot.R;
import dev.nelson.mot.db.SQLiteOpenHelperImpl;
import dev.nelson.mot.db.model.CategoriesProvider;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        File dbFile = new File(getApplicationInfo().dataDir + "/databases/" + SQLiteOpenHelperImpl.DATABASE_NAME);
        if(!dbFile.exists()){
            SQLiteOpenHelperImpl helper = new SQLiteOpenHelperImpl(this);
            SQLiteDatabase db = helper.getReadableDatabase();
            Resources resources = this.getResources();
            String[] buildInCategories = resources.getStringArray(R.array.build_in_categories);
            ContentValues cv = new ContentValues();
            for (String category : buildInCategories) {
                cv.put(CategoriesProvider.Columns.CATEGORY_NAME, category);
                db.insert(CategoriesProvider.TABLE_NAME, null, cv);
            }
            helper.close();
        }

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }


}
