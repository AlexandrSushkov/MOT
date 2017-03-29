package dev.nelson.mot.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.crash.FirebaseCrash;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dev.nelson.mot.R;
import dev.nelson.mot.activity.PaymentActivity;
import dev.nelson.mot.adapter.PaymentsAdapter;
import dev.nelson.mot.callback.DatabaseChangesCallback;
import dev.nelson.mot.callback.EmptyCursorCallback;
import dev.nelson.mot.db.model.PaymentsProvider;
import dev.nelson.mot.loadercalback.PaymentLoaderCallbacks;
import dev.nelson.mot.loadercalback.RecentPaymentsLoadersCallbacks;
import dev.nelson.mot.observer.DatabaseChangesObserver;


public class RecentPaymentsFragment extends android.support.v4.app.Fragment implements DatabaseChangesCallback, EmptyCursorCallback {

    public static final String FRAGMENT_TAG = RecentPaymentsFragment.class.getName();

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.no_data_announcement)
    TextView mNoDataAnnouncement;
    @BindView(R.id.fragment_home_fab)
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
        ButterKnife.bind(this, view);
        mAdapter = new PaymentsAdapter(mContext, null, PaymentsAdapter.FLAG_RECENT_PAYMENTS);
        initRecyclerView();
        mLoaderCallbacks = new RecentPaymentsLoadersCallbacks(mContext, mAdapter, this);
        getActivity().getSupportLoaderManager().initLoader(PaymentLoaderCallbacks.LOADER_ID, null, mLoaderCallbacks);
        return view;
    }

    @OnClick(R.id.fragment_home_fab)
    void onClickFab() {
        Intent intent = new Intent(mContext, PaymentActivity.class);
        intent.setAction(PaymentActivity.ACTION_EDIT);
        mContext.startActivity(intent);
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
