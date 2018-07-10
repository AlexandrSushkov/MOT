package dev.nelson.mot.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.github.mikephil.charting.data.BarData;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import dev.nelson.mot.R;
import dev.nelson.mot.adapter.StatisticByMonthWithCategoriesAdapter;
import dev.nelson.mot.callback.EmptyCursorCallback;
import dev.nelson.mot.callback.StatisticByMonthWithCategoriesCallback;
import dev.nelson.mot.loadercallback.StatisticByMonthWithCategoriesLoaderCallbacks;

public class StatisticByMonthsWithCategoriesFragment extends Fragment implements StatisticByMonthWithCategoriesCallback,
        EmptyCursorCallback {

    public static final String FRAGMENT_TAG = StatisticByMonthsWithCategoriesFragment.class.getName();

    @BindView(R.id.statistic_list_view)
    ListView mListView;
    @BindView(R.id.no_data_announcement)
    TextView mNoDataAnnouncement;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_satistic_by_months_with_categories, container, false);
        ButterKnife.bind(this, view);
        StatisticByMonthWithCategoriesLoaderCallbacks mLoaderCallbacks = new StatisticByMonthWithCategoriesLoaderCallbacks(getContext(), this, this);
        getActivity().getSupportLoaderManager().restartLoader(StatisticByMonthWithCategoriesLoaderCallbacks.LOADER_ID, null, mLoaderCallbacks);
        return view;
    }

    @Override
    public void getDataFromStatisticLoaderCallbacks(ArrayList<BarData> months) {
//        pass data into adapter
        StatisticByMonthWithCategoriesAdapter mAdapter = new StatisticByMonthWithCategoriesAdapter(getContext(), months);
        mListView.setAdapter(mAdapter);
    }

    @Override
    public void showNoDataAnnouncement() {
        mNoDataAnnouncement.setVisibility(View.VISIBLE);
        mListView.setVisibility(View.GONE);
    }
}
