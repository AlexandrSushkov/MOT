package dev.nelson.mot.service.action;

import android.net.Uri;
import android.os.Bundle;

import dev.nelson.mot.utils.MyApplication;
import dev.nelson.mot.utils.SqlUtils;

/**
 * Created by Nelson on 2/9/17.
 */

public class DeleteCategoryAction implements DataOperationAction {
    @Override
    public void perform(Bundle bundle) {
        Uri uri = Uri.parse(bundle.getString(SqlUtils.URI_KEY));
        MyApplication.getContext().getContentResolver().delete(uri, null, null);
    }
}
