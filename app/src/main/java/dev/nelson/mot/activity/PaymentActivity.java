package dev.nelson.mot.activity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dev.nelson.mot.R;
import dev.nelson.mot.callback.SetDataFromPaymentLoaderCallbacks;
import dev.nelson.mot.db.SQLiteOpenHelperImpl;
import dev.nelson.mot.db.model.CategoriesProvider;
import dev.nelson.mot.db.model.PaymentsProvider;
import dev.nelson.mot.loadercalback.PaymentLoaderCallbacks;
import dev.nelson.mot.service.DataOperationService;
import dev.nelson.mot.service.action.DataOperationFabric;


public class PaymentActivity extends AppCompatActivity implements SetDataFromPaymentLoaderCallbacks {

    public static final String ACTION_PREVIEW = "preview";
    public static final String ACTION_EDIT = "edit";
    private static final String FLAG_KEY = "flag_key";

    @BindView(R.id.payment_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.item_payment_text_title)
    EditText mTitle;
    @BindView(R.id.payment_category)
    TextView mCategoryName;
    @BindView(R.id.item_payment_text_cost)
    EditText mCost;
    @BindView(R.id.payment_summary)
    EditText mSummary;
    @BindView(R.id.payment_fab)
    FloatingActionButton mFab;
    @BindView(R.id.payment_wrapper)
    LinearLayout mPaymentWrapper;
    private ActionBar mActonBar;
    private String mActionStatus;
    private PaymentLoaderCallbacks mLoaderCallbacks;

    //payment data
    private int mPaymentId = -1;
    private int mCategoryId = -1;
    private int mNewCategoryId = -1;
    private String mCategoryNameData = "";
    private String mNewCategoryNameData = "";
    private String mTitleData = "";
    private long mCostData = 0;
    private String mSummaryData = "";


// TODO: 2/26/17 save instance state

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        ButterKnife.bind(this);
        initToolbar();
        mActionStatus = getIntent().getAction();
        mPaymentId = getIntent().getIntExtra(PaymentsProvider.Columns._ID, -1);

