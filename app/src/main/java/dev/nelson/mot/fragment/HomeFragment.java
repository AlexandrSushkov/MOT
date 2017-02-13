package dev.nelson.mot.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import dev.nelson.mot.R;


public class HomeFragment extends android.support.v4.app.Fragment {
    private static final int LOADER_ID = 1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    //    @OnClick(R.id.fragment_categories_fab)
//    void onClickFab(){
//        Snackbar.make(view, "fab", Snackbar.LENGTH_LONG).show();
//        DialogFragment dialog = DialogFabric.getDialog(DialogFabric.DIALOG_ADD_CATEGORY);
//        dialog.show(getActivity().getSupportFragmentManager(), DialogFabric.DIALOG_ADD_CATEGORY);
//        DialogFragmentAddCategory.newInstance().show(getActivity().getSupportFragmentManager(), "tag");
//    }
}
