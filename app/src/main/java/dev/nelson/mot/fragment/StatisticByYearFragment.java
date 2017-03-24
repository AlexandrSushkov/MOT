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
import dev.nelson.mot.adapter.StatisticByYearsAdapter;
import dev.nelson.mot.callback.StatisticByYearsCallback;
import dev.nelson.mot.loadercalback.StatisticByMonthWithCategoriesLoaderCallbacks;
import dev.nelson.mot.loadercalback.StatisticByYearsLoaderCallbacks;


public class StatisticByYearFragment extends Fragment implements StatisticByYearsCallback{

    public static final String FRAGMENT_TAG = StatisticByYearFragment.class.getName();

    @BindView(R.id.statistic_list_view)
    ListView mListView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_statistic_by_year, container, false);
        ButterKnife.bind(this, view);
        StatisticByYearsLoaderCallbacks loadersCallback = new StatisticByYearsLoaderCallbacks(getContext(), this);
        getActivity().getSupportLoaderManager().restartLoader(StatisticByYearsLoaderCallbacks.LOADER_ID, null, loadersCallback);
        return view;
    }

    @Override
    public void getDataFromStatisticByYearsLoaderCallbacks(ArrayList<BarData> years) {
        //        pass data into adapter
        StatisticByYearsAdapter adapter = new StatisticByYearsAdapter(getContext(), years);
        mListView.setAdapter(adapter);
    }
}
