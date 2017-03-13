package dev.nelson.mot.adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import dev.nelson.mot.R;
import dev.nelson.mot.activity.PaymentActivity;
import dev.nelson.mot.db.model.CategoriesProvider;
import dev.nelson.mot.db.model.PaymentsProvider;
import dev.nelson.mot.dialog.PaymentOptionsDialog;
import dev.nelson.mot.utils.StringUtils;


public class PaymentsAdapter extends CursorRecyclerAdapter<PaymentsAdapter.ViewHolder> {

    public static final String FLAG_RECENT_PAYMENTS = "recent_payments";
    public static final String FLAG_PAYMENTS_FOR_CATEGORY = "payments_for_category";

    private Context mContext;
    private String mFlagStatus;

    public PaymentsAdapter(Context context, Cursor c, String flag) {
        super(c);
        mContext = context;
        mFlagStatus = flag;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_payment, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, Cursor cursor) {
        holder.paymentId = cursor.getInt(cursor.getColumnIndex(PaymentsProvider.Columns._ID));
        holder.mTitle.setText(cursor.getString(cursor.getColumnIndex(PaymentsProvider.Columns.TITLE)));

        String formattedCost = StringUtils.formattedCost(cursor.getLong(cursor.getColumnIndex(PaymentsProvider.Columns.COST)));
        holder.mCost.setText(StringUtils.makeCostNegative(formattedCost));
        holder.mDate.setText(cursor.getString(cursor.getColumnIndex(PaymentsProvider.Columns.DATE)));

        if (mFlagStatus.equals(FLAG_RECENT_PAYMENTS)) {
            holder.mCategory.setText(cursor.getString(cursor.getColumnIndex(CategoriesProvider.Columns.CATEGORY_NAME)));
        }

        if (mFlagStatus.equals(FLAG_PAYMENTS_FOR_CATEGORY)) {
            holder.mCategory.setVisibility(View.GONE);
            // set weight params in header
            holder.mTitle.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 5f));
            holder.mCost.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0f));
            String summary = cursor.getString(cursor.getColumnIndex(PaymentsProvider.Columns.SUMMARY));

            if (summary.length() > 0) {
                holder.mSummary.setVisibility(View.VISIBLE);
                holder.mSummary.setText(summary);
            }
        }
        setOnItemClickListener(holder);
        setOnItemMenuClickListener(holder);
    }

    private void setOnItemMenuClickListener(final PaymentsAdapter.ViewHolder holder) {
        holder.mItemMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = ((AppCompatActivity) mContext).getSupportFragmentManager();
                PaymentOptionsDialog.newInstance(holder.paymentId).show(fm, "CategoryOptionsDialog");
            }
        });
    }

    private void setOnItemClickListener(final PaymentsAdapter.ViewHolder holder) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, PaymentActivity.class);
                intent.setAction(PaymentActivity.ACTION_PREVIEW);
                intent.putExtra(PaymentsProvider.Columns._ID, holder.paymentId);
                mContext.startActivity(intent);
            }
        });
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_payment_text_title)
        TextView mTitle;
        @BindView(R.id.item_payment_text_cost)
        TextView mCost;
        @BindView(R.id.item_payment_text_category_name)
        TextView mCategory;
        @BindView(R.id.item_payment_text_date)
        TextView mDate;
        @BindView(R.id.item_payment_text_summary)
        TextView mSummary;
        @BindView(R.id.item_payment_image_settings)
        ImageView mItemMenu;
        int paymentId;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
