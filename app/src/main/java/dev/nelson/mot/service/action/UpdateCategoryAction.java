package dev.nelson.mot.service.action;

import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;

import dev.nelson.mot.db.model.CategoriesProvider;
import dev.nelson.mot.utils.MyApplication;
import dev.nelson.mot.utils.SqlUtils;

/**
 * Created by Nelson on 2/9/17.
 */

public class UpdateCategoryAction implements DataOperationAction {
    @Override
    public void perform(Bundle bundle) {
        Uri uri = Uri.parse(bundle.getString(SqlUtils.URI_KEY));
        String newName = bundle.getString(CategoriesProvider.Columns.CATEGORY_NAME);
        ContentValues cv = new ContentValues();
        cv.put(CategoriesProvider.Columns.CATEGORY_NAME, newName);
        MyApplication.getContext().getContentResolver().update(uri, cv, null, null);
    }
}
