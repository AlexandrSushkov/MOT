package dev.nelson.mot.activity;

import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import butterknife.BindView;
import butterknife.ButterKnife;
import dev.nelson.mot.R;
import dev.nelson.mot.dialog.DialogFragmentAddCategory;
import dev.nelson.mot.fragment.AboutFragment;
import dev.nelson.mot.fragment.CategoriesFragment;
import dev.nelson.mot.fragment.HomeFragment;
import dev.nelson.mot.fragment.StatisticFragment;

public class MainActivity extends AppCompatActivity{

    @BindView(R.id.activity_main_drawer)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.main_activity_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.main_activity_navigation_view)
    NavigationView mNavView;

    private ActionBarDrawerToggle drawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        drawerToggle = setUpDrawerTogle();
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
        }
        return super.onOptionsItemSelected(item);
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

    public void selectDrawerItem(MenuItem menuItem){
        Fragment fragment = null;
        Class fragmentClass;
        switch (menuItem.getItemId()){
            case R.id.navigation_menu_item_home:
                fragmentClass = HomeFragment.class;
                break;
            case R.id.navigation_menu_item_category:
                fragmentClass = CategoriesFragment.class;
                break;
            case R.id.navigation_menu_item_statistic:
                fragmentClass = StatisticFragment.class;
                break;
            case R.id.navigation_menu_item_about:
                fragmentClass = AboutFragment.class;
                break;
            default:
                fragmentClass = HomeFragment.class;
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit();

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
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    private ActionBarDrawerToggle setUpDrawerTogle(){
        return new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.drawer_open, R.string.drawer_close);
    }
}
