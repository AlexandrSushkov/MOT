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
    private Fragment mCurrentFragment;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            mCurrentFragment = getChildFragmentManager().getFragment(savedInstanceState, "key");
            mFragmentManager.beginTransaction().replace(R.id.statistic_wrapper, mCurrentFragment).commit();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_statistic, container, false);
        ButterKnife.bind(this, view);
        mFragmentManager = this.getChildFragmentManager();
        if (mCurrentFragment == null) {
            Fragment f = new StatisticCurrentMonthFragment();
            mFragmentManager.beginTransaction().replace(R.id.statistic_wrapper, f, StatisticCurrentMonthFragment.FRAGMENT_TAG).commit();
            mCurrentFragment = f;
        }
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
            case R.id.fragment_statistic_menu_item_current_month:
                if (!(mCurrentFragment instanceof StatisticCurrentMonthFragment)) {
                    mCurrentFragment = new StatisticCurrentMonthFragment();
                    mFragmentManager
                            .beginTransaction()
                            .replace(R.id.statistic_wrapper, mCurrentFragment, StatisticCurrentMonthFragment.FRAGMENT_TAG)
                            .commit();
                }
                break;
            case R.id.fragment_statistic_menu_item_by_months:
                if (!(mCurrentFragment instanceof StatisticByMonthsFragment)) {
                    mCurrentFragment = new StatisticByMonthsFragment();
                    mFragmentManager
                            .beginTransaction()
                            .replace(R.id.statistic_wrapper, mCurrentFragment, StatisticByMonthsFragment.FRAGMENT_TAG)
                            .commit();
                }
                break;
            case R.id.fragment_statistic_menu_item_by_months_with_categories:
                if (!(mCurrentFragment instanceof StatisticByMonthsWithCategoriesFragment)) {
                    mCurrentFragment = new StatisticByMonthsWithCategoriesFragment();
                    mFragmentManager
                            .beginTransaction()
                            .replace(R.id.statistic_wrapper, mCurrentFragment, StatisticByMonthsWithCategoriesFragment.FRAGMENT_TAG)
                            .commit();
                }
                break;
            case R.id.fragment_statistic_menu_item_by_years:
                if (!(mCurrentFragment instanceof StatisticByYearFragment)) {
                    mCurrentFragment = new StatisticByYearFragment();
                    mFragmentManager
                            .beginTransaction()
                            .replace(R.id.statistic_wrapper, mCurrentFragment, StatisticByYearFragment.FRAGMENT_TAG)
                            .commit();
                }
                break;
            case R.id.fragment_statistic_menu_item_categories:
                if (!(mCurrentFragment instanceof StatisticByCategoriesFragment)) {
                    mCurrentFragment = new StatisticByCategoriesFragment();
                    mFragmentManager
                            .beginTransaction()
                            .replace(R.id.statistic_wrapper, mCurrentFragment, StatisticByCategoriesFragment.FRAGMENT_TAG)
                            .commit();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getChildFragmentManager().putFragment(outState, "key", mCurrentFragment);
    }
}
