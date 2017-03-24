package dev.nelson.mot.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.github.mikephil.charting.data.BarData;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import dev.nelson.mot.R;
import dev.nelson.mot.adapter.StatisticByMonthWithCategoriesAdapter;
import dev.nelson.mot.callback.StatisticByMonthWithCategoriesCallback;
import dev.nelson.mot.loadercalback.StatisticByMonthWithCategoriesLoaderCallbacks;

public class StatisticByMonthsWithCategoriesFragment extends Fragment implements StatisticByMonthWithCategoriesCallback {

    public static final String FRAGMENT_TAG = StatisticByMonthsWithCategoriesFragment.class.getName();

    @BindView(R.id.statistic_list_view)
    ListView mListView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_satistic_by_months_with_categories, container, false);
        ButterKnife.bind(this, view);
        StatisticByMonthWithCategoriesLoaderCallbacks mLoaderCallbacks = new StatisticByMonthWithCategoriesLoaderCallbacks(getContext(), this);
//        rewrite database observer, change notification by uri, use init loader instead of restart
//        getActivity().getSupportLoaderManager().initLoader(StatisticByMonthWithCategoriesLoaderCallbacks.LOADER_ID, null, mLoaderCallbacks);
        getActivity().getSupportLoaderManager().restartLoader(StatisticByMonthWithCategoriesLoaderCallbacks.LOADER_ID, null, mLoaderCallbacks);
        return view;
    }

    @Override
    public void getDataFromStatisticLoaderCallbacks(ArrayList<BarData> months) {
//        pass data into adapter
        StatisticByMonthWithCategoriesAdapter mAdapter = new StatisticByMonthWithCategoriesAdapter(getContext(), months);
        mListView.setAdapter(mAdapter);
    }
}
