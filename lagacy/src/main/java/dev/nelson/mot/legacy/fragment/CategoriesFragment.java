package dev.nelson.mot.legacy.fragment;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import dev.nelson.mot.R;
import dev.nelson.mot.legacy.activity.CategoryContentActivity;
import dev.nelson.mot.legacy.adapter.CategoriesAdapter;
import dev.nelson.mot.legacy.db.model.CategoriesProvider;
import dev.nelson.mot.legacy.loadercallback.CategoriesLoaderCallbacks;


public class CategoriesFragment extends Fragment {

    public static final String FRAGMENT_TAG = CategoriesFragment.class.getName();


    FrameLayout mItemNoCategory;
    RecyclerView mRecyclerView;
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
        mItemNoCategory = view.findViewById(R.id.item_no_category);
        mRecyclerView = view.findViewById(R.id.recycler_view);

        mItemNoCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), CategoryContentActivity.class);
                intent.putExtra(CategoriesProvider.Columns._ID, -1);
                intent.putExtra(CategoriesProvider.Columns.CATEGORY_NAME, getString(R.string.no_category_category_name));
                getContext().startActivity(intent);
            }
        });

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
}
