package dev.nelson.mot.legacy.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import dev.nelson.mot.R;

public class StatisticFragment extends Fragment {

    public static final String FRAGMENT_TAG = StatisticFragment.class.getName();

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
            mFragmentManager.beginTransaction()
                    .replace(R.id.statistic_wrapper, mCurrentFragment)
                    .commit();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_statistic, container, false);
        mWrapper = view.findViewById(R.id.statistic_wrapper);
        mFragmentManager = this.getChildFragmentManager();
        if (mCurrentFragment == null) {
            Fragment f = new StatisticCurrentMonthFragment();
            mFragmentManager.beginTransaction()
                    .replace(R.id.statistic_wrapper, f, StatisticCurrentMonthFragment.FRAGMENT_TAG)
                    .commit();
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
        if (item.getItemId() == R.id.fragment_statistic_menu_item_current_month) {
            if (!(mCurrentFragment instanceof StatisticCurrentMonthFragment)) {
                mCurrentFragment = new StatisticCurrentMonthFragment();
                mFragmentManager
                        .beginTransaction()
                        .replace(R.id.statistic_wrapper, mCurrentFragment,
                                StatisticCurrentMonthFragment.FRAGMENT_TAG)
                        .commit();
            }
        } else if (item.getItemId() == R.id.fragment_statistic_menu_item_by_months) {
            if (!(mCurrentFragment instanceof StatisticByMonthsFragment)) {
                mCurrentFragment = new StatisticByMonthsFragment();
                mFragmentManager
                        .beginTransaction()
                        .replace(R.id.statistic_wrapper, mCurrentFragment,
                                StatisticByMonthsFragment.FRAGMENT_TAG)
                        .commit();
            }
        } else if (item.getItemId() == R.id.fragment_statistic_menu_item_by_months_with_categories) {
            if (!(mCurrentFragment instanceof StatisticByMonthsWithCategoriesFragment)) {
                mCurrentFragment = new StatisticByMonthsWithCategoriesFragment();
                mFragmentManager
                        .beginTransaction()
                        .replace(R.id.statistic_wrapper, mCurrentFragment,
                                StatisticByMonthsWithCategoriesFragment.FRAGMENT_TAG)
                        .commit();
            }
        } else if (item.getItemId() == R.id.fragment_statistic_menu_item_by_years) {
            if (!(mCurrentFragment instanceof StatisticByYearFragment)) {
                mCurrentFragment = new StatisticByYearFragment();
                mFragmentManager
                        .beginTransaction()
                        .replace(R.id.statistic_wrapper, mCurrentFragment,
                                StatisticByYearFragment.FRAGMENT_TAG)
                        .commit();
            }
        } else if (item.getItemId() == R.id.fragment_statistic_menu_item_categories) {
            if (!(mCurrentFragment instanceof StatisticByCategoriesFragment)) {
                mCurrentFragment = new StatisticByCategoriesFragment();
                mFragmentManager
                        .beginTransaction()
                        .replace(R.id.statistic_wrapper, mCurrentFragment,
                                StatisticByCategoriesFragment.FRAGMENT_TAG)
                        .commit();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getChildFragmentManager().putFragment(outState, "key", mCurrentFragment);
    }
}
