package dev.nelson.mot.activity;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import dev.nelson.mot.R;
import dev.nelson.mot.adapter.CategoriesAdapter;
import dev.nelson.mot.callback.SetDataFromCategoriesLoaderCallbacks;
import dev.nelson.mot.db.model.CategoriesProvider;
import dev.nelson.mot.dialog.CategoryDialog;
import dev.nelson.mot.loadercallback.CategoriesLoaderCallbacks;

public class ChooseCategoryActivity extends AppCompatActivity implements SetDataFromCategoriesLoaderCallbacks {

    public static final int REQUEST_CODE = 100;

    private Toolbar mToolbar;
    private RecyclerView mRecyclerView;
    private CategoriesAdapter mAdapter;
    private CategoriesLoaderCallbacks mLoaderCallbacks;
    private ActionBar mActonBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_category);
        initView();
        initToolbar();
        mAdapter = new CategoriesAdapter(this, null, CategoriesAdapter.FLAG_CHOOSE_CATEGORY, this, this);
        initRecyclerView();
        mLoaderCallbacks = new CategoriesLoaderCallbacks(this, mAdapter);
        getSupportLoaderManager().initLoader(CategoriesLoaderCallbacks.LOADER_ID, null, mLoaderCallbacks);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.fragment_categories_menu, menu);
        for (int i = 0; i < menu.size(); i++) {
            Drawable drawable = menu.getItem(i).getIcon();
            if (drawable != null) {
                drawable.mutate();
                drawable.setColorFilter(ContextCompat.getColor(this, R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);
            }
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.fragment_categories_menu_item_add) {
            CategoryDialog.newInstance(CategoryDialog.ACTION_ADD).show(getSupportFragmentManager(), "Add new category dialog");
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setValues(int id, String name) {
        Intent resultIntent = new Intent();
        resultIntent.putExtra(CategoriesProvider.Columns._ID, id);
        resultIntent.putExtra(CategoriesProvider.Columns.CATEGORY_NAME, name);
        setResult(RESULT_OK, resultIntent);
    }

    private void initView() {
        mToolbar = findViewById(R.id.toolbar);
        mRecyclerView = findViewById(R.id.recycler_view);
    }

    private void initToolbar() {
        setSupportActionBar(mToolbar);
        if ((mActonBar = getSupportActionBar()) != null) {
            mActonBar.setTitle(R.string.activity_choose_category_toolbar_title);
        }
    }

    private void initRecyclerView() {
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);
        DividerItemDecoration decoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        mRecyclerView.addItemDecoration(decoration);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
