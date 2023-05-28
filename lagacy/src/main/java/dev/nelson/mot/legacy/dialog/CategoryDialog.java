package dev.nelson.mot.legacy.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import dev.nelson.mot.R;
import dev.nelson.mot.legacy.db.model.CategoriesProvider;
import dev.nelson.mot.legacy.service.DataOperationService;
import dev.nelson.mot.legacy.service.action.DataOperationFabric;
import dev.nelson.mot.legacy.utils.Constants;
import dev.nelson.mot.legacy.utils.StringUtils;

public class CategoryDialog extends DialogFragment {

    public static final int ACTION_ADD = 1;
    public static final int ACTION_EDIT = 2;
    public static final String ACTION_KEY = "action_key";
    public static final String ID_KEY = "id_key";
    public static final String TITLE_KEY = "title_key";

    EditText mEditText;
    private int mActionState;
    private int mCategoryId;
    private String mTitle;

    public static CategoryDialog newInstance(int action) {
        Bundle args = new Bundle();
        CategoryDialog fragment = new CategoryDialog();
        args.putInt(ACTION_KEY, action);
        fragment.setArguments(args);
        return fragment;
    }

    public static CategoryDialog newInstance(int action, int id) {
        Bundle args = new Bundle();
        CategoryDialog fragment = new CategoryDialog();
        args.putInt(ACTION_KEY, action);
        args.putInt(ID_KEY, id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActionState = getArguments().getInt(ACTION_KEY, -1);
        mCategoryId = getArguments().getInt(ID_KEY, -1);
        if (mActionState == ACTION_ADD){
            mTitle = getResources().getString(R.string.dialog_category_add_action_title);
        }
        if(mActionState == ACTION_EDIT){
            mTitle = getResources().getString(R.string.dialog_category_edit_action_title);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.fragment_dialog_add_category, null);
        mEditText = dialogView.findViewById(R.id.fragment_dialog_add_category_edit_text);
        mEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        mEditText.requestFocus();
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        dialogBuilder.setView(dialogView);
        dialogBuilder.setTitle(mTitle);
        dialogBuilder.setPositiveButton(R.string.dialog_positive_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String editTextData = mEditText.getText().toString().trim();
                if(StringUtils.isEmpty(editTextData)){
                    //alert dialog "category empty name"
                    AlertDialog alertDialog = initCategoryEmptyNameAlertDialog();
                    alertDialog.show();
                }else {
                    if(mActionState == ACTION_ADD){
                        insertCategoryIntoDB(editTextData);
                    }
                    if(mActionState == ACTION_EDIT){
                        renameCategory(mCategoryId, editTextData);
                    }
                }
                dialog.dismiss();
            }
        });
        dialogBuilder.setNegativeButton(R.string.dialog_negative_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        final Dialog dialog = dialogBuilder.create();
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        mEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE).performClick();
                return false;
            }
        });
        return dialog;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(ACTION_KEY, mActionState);
        outState.putString(TITLE_KEY, mTitle);
        outState.putInt(ID_KEY, mCategoryId);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null){
            mActionState = savedInstanceState.getInt(ACTION_KEY, -1);
            mTitle = savedInstanceState.getString(TITLE_KEY);
            mCategoryId = savedInstanceState.getInt(ID_KEY, -1);
        }
    }

    private AlertDialog initCategoryEmptyNameAlertDialog(){
        AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
        alertDialog.setTitle(getActivity().getString(R.string.dialog_empty_category_name_title));
        alertDialog.setMessage(getActivity().getString(R.string.dialog_empty_category_name_message));
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE,
                getActivity().getString(R.string.dialog_positive_button),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        return alertDialog;
    }

    private void insertCategoryIntoDB(String categoryName){
        Intent intent = new Intent(getContext(), DataOperationService.class);
        intent.setAction(DataOperationFabric.INSERT_CATEGORY);
        intent.putExtra(CategoriesProvider.Columns.CATEGORY_NAME, categoryName);
        getContext().startService(intent);
    }

    private void renameCategory(int categoryID, String newName){
        Intent intent = new Intent(getContext(), DataOperationService.class);
        intent.setAction(DataOperationFabric.UPDATE_CATEGORY);
        intent.putExtra(CategoriesProvider.Columns.CATEGORY_NAME, newName);
        intent.putExtra(Constants.ID_KEY, categoryID);
        getContext().startService(intent);
    }
}
