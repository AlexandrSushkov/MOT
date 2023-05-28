package dev.nelson.mot.legacy.service.action;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import dev.nelson.mot.legacy.db.model.CategoriesProvider;
import dev.nelson.mot.legacy.utils.Constants;
import dev.nelson.mot.legacy.utils.StringUtils;

public class UpdateCategoryAction implements DataOperationAction {

    private Context mContext;
    public UpdateCategoryAction(Context context) {
        mContext = context;
    }

    @Override
    public void perform(Bundle bundle) {
        int categoryId = bundle.getInt(Constants.ID_KEY, -1);
        String newName = bundle.getString(CategoriesProvider.Columns.CATEGORY_NAME);
        Uri uri = Uri.withAppendedPath(CategoriesProvider.URI, String.valueOf(categoryId));
        ContentValues cv = new ContentValues();
        cv.put(CategoriesProvider.Columns.CATEGORY_NAME, StringUtils.capitalizeFirstCharacter(newName));
        mContext.getContentResolver().update(uri, cv, null, null);
    }
}
