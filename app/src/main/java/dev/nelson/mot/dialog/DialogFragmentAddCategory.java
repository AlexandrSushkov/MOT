package dev.nelson.mot.dialog;

import android.app.Dialog;
import android.app.IntentService;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import dev.nelson.mot.R;
import dev.nelson.mot.db.model.CategoriesProvider;
import dev.nelson.mot.service.DataOperationService;
import dev.nelson.mot.service.action.DataOperationAction;
import dev.nelson.mot.service.action.DataOperationFabric;
import dev.nelson.mot.utils.SqlUtils;
import dev.nelson.mot.utils.StringUtils;


public class DialogFragmentAddCategory extends DialogFragment {

    @BindView(R.id.fragment_dialog_add_category_edit_text)
    EditText mEditText;

    public DialogFragmentAddCategory(){
    }

    public static DialogFragmentAddCategory newInstance() {

        Bundle args = new Bundle();

        DialogFragmentAddCategory fragment = new DialogFragmentAddCategory();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.fragment_dialog_add_category, null);
        ButterKnife.bind(this, dialogView);
        mEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        mEditText.requestFocus();
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        dialogBuilder.setView(dialogView);
        dialogBuilder.setTitle(R.string.dialog_add_category_title);
        dialogBuilder.setPositiveButton(R.string.dialog_positive_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String editTextData = mEditText.getText().toString().trim();
                if(StringUtils.isEmpty(editTextData)){
                    //alert dialog "category empty name"
                    AlertDialog alertDialog = initCategoryEmptyNameAlertDialog();
                    alertDialog.show();
                }else {
                    String categoryName = Character.toUpperCase(editTextData.charAt(0)) + editTextData.substring(1);
                    insertCategoryIntoDB(categoryName);
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
}
