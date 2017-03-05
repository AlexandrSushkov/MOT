package dev.nelson.mot.fragment;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import dev.nelson.mot.R;
import dev.nelson.mot.adapter.CategoriesAdapter;
import dev.nelson.mot.dialog.CategoryDialog;
import dev.nelson.mot.loadercalback.CategoriesLoaderCallbacks;


public class CategoriesFragment extends Fragment{

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
//    @BindView(R.id.item_fragment_category_recycler_view_image_menu)
//    ImageView mItemMenu;
//    private CategoryAdapter mAdapter;
    private CategoriesAdapter mAdapter;
    private CategoriesLoaderCallbacks mLoaderCallbacks;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_categories, container, false);
        ButterKnife.bind(this, view);
//        mAdapter = new CategoryAdapter(view.getContext(), null);
        mAdapter = new CategoriesAdapter(view.getContext(), null, CategoriesAdapter.FLAG_VIEW_CATEGORIES);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);
        DividerItemDecoration decoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        mRecyclerView.addItemDecoration(decoration);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mLoaderCallbacks = new CategoriesLoaderCallbacks(getContext(), mAdapter);
        getActivity().getSupportLoaderManager().initLoader(CategoriesLoaderCallbacks.LOADER_ID, null, mLoaderCallbacks);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_categories_menu, menu);

        for (int i = 0; i < menu.size(); i++) {
            Drawable drawable = menu.getItem(i).getIcon();
            if (drawable != null){
                drawable.mutate();
                drawable.setColorFilter(ContextCompat.getColor(getContext(), R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.fragment_categories_menu_item_add) {
            CategoryDialog.newInstance(CategoryDialog.ACTION_ADD).show(getActivity().getSupportFragmentManager(), "tag");
        }
        return super.onOptionsItemSelected(item);
    }

}
