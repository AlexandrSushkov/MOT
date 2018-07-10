package dev.nelson.mot.service.action;

import android.net.Uri;
import android.os.Bundle;

import dev.nelson.mot.db.model.CategoriesProvider;
import dev.nelson.mot.utils.MyApplication;

public class DeleteCategoryAction implements DataOperationAction {
    @Override
    public void perform(Bundle bundle) {
        int categoryId = bundle.getInt(CategoriesProvider.Columns._ID, -1);
        Uri uri = Uri.withAppendedPath(CategoriesProvider.URI, String.valueOf(categoryId));
        MyApplication.getContext().getContentResolver().delete(uri, null, null);
    }
}
