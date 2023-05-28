package dev.nelson.mot.legacy.adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import dev.nelson.mot.R;
import dev.nelson.mot.legacy.activity.PaymentActivity;
import dev.nelson.mot.legacy.db.model.CategoriesProvider;
import dev.nelson.mot.legacy.db.model.PaymentsProvider;
import dev.nelson.mot.legacy.dialog.PaymentOptionsDialog;
import dev.nelson.mot.legacy.utils.StringUtils;


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

        String formattedCost = StringUtils.formattedCost(mContext, cursor.getLong(cursor.getColumnIndex(PaymentsProvider.Columns.COST)));
        holder.mCost.setText(StringUtils.makeCostNegative(formattedCost));
        holder.mDate.setText(cursor.getString(cursor.getColumnIndex(PaymentsProvider.Columns.DATE)));

        if (mFlagStatus.equals(FLAG_RECENT_PAYMENTS)) {
            String categoryName = cursor.getString(cursor.getColumnIndex(CategoriesProvider.Columns.CATEGORY_NAME));
            if (categoryName == null) {
                holder.mCategory.setText(mContext.getString(R.string.no_category_category_name));
            } else {
                holder.mCategory.setText(categoryName);
            }
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
        TextView mTitle = itemView.findViewById(R.id.item_payment_text_title);
        TextView mCost = itemView.findViewById(R.id.item_payment_text_cost);
        TextView mCategory = itemView.findViewById(R.id.item_payment_text_category_name);
        TextView mDate = itemView.findViewById(R.id.item_payment_text_date);
        TextView mSummary = itemView.findViewById(R.id.item_payment_text_summary);
        ImageView mItemMenu = itemView.findViewById(R.id.item_payment_image_settings);
        int paymentId;

        ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
