package dev.nelson.mot.legacy.activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import dev.nelson.mot.R;
import dev.nelson.mot.legacy.adapter.PaymentsAdapter;
import dev.nelson.mot.legacy.callback.EmptyCursorCallback;
import dev.nelson.mot.legacy.db.model.CategoriesProvider;
import dev.nelson.mot.legacy.loadercallback.PaymentsForCategoryLoaderCallbacks;

@Deprecated
public class CategoryContentActivity extends AppCompatActivity implements EmptyCursorCallback {

    Toolbar mToolbar = findViewById(R.id.toolbar);
    RecyclerView mRecyclerView = findViewById(R.id.recycler_view);
    TextView mNoDataAnnouncement = findViewById(R.id.no_data_announcement);

    private ActionBar mActonBar;
    private PaymentsAdapter mAdapter;
    private PaymentsForCategoryLoaderCallbacks mLoaderCallbacks;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_content);
        initToolbar();
        mAdapter = new PaymentsAdapter(this, null, PaymentsAdapter.FLAG_PAYMENTS_FOR_CATEGORY);
        initRecyclerView();
        int categoryId = getIntent().getIntExtra(CategoriesProvider.Columns._ID, -1);
        mLoaderCallbacks = new PaymentsForCategoryLoaderCallbacks(this, mAdapter, categoryId, this);
        getSupportLoaderManager().initLoader(PaymentsForCategoryLoaderCallbacks.LOADER_ID, null, mLoaderCallbacks);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showNoDataAnnouncement() {
        mNoDataAnnouncement.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.GONE);
    }

    private void initToolbar() {
        setSupportActionBar(mToolbar);
        if ((mActonBar = getSupportActionBar()) != null) {
            mActonBar.setDisplayHomeAsUpEnabled(true);
            mActonBar.setDisplayShowHomeEnabled(true);
            mActonBar.setTitle(getIntent().getStringExtra(CategoriesProvider.Columns.CATEGORY_NAME));
        }
    }

    private void initRecyclerView(){
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        DividerItemDecoration decoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        mRecyclerView.addItemDecoration(decoration);
    }

}
