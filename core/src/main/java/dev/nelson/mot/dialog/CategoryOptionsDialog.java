package dev.nelson.mot.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import dev.nelson.mot.R;
import dev.nelson.mot.db.model.CategoriesProvider;
import dev.nelson.mot.service.DataOperationService;
import dev.nelson.mot.service.action.DataOperationFabric;


public class CategoryOptionsDialog extends DialogFragment {

    public static final String CATEGORY_ID_KEY = "category_id";

    private int categoryId;

    public static CategoryOptionsDialog newInstance(int id) {
        Bundle args = new Bundle();
        CategoryOptionsDialog fragment = new CategoryOptionsDialog();
        args.putInt(CATEGORY_ID_KEY, id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        categoryId = getArguments().getInt(CATEGORY_ID_KEY, -1);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.dialog_category_item_menu_title);
        builder.setItems(R.array.category_options_dialog, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case 0:
                        //rename category
                        CategoryDialog.newInstance(CategoryDialog.ACTION_EDIT, categoryId).show(getActivity().getSupportFragmentManager(), "tag");
                        break;
                    case 1:
                        //delete category
                        Intent intent = new Intent(getContext(), DataOperationService.class);
                        intent.setAction(DataOperationFabric.DELETE_CATEGORY);
                        intent.putExtra(CategoriesProvider.Columns._ID, categoryId);
                        getContext().startService(intent);
                        break;
                }
            }
        });
        return builder.create();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(CATEGORY_ID_KEY, categoryId);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null){
            categoryId = savedInstanceState.getInt(CATEGORY_ID_KEY);
        }
    }
}
