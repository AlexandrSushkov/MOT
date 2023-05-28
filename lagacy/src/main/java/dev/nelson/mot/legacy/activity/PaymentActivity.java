package dev.nelson.mot.legacy.activity;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import dev.nelson.mot.R;
import dev.nelson.mot.legacy.callback.DatabaseChangesCallback;
import dev.nelson.mot.legacy.callback.SetDataFromPaymentLoaderCallbacks;
import dev.nelson.mot.legacy.db.model.CategoriesProvider;
import dev.nelson.mot.legacy.db.model.PaymentsProvider;
import dev.nelson.mot.legacy.loadercallback.PaymentLoaderCallbacks;
import dev.nelson.mot.legacy.observer.DatabaseChangesObserver;
import dev.nelson.mot.legacy.payment.Payment;
import dev.nelson.mot.legacy.service.DataOperationService;
import dev.nelson.mot.legacy.service.action.DataOperationFabric;
import dev.nelson.mot.legacy.utils.CurrencyTextWatcher;
import dev.nelson.mot.legacy.utils.StringUtils;

@Deprecated
public class PaymentActivity extends AppCompatActivity implements SetDataFromPaymentLoaderCallbacks,
        DatabaseChangesCallback {

    //actions for intent
    public static final String ACTION_PREVIEW = "preview";
    public static final String ACTION_EDIT = "edit";

    //saveInstanceState keys
    private static final String PAYMENT_INITIAL_STATE_KEY = "payment_initial_state";
    private static final String PAYMENT_CURRENT_STATE_KEY = "payment_current_state";

    private Toolbar mToolbar;
    private EditText mTitle;
    private TextView mCategoryName;
    private EditText mCost;
    private EditText mSummary;
    private FloatingActionButton mFab;
    private LinearLayout mPaymentWrapper;
    private ActionBar mActonBar;
    private String mActionStatus;
    private PaymentLoaderCallbacks mPaymentLoaderCallbacks;

    //payment data
    private Payment paymentInitialState;
    private Payment paymentCurrentState;
    private DatabaseChangesObserver mDatabaseChangesObserver;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        initView();
        initToolbar();
        mActionStatus = getIntent().getAction();
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
        mCost.addTextChangedListener(new CurrencyTextWatcher(this, mCost));
        if (paymentInitialState == null && paymentCurrentState == null) {
            initPaymentStates();
        }
        mCategoryName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PaymentActivity.this, ChooseCategoryActivity.class);
                startActivityForResult(intent, ChooseCategoryActivity.REQUEST_CODE);
            }
        });

        mPaymentWrapper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSummary.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.showSoftInput(mSummary, 0);
            }
        });

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initEditMode();
                mActionStatus = ACTION_EDIT;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                switch (mActionStatus) {
                    case ACTION_EDIT:
                        addChangesInPaymentCurrentState();
                        if (isRequiredFieldsEmpty()) {
                            Toast.makeText(this, getString(R.string.title_and_cost_should_not_be_empty), Toast.LENGTH_SHORT).show();
                            break;
                        }
                        if (!isPaymentChanged()) {
                            Toast.makeText(this, getString(R.string.payment_has_not_changed), Toast.LENGTH_SHORT).show();
                        } else {
                            if (paymentInitialState.getId() == -1) {
                                insertNewPayment();
                            } else {
                                updatePayment();
                            }
                        }
                        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                        mFab.setVisibility(View.VISIBLE);
                        mActionStatus = ACTION_PREVIEW;
                        initPreviewMode();
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
        paymentInitialState.setTitle(title);
        paymentInitialState.setCategoryId(categoryId);
        paymentInitialState.setCategoryName(categoryName);
        paymentInitialState.setCost(cost);
        paymentInitialState.setSummary(summary);
        initPaymentCurrentState();
        fillFields();
    }

    @Override
    public void dataBaseChanged(int lastAffectedRow) {
        if (lastAffectedRow > 0) {
            Toast.makeText(this, getString(R.string.new_payment_has_been_added), Toast.LENGTH_SHORT).show();
            paymentCurrentState.setId(lastAffectedRow);
            paymentInitialState = new Payment(paymentCurrentState);
        } else {
            paymentInitialState = new Payment(paymentCurrentState);
            Toast.makeText(this, getString(R.string.payment_has_been_updated), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mDatabaseChangesObserver = new DatabaseChangesObserver(new Handler(), this);
        getContentResolver().registerContentObserver(PaymentsProvider.URI, true, mDatabaseChangesObserver);
    }

    @Override
    protected void onPause() {
        super.onPause();
        getContentResolver().unregisterContentObserver(mDatabaseChangesObserver);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ChooseCategoryActivity.REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                paymentCurrentState.setCategoryId(data.getIntExtra(CategoriesProvider.Columns._ID, -1));
                paymentCurrentState.setCategoryName(data.getStringExtra(CategoriesProvider.Columns.CATEGORY_NAME));
                mCategoryName.setText(paymentCurrentState.getCategoryName());
            }
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            paymentInitialState = savedInstanceState.getParcelable(PAYMENT_INITIAL_STATE_KEY);
            paymentCurrentState = savedInstanceState.getParcelable(PAYMENT_CURRENT_STATE_KEY);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        paymentCurrentState.setTitle(mTitle.getText().toString());
        if (mCost.getText().toString().length() > 0) {
            paymentCurrentState.setCost(Long.valueOf(StringUtils.cleanString(mCost.getText().toString())));
        }
        paymentCurrentState.setSummary(mSummary.getText().toString());
        outState.putParcelable(PAYMENT_INITIAL_STATE_KEY, paymentInitialState);
        outState.putParcelable(PAYMENT_CURRENT_STATE_KEY, paymentCurrentState);
    }

    private void initView() {
        mToolbar = findViewById(R.id.payment_toolbar);
        mTitle = findViewById(R.id.item_payment_text_title);
        mCategoryName = findViewById(R.id.payment_category);
        mCost = findViewById(R.id.item_payment_text_cost);
        mSummary = findViewById(R.id.payment_summary);
        mFab = findViewById(R.id.payment_fab);
        mPaymentWrapper = findViewById(R.id.payment_wrapper);
    }

    private void initToolbar() {
        setSupportActionBar(mToolbar);
        if ((mActonBar = getSupportActionBar()) != null) {
            mActonBar.setDisplayHomeAsUpEnabled(true);
            mActonBar.setDisplayShowHomeEnabled(true);
            mActonBar.setTitle("");
        }
    }

    private void initPreviewMode() {
        final Drawable iconBack = ContextCompat.getDrawable(this, R.drawable.ic_arrow_back_black_24dp);
        iconBack.setColorFilter(ContextCompat.getColor(this, R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);
        mActonBar.setHomeAsUpIndicator(iconBack);
        setFieldsEnabled(false);
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

    private void setFieldsEnabled(boolean enabled) {
        mTitle.setEnabled(enabled);
        mCost.setEnabled(enabled);
        mSummary.setEnabled(enabled);
        mCategoryName.setClickable(enabled);
        mPaymentWrapper.setClickable(enabled);
    }

    private void fillFields() {
        mTitle.setText(paymentCurrentState.getTitle());
        mCost.setText(String.valueOf(paymentCurrentState.getCost()));
        mCategoryName.setText(paymentCurrentState.getCategoryName());
        mSummary.setText(paymentCurrentState.getSummary());
    }

    private boolean isPaymentChanged() {
//        if object equals  ==> return false - payment doesn't changed
//        if not equals ===> return true - payment has been changed
        return !paymentInitialState.equals(paymentCurrentState);
    }

    private void initPaymentStates() {
        paymentInitialState = new Payment(getIntent().getIntExtra(PaymentsProvider.Columns._ID, -1));
        if (paymentInitialState.getId() != -1) {
            mPaymentLoaderCallbacks = new PaymentLoaderCallbacks(this, this, paymentInitialState.getId());
            getSupportLoaderManager().initLoader(PaymentLoaderCallbacks.LOADER_ID, null, mPaymentLoaderCallbacks);
        } else {
            initPaymentCurrentState();
        }
    }

    private void initPaymentCurrentState() {
        paymentCurrentState = new Payment(paymentInitialState);
    }

    private void insertNewPayment() {
        Intent intent = new Intent(this, DataOperationService.class);
        intent.setAction(DataOperationFabric.INSERT_PAYMENT);
        intent.putExtra(PaymentsProvider.Columns.TITLE, paymentCurrentState.getTitle());
        intent.putExtra(PaymentsProvider.Columns.CATEGORY_ID, paymentCurrentState.getCategoryId());
        intent.putExtra(PaymentsProvider.Columns.COST, paymentCurrentState.getCost());
        intent.putExtra(PaymentsProvider.Columns.SUMMARY, paymentCurrentState.getSummary());
        startService(intent);
    }

    private void updatePayment() {
        Intent intent = new Intent(this, DataOperationService.class);
        intent.setAction(DataOperationFabric.UPDATE_PAYMENT);
        intent.putExtra(PaymentsProvider.Columns._ID, paymentCurrentState.getId());
        intent.putExtra(PaymentsProvider.Columns.TITLE, paymentCurrentState.getTitle());
        intent.putExtra(PaymentsProvider.Columns.CATEGORY_ID, paymentCurrentState.getCategoryId());
        intent.putExtra(PaymentsProvider.Columns.COST, paymentCurrentState.getCost());
        intent.putExtra(PaymentsProvider.Columns.SUMMARY, mSummary.getText().toString());
        startService(intent);
    }

    private boolean isRequiredFieldsEmpty() {
        return (paymentCurrentState.getTitle().length() <= 0 || paymentCurrentState.getCost() == 0);
    }

    private void addChangesInPaymentCurrentState() {
        paymentCurrentState.setTitle(mTitle.getText().toString());
        if (mCost.getText().toString().length() > 0) {
            paymentCurrentState.setCost(Long.valueOf(StringUtils.cleanString(mCost.getText().toString())));
        }
        paymentCurrentState.setSummary(mSummary.getText().toString());
    }
}
