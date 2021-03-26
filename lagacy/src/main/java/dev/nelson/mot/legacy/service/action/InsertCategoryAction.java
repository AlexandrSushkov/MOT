package dev.nelson.mot.legacy.service.action;

import android.content.ContentValues;
import android.content.Context;
import android.os.Bundle;

import dev.nelson.mot.legacy.db.model.CategoriesProvider;
import dev.nelson.mot.legacy.utils.StringUtils;

class InsertCategoryAction implements DataOperationAction{

    private Context mContext;
    public InsertCategoryAction(Context context) {
        mContext = context;
    }

    @Override
    public void perform(Bundle bundle) {
        String categoryName = bundle.getString(CategoriesProvider.Columns.CATEGORY_NAME);
        ContentValues contentValues = new ContentValues();
        contentValues.put(CategoriesProvider.Columns.CATEGORY_NAME, StringUtils.capitalizeFirstCharacter(categoryName));
        mContext.getContentResolver().insert(CategoriesProvider.URI, contentValues);
    }
}
