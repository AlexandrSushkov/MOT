package dev.nelson.mot.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import butterknife.BindView;
import butterknife.ButterKnife;
import dev.nelson.mot.R;
import dev.nelson.mot.adapter.PaymentsAdapter;
import dev.nelson.mot.loadercalback.PaymentsForCategoryLoaderCallbacks;
import dev.nelson.mot.db.model.CategoriesProvider;
import dev.nelson.mot.loadercalback.PaymentLoaderCallbacks;

public class CategoryContentActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    private ActionBar mActonBar;
    private PaymentsAdapter mAdapter;
    private PaymentsForCategoryLoaderCallbacks mLoaderCallbacks;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_content);
        ButterKnife.bind(this);
        initToolbar();
        mAdapter = new PaymentsAdapter(this, null, PaymentsAdapter.FLAG_PAYMENTS_FOR_CATEGORY);
        initRecyclerView();
        int categoryId = getIntent().getIntExtra(CategoriesProvider.Columns._ID, -1);
        mLoaderCallbacks = new PaymentsForCategoryLoaderCallbacks(this, mAdapter, categoryId);
        getSupportLoaderManager().initLoader(PaymentsForCategoryLoaderCallbacks.LOADER_ID, null, mLoaderCallbacks);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
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
