package dev.nelson.mot.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import dev.nelson.mot.R;


public class StatisticCategoriesFragment extends Fragment {

    public static final String FRAGMENT_TAG = StatisticCategoriesFragment.class.getName();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_statistic_categories, container, false);
        ButterKnife.bind(this, view);
        return view;
    }


}
