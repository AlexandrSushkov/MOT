package dev.nelson.mot.service.action;

import android.content.ContentValues;
import android.os.Bundle;

import dev.nelson.mot.db.model.CategoriesProvider;
import dev.nelson.mot.utils.MyApplication;

class InsertCategoryAction implements DataOperationAction{

    @Override
    public void perform(Bundle bundle) {
        String categoryName = bundle.getString(CategoriesProvider.Columns.CATEGORY_NAME);
        ContentValues contentValues = new ContentValues();
        contentValues.put(CategoriesProvider.Columns.CATEGORY_NAME, categoryName);
        MyApplication.getContext().getContentResolver().insert(CategoriesProvider.URI, contentValues);
    }
}
