package dev.nelson.mot.activity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import dev.nelson.mot.R;
import dev.nelson.mot.dialog.CategoryDialog;
import dev.nelson.mot.fragment.AboutFragment;
import dev.nelson.mot.fragment.CategoriesFragment;
import dev.nelson.mot.fragment.RecentPaymentsFragment;
import dev.nelson.mot.fragment.StatisticFragment;

public class MainActivity extends AppCompatActivity {

    DrawerLayout mDrawerLayout = findViewById(R.id.activity_main_drawer);
    Toolbar mToolbar = findViewById(R.id.toolbar);
    NavigationView mNavView = findViewById(R.id.toolbar);

    private ActionBarDrawerToggle drawerToggle;
    private Fragment mContentFragment = null;
    private FragmentManager mFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFragmentManager = getSupportFragmentManager();
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
                mDrawerLayout.openDrawer(GravityCompat.START, true);
                return true;
            case R.id.fragment_categories_menu_item_add:
              CategoryDialog
                      .newInstance(CategoryDialog.ACTION_ADD)
                      .show(getSupportFragmentManager(), "Category option dialog");
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
