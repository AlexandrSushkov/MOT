package dev.nelson.mot.dialog;


import android.support.v4.app.DialogFragment;

/**
 * Created by Nelson on 2/13/17.
 */

public class DialogFabric {

    public static final String DIALOG_ADD_CATEGORY = "add_category";

    public static DialogFragment getDialog(String dialogType){
        switch (dialogType){
            case DIALOG_ADD_CATEGORY:
                return new DialogCategory();
            default:
                throw new IllegalArgumentException("Wrong dialog type: " + dialogType);
        }
    }

}
