package dev.nelson.mot.legacy.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.github.mikephil.charting.data.BarData;

import java.util.ArrayList;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import dev.nelson.mot.R;
import dev.nelson.mot.legacy.adapter.StatisticByMonthWithCategoriesAdapter;
import dev.nelson.mot.legacy.callback.EmptyCursorCallback;
import dev.nelson.mot.legacy.callback.StatisticByMonthWithCategoriesCallback;
import dev.nelson.mot.legacy.loadercallback.StatisticByMonthWithCategoriesLoaderCallbacks;

public class StatisticByMonthsWithCategoriesFragment extends Fragment implements StatisticByMonthWithCategoriesCallback,
        EmptyCursorCallback {

    public static final String FRAGMENT_TAG = StatisticByMonthsWithCategoriesFragment.class.getName();

    ListView mListView;
    TextView mNoDataAnnouncement;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_satistic_by_months_with_categories, container, false);
        mListView = view.findViewById(R.id.statistic_list_view);
        mNoDataAnnouncement = view.findViewById(R.id.no_data_announcement);
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
