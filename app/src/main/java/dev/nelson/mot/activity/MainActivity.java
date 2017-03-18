package dev.nelson.mot.activity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import butterknife.BindView;
import butterknife.ButterKnife;
import dev.nelson.mot.R;
import dev.nelson.mot.dialog.CategoryDialog;
import dev.nelson.mot.fragment.AboutFragment;
import dev.nelson.mot.fragment.CategoriesFragment;
import dev.nelson.mot.fragment.RecentPaymentsFragment;
import dev.nelson.mot.fragment.StatisticCategoriesFragment;
import dev.nelson.mot.fragment.StatisticFragment;
import dev.nelson.mot.fragment.StatisticMonthFragment;
import dev.nelson.mot.fragment.StatisticYearFragment;

public class MainActivity extends AppCompatActivity{

    @BindView(R.id.activity_main_drawer)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.main_activity_navigation_view)
    NavigationView mNavView;
    private ActionBarDrawerToggle drawerToggle;
    private Fragment mContentFragment = null;
    private FragmentManager mFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        drawerToggle = setUpDrawerToggle();
        mDrawerLayout.addDrawerListener(drawerToggle);
        setNavDrawer(mNavView);
        mNavView.setCheckedItem(R.id.navigation_menu_item_home);
        mNavView.getMenu().performIdentifierAction(R.id.navigation_menu_item_home, 0);
    }

    //this method open navigation menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(drawerToggle.onOptionsItemSelected(item)){
            return true;
        }
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.fragment_categories_menu_item_add:
              CategoryDialog.newInstance(CategoryDialog.ACTION_ADD).show(getSupportFragmentManager(), "Category option dialog");
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void selectDrawerItem(MenuItem menuItem){
        Class fragmentClass;
        String tag;
        switch (menuItem.getItemId()){
            case R.id.navigation_menu_item_home:
                fragmentClass = RecentPaymentsFragment.class;
                tag = RecentPaymentsFragment.FRAGMENT_TAG;
                break;
            case R.id.navigation_menu_item_category:
                fragmentClass = CategoriesFragment.class;
                tag = CategoriesFragment.FRAGMENT_TAG;
                break;
            case R.id.navigation_menu_item_statistic:
                fragmentClass = StatisticFragment.class;
                tag = StatisticFragment.FRAGMENT_TAG;
                break;
            case R.id.navigation_menu_item_about:
                fragmentClass = AboutFragment.class;
                tag = AboutFragment.FRAGMENT_TAG;
                break;
            default:
                fragmentClass = RecentPaymentsFragment.class;
                tag = RecentPaymentsFragment.FRAGMENT_TAG;
        }

        try {
            mContentFragment = (Fragment) fragmentClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }

        mFragmentManager = getSupportFragmentManager();
        mFragmentManager.beginTransaction().replace(R.id.fragment_container, mContentFragment, tag).commit();

        // Highlight the selected item has been done by NavigationView
        menuItem.setEnabled(true);

        // Set action bar title
        setTitle(menuItem.getTitle());

        // Close the navigation drawer
        mDrawerLayout.closeDrawers();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mFragmentManager.beginTransaction().detach(mContentFragment).attach(mContentFragment).commit();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    private ActionBarDrawerToggle setUpDrawerToggle(){
        return new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.drawer_open, R.string.drawer_close);
    }

    private void setNavDrawer(NavigationView navigationView){
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                selectDrawerItem(item);
                return true;
            }
        });
    }
}
