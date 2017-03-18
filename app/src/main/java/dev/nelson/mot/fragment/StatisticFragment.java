package dev.nelson.mot.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import dev.nelson.mot.R;

public class StatisticFragment extends Fragment {

    public static final String FRAGMENT_TAG = StatisticFragment.class.getName();

    @BindView(R.id.statistic_wrapper)
    FrameLayout mWrapper;

    private FragmentManager mFragmentManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_statistic, container, false);
        ButterKnife.bind(this, view);
        mFragmentManager = getActivity().getSupportFragmentManager();
        mFragmentManager.beginTransaction().replace(R.id.statistic_wrapper, new StatisticMonthFragment(), StatisticMonthFragment.FRAGMENT_TAG).commit();
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_statistic_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.fragment_statistic_menu_item_month:
                Fragment currentFragment = mFragmentManager.findFragmentByTag(StatisticMonthFragment.FRAGMENT_TAG);
                if (!(currentFragment instanceof StatisticMonthFragment)) {
                    mFragmentManager
                            .beginTransaction()
                            .replace(R.id.statistic_wrapper, new StatisticMonthFragment(), StatisticMonthFragment.FRAGMENT_TAG)
                            .commit();
                }
                break;
            case R.id.fragment_statistic_menu_item_year:
                mFragmentManager
                        .beginTransaction()
                        .replace(R.id.statistic_wrapper, new StatisticYearFragment(), StatisticYearFragment.FRAGMENT_TAG)
                        .commit();
                break;
            case R.id.fragment_statistic_menu_item_categories:
                mFragmentManager
                        .beginTransaction()
                        .replace(R.id.statistic_wrapper, new StatisticCategoriesFragment(), StatisticCategoriesFragment.FRAGMENT_TAG)
                        .commit();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
