package dev.nelson.mot.service.action;

import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;

import dev.nelson.mot.db.model.CategoriesProvider;
import dev.nelson.mot.utils.MyApplication;
import dev.nelson.mot.utils.Constants;
import dev.nelson.mot.utils.StringUtils;

public class UpdateCategoryAction implements DataOperationAction {
    @Override
    public void perform(Bundle bundle) {
        int categoryId = bundle.getInt(Constants.ID_KEY, -1);
        String newName = bundle.getString(CategoriesProvider.Columns.CATEGORY_NAME);
        Uri uri = Uri.withAppendedPath(CategoriesProvider.URI, String.valueOf(categoryId));
        ContentValues cv = new ContentValues();
        cv.put(CategoriesProvider.Columns.CATEGORY_NAME, StringUtils.capitalizeFirstCharacter(newName));
        MyApplication.getContext().getContentResolver().update(uri, cv, null, null);
    }
}