        switch (mActionStatus) {
            case ACTION_PREVIEW:
                initPreviewMode();
                break;
            case ACTION_EDIT:
                initEditMode();
                break;
            default:
                throw new IllegalStateException(getClass().getName() + " Wrong action flag.");
        }
        initLoader();
//        mCost.setFilters(new InputFilter[]{new CurrencyFormatInputFilter()});
//        mCost.addTextChangedListener(new CurrencyFormatTextWatcher(mCost));


    }

    @OnClick(R.id.payment_category)
    void onClickCategory(){
        Intent intent = new Intent(this, ChooseCategoryActivity.class);
        startActivityForResult(intent, ChooseCategoryActivity.REQUEST_CODE);
    }

    @OnClick(R.id.payment_wrapper)
    void onClickPaymentWrapper(){
        mSummary.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.showSoftInput(mSummary, 0);
    }

    @OnClick(R.id.payment_fab)
    void onClickFab(){
        initEditMode();
        mActionStatus = ACTION_EDIT;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // TODO: 2/26/17  check all fields are empty
                switch (mActionStatus){
                    case ACTION_EDIT:
//                        Toast.makeText(this, ACTION_EDIT, Toast.LENGTH_SHORT).show();
                        // update data in db
                        if(isPaymentDataChanged()){
                            // TODO: 2/26/17 change message, payments data hasn't changed
                            Toast.makeText(this, "There are no changes", Toast.LENGTH_SHORT).show();
                        } else {
                            if(mCategoryId == -1){
                                //insert new payment
                                Intent intent = new Intent(this, DataOperationService.class);
                                intent.setAction(DataOperationFabric.INSERT_PAYMENT);
                                intent.putExtra(PaymentsProvider.Columns._ID, mPaymentId);
                                intent.putExtra(PaymentsProvider.Columns.TITLE, mTitle.getText().toString());
                                if(mNewCategoryId != -1){
                                    intent.putExtra(PaymentsProvider.Columns.CATEGORY_ID, mNewCategoryId);
                                }
                                intent.putExtra(PaymentsProvider.Columns.COST, Double.valueOf(mCost.getText().toString()));
                                intent.putExtra(PaymentsProvider.Columns.SUMMARY, mSummary.getText().toString());
                                startService(intent);
                                // TODO: 2/26/17 paymentId = get last inserted ID , refresh loader
                                mPaymentId = getLastInsertedId();
                            }else{
                                //update existing payment
                                Intent intent = new Intent(this, DataOperationService.class);
                                intent.setAction(DataOperationFabric.UPDATE_PAYMENT);
                                intent.putExtra(PaymentsProvider.Columns._ID, mPaymentId);
                                intent.putExtra(PaymentsProvider.Columns.TITLE, mTitle.getText().toString());
                                if(mNewCategoryId != -1){
                                    intent.putExtra(PaymentsProvider.Columns.CATEGORY_ID, mNewCategoryId);
                                }
                                intent.putExtra(PaymentsProvider.Columns.COST, Double.valueOf(mCost.getText().toString()));
                                intent.putExtra(PaymentsProvider.Columns.SUMMARY, mSummary.getText().toString());
                                startService(intent);
                            }
                        }
                        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                        mFab.setVisibility(View.VISIBLE);
                        mActionStatus = ACTION_PREVIEW;
                        initPreviewMode();
                        //or restart loader
                        initLoader();
                        break;
                    case ACTION_PREVIEW:
                        finish();
                        break;
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void fillPaymentInitialStateWithData(String title, int categoryId, String categoryName, long cost, String summary) {
        mTitleData = title;
        mCategoryId = categoryId;
        mCategoryNameData = categoryName;
        mCostData = cost;
        mSummaryData = summary;
        fillFields();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ChooseCategoryActivity.REQUEST_CODE ){
            if (resultCode == RESULT_OK){
                mNewCategoryId = data.getIntExtra(CategoriesProvider.Columns._ID, -1);
                mNewCategoryNameData = data.getStringExtra(CategoriesProvider.Columns.CATEGORY_NAME);
                mCategoryName.setText(mNewCategoryNameData);
            }

        }
    }

    private void initToolbar() {
        setSupportActionBar(mToolbar);
        if ((mActonBar = getSupportActionBar()) != null) {
            mActonBar.setDisplayHomeAsUpEnabled(true);
            mActonBar.setDisplayShowHomeEnabled(true);
            mActonBar.setTitle("");
        }
    }

    private void initLoader(){
        if(mPaymentId != -1){
            mLoaderCallbacks = new PaymentLoaderCallbacks(this, this, mPaymentId);
            getSupportLoaderManager().initLoader(PaymentLoaderCallbacks.LOADER_ID, null, mLoaderCallbacks);
        }

    }

    private void initPreviewMode() {
        final Drawable iconBack = ContextCompat.getDrawable(this, R.drawable.ic_arrow_back_black_24dp);
        iconBack.setColorFilter(ContextCompat.getColor(this, R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);
        mActonBar.setHomeAsUpIndicator(iconBack);
        setFieldsEnabled(false);
//        fillFields(getIntent());
    }

    private void initEditMode() {
        final Drawable iconDone = ContextCompat.getDrawable(this, R.drawable.ic_done_black_24dp);
        iconDone.setColorFilter(ContextCompat.getColor(this, R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);
        mActonBar.setHomeAsUpIndicator(iconDone);
        mFab.setVisibility(View.GONE);
        setFieldsEnabled(true);
        mTitle.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    private void setFieldsEnabled(boolean enabled){
        mTitle.setEnabled(enabled);
        mCost.setEnabled(enabled);
        mSummary.setEnabled(enabled);
        mCategoryName.setClickable(enabled);
        mPaymentWrapper.setClickable(enabled);
    }

    private void fillFields(){
//        mTitle.setText(intent.getStringExtra(Constants.TITLE_KEY));
//        mCategoryName.setText(intent.getStringExtra(Constants.CATEFORY_KEY));
//        mCost.setText(intent.getStringExtra(Constants.COST_KEY));
//        mSummary.setText(intent.getStringExtra(Constants.SUMMARY_KEY));
        mTitle.setText(mTitleData);
        if(mSummaryData.length()<= 0){
            mCategoryName.setText("Category");
        }else {
            mCategoryName.setText(mCategoryNameData);
        }
        mCost.setText(String.valueOf(mCostData));
        mSummary.setText(mSummaryData);
    }

    private boolean isPaymentDataChanged(){
        if(mTitleData.equals(mTitle.getText().toString())){
            Log.d("tag", "data = " + mTitleData + " title = "+mTitle.getText().toString()+" status: true");
        }else {
            Log.d("tag", "data = " + mTitleData + " title = "+mTitle.getText().toString()+" status: false");

        }
        if (mCostData == Double.valueOf(mCost.getText().toString())){
            Log.d("tag", "data = " + mCostData + " cost = "+mCost.getText().toString()+" status: true");
        }else {
            Log.d("tag", "data = " + mCostData + " cost = "+mCost.getText().toString()+" status: false");
        }
        if (mCategoryNameData.equals(mCategoryName.getText().toString())){
            Log.d("tag", "data = " + mCategoryNameData + " category name = "+ mCategoryName.getText().toString()+" status: true");
        }else {
            Log.d("tag", "data = " + mCategoryNameData + " category name = "+ mCategoryName.getText().toString()+" status: false");
        }
        if (mSummaryData.equals(mSummary.getText().toString())){
            Log.d("tag", "data = " + mSummaryData + " title = "+mSummary.getText().toString()+" status: true");
        }else {
            Log.d("tag", "data = " + mSummaryData + " title = "+mSummary.getText().toString()+" status: false");
        }

        return mTitleData.equals(mTitle.getText().toString())
                && mCostData == Double.valueOf(mCost.getText().toString())
                && mCategoryNameData.equals(mCategoryName.getText().toString())
                && mSummaryData.equals(mSummary.getText().toString());
    }

    private int getLastInsertedId(){
        SQLiteOpenHelperImpl helper = new SQLiteOpenHelperImpl(this);
        SQLiteDatabase db = helper.getReadableDatabase();
//        select _id from payments order by _id desc limit 1
        String rawQuery = "select " + PaymentsProvider.Columns._ID + " from " + PaymentsProvider.TABLE_NAME
                + " order by " + PaymentsProvider.Columns._ID + " desc limit 1";
        Cursor cursor = db.rawQuery(rawQuery, null);
        int lastInsertedId = 0;
        if(cursor != null){
            cursor.moveToFirst();
            lastInsertedId = cursor.getInt(cursor.getColumnIndex(PaymentsProvider.Columns._ID));
            cursor.close();
        }
        return lastInsertedId;
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mPaymentId = savedInstanceState.getInt("paymentId", mPaymentId);
        mCategoryId = savedInstanceState.getInt("categoryId", mCategoryId);
        mTitleData = savedInstanceState.getString("title", mTitleData);
        mCategoryNameData = savedInstanceState.getString("categoryName", mCategoryNameData);
        mCostData = savedInstanceState.getLong("cost", mCostData);
        mSummaryData = savedInstanceState.getString("summary", mSummaryData);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("paymentId", mPaymentId);
        outState.putInt("categoryId", mCategoryId);
        outState.putString("title", mTitleData);
        outState.putString("categoryName", mCategoryNameData);
        outState.putDouble("cost", mCostData);
        outState.putString("summary", mSummaryData);
    }
}
