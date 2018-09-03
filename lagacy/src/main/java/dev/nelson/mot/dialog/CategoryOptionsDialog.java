package dev.nelson.mot.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import dev.nelson.mot.R;
import dev.nelson.mot.db.model.CategoriesProvider;
import dev.nelson.mot.service.DataOperationService;
import dev.nelson.mot.service.action.DataOperationFabric;

import static androidx.appcompat.app.AlertDialog.Builder;

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
        Builder builder = new Builder(Objects.requireNonNull(getActivity()));
        builder.setTitle(R.string.dialog_category_item_menu_title);
        builder.setItems(R.array.category_options_dialog, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case 0:
                        //rename category
                        CategoryDialog.newInstance(CategoryDialog.ACTION_EDIT, categoryId).show(Objects.requireNonNull(getActivity()).getSupportFragmentManager(), "tag");
                        break;
                    case 1:
                        //delete category
                        Intent intent = new Intent(getContext(), DataOperationService.class);
                        intent.setAction(DataOperationFabric.DELETE_CATEGORY);
                        intent.putExtra(CategoriesProvider.Columns._ID, categoryId);
                        Objects.requireNonNull(getContext()).startService(intent);
                        break;
                }
            }
        });
        return builder.create();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
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
