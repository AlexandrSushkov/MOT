package dev.nelson.mot.legacy.service.action;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import dev.nelson.mot.legacy.db.model.CategoriesProvider;

public class DeleteCategoryAction implements DataOperationAction {

    private Context mContext;
    public DeleteCategoryAction(Context context) {
        mContext = context;
    }

    @Override
    public void perform(Bundle bundle) {
        int categoryId = bundle.getInt(CategoriesProvider.Columns._ID, -1);
        Uri uri = Uri.withAppendedPath(CategoriesProvider.URI, String.valueOf(categoryId));
        mContext.getContentResolver().delete(uri, null, null);
    }
}
