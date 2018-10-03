package dev.nelson.mot.legacy.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import dev.nelson.mot.R;
import dev.nelson.mot.legacy.activity.PaymentActivity;
import dev.nelson.mot.legacy.adapter.PaymentsAdapter;
import dev.nelson.mot.legacy.callback.DatabaseChangesCallback;
import dev.nelson.mot.legacy.callback.EmptyCursorCallback;
import dev.nelson.mot.legacy.db.model.PaymentsProvider;
import dev.nelson.mot.legacy.loadercallback.PaymentLoaderCallbacks;
import dev.nelson.mot.legacy.loadercallback.RecentPaymentsLoadersCallbacks;
import dev.nelson.mot.legacy.observer.DatabaseChangesObserver;

public class RecentPaymentsFragment extends Fragment implements DatabaseChangesCallback, EmptyCursorCallback {

    public static final String FRAGMENT_TAG = RecentPaymentsFragment.class.getName();

    RecyclerView mRecyclerView;
    TextView mNoDataAnnouncement;
    FloatingActionButton mFab;
    private Context mContext;
    private PaymentsAdapter mAdapter;
    private RecentPaymentsLoadersCallbacks mLoaderCallbacks;
    private DatabaseChangesObserver mDatabaseChangesObserver;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getContext();
        View view = inflater.inflate(R.layout.fragment_recent_payments, container, false);
        mRecyclerView = view.findViewById(R.id.recycler_view);
        mNoDataAnnouncement = view.findViewById(R.id.no_data_announcement);
        mFab = view.findViewById(R.id.fragment_home_fab);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, PaymentActivity.class);
                intent.setAction(PaymentActivity.ACTION_EDIT);
                mContext.startActivity(intent);
            }
        });
        mAdapter = new PaymentsAdapter(mContext, null, PaymentsAdapter.FLAG_RECENT_PAYMENTS);
        initRecyclerView();
        mLoaderCallbacks = new RecentPaymentsLoadersCallbacks(mContext, mAdapter, this);
        getActivity().getSupportLoaderManager().initLoader(PaymentLoaderCallbacks.LOADER_ID, null, mLoaderCallbacks);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mDatabaseChangesObserver = new DatabaseChangesObserver(new Handler(), this);
        getActivity().getContentResolver().registerContentObserver(PaymentsProvider.URI, true, mDatabaseChangesObserver);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        getActivity().getContentResolver().unregisterContentObserver(mDatabaseChangesObserver);
    }

    private void initRecyclerView() {
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        DividerItemDecoration decoration = new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL);
        mRecyclerView.addItemDecoration(decoration);
    }

    @Override
    public void dataBaseChanged(int lastAffectedRow) {
        getActivity().getSupportLoaderManager().restartLoader(PaymentLoaderCallbacks.LOADER_ID, null, mLoaderCallbacks);
    }

    @Override
    public void showNoDataAnnouncement() {
        mNoDataAnnouncement.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.GONE);
    }
}
