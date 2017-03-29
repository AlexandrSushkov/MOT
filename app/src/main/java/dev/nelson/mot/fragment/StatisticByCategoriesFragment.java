package dev.nelson.mot.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.github.mikephil.charting.data.LineData;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import dev.nelson.mot.R;
import dev.nelson.mot.adapter.StatisticByCategoriesAdapter;
import dev.nelson.mot.adapter.StatisticByMonthWithCategoriesAdapter;
import dev.nelson.mot.callback.EmptyCursorCallback;
import dev.nelson.mot.callback.StatisticByCategoriesCallback;
import dev.nelson.mot.loadercalback.StatisticByCategoriesLoaderCallbacks;
import dev.nelson.mot.loadercalback.StatisticByMonthWithCategoriesLoaderCallbacks;


public class StatisticByCategoriesFragment extends Fragment implements StatisticByCategoriesCallback,
        EmptyCursorCallback{

    public static final String FRAGMENT_TAG = StatisticByCategoriesFragment.class.getName();

    @BindView(R.id.statistic_list_view)
    ListView mListView;
    @BindView(R.id.no_data_announcement)
    TextView mNoDataAnnouncement;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_statistic_categories, container, false);
        StatisticByCategoriesLoaderCallbacks loaderCallbacks = new StatisticByCategoriesLoaderCallbacks(getContext(), this, this);
        getActivity().getSupportLoaderManager().restartLoader(StatisticByCategoriesLoaderCallbacks.LOADER_ID, null, loaderCallbacks);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void getDataFromStatisticByCategoriesLoaderCallback(ArrayList<LineData> categories ) {
//      pass data into adapter
        StatisticByCategoriesAdapter mAdapter = new StatisticByCategoriesAdapter(getContext(), categories);
        mListView.setAdapter(mAdapter);
    }

    @Override
    public void showNoDataAnnouncement() {
        mNoDataAnnouncement.setVisibility(View.VISIBLE);
        mListView.setVisibility(View.GONE);
    }
}
