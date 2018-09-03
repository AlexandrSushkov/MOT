package dev.nelson.mot.adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import dev.nelson.mot.R;
import dev.nelson.mot.activity.CategoryContentActivity;
import dev.nelson.mot.callback.SetDataFromCategoriesLoaderCallbacks;
import dev.nelson.mot.db.model.CategoriesProvider;
import dev.nelson.mot.dialog.CategoryOptionsDialog;


public class CategoriesAdapter extends CursorRecyclerAdapter<CategoriesAdapter.ViewHolder> {

    public static final int FLAG_VIEW_CATEGORIES = 1;
    public static final int FLAG_CHOOSE_CATEGORY = 2;

    private Context mContext;
    private int mFlagStatus;
    private SetDataFromCategoriesLoaderCallbacks mCategoryDataCallback;
    private AppCompatActivity mActivity;

    public CategoriesAdapter(Context context, Cursor c, int flag) {
        super(c);
        boolean cursorPresent = c != null;
        mDataValid = cursorPresent;
        mContext = context;
        mFlagStatus = flag;
    }

    public CategoriesAdapter(Context context, Cursor c, int flag, AppCompatActivity activity, SetDataFromCategoriesLoaderCallbacks callbackItem) {
        super(c);
        mContext = context;
        mFlagStatus = flag;
        mActivity = activity;
        mCategoryDataCallback = callbackItem;
    }

    @Override
    public CategoriesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, Cursor cursor) {
        if (mFlagStatus == FLAG_CHOOSE_CATEGORY) {
            holder.mItemMenu.setVisibility(View.GONE);
        } else {
            setOnItemMenuClickListener(holder);
        }
        holder.mCategoryName.setText(cursor.getString(cursor.getColumnIndex(CategoriesProvider.Columns.CATEGORY_NAME)));
        holder.categoryId = (int) cursor.getLong(cursor.getColumnIndex(CategoriesProvider.Columns._ID));
        setOnItemClickListener(holder);
    }

    private void setOnItemClickListener(final ViewHolder holder) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mFlagStatus == FLAG_CHOOSE_CATEGORY) {
                    // callback. return data about category into ChooseCategoryActivity
                    mCategoryDataCallback.setValues(holder.categoryId, holder.mCategoryName.getText().toString());
                    mActivity.finish();
                } else if (mFlagStatus == FLAG_VIEW_CATEGORIES) {
                    Intent intent = new Intent(mContext, CategoryContentActivity.class);
                    intent.putExtra(CategoriesProvider.Columns._ID, holder.categoryId);
                    intent.putExtra(CategoriesProvider.Columns.CATEGORY_NAME, holder.mCategoryName.getText().toString());
                    mContext.startActivity(intent);
                }
            }
        });
    }

    private void setOnItemMenuClickListener(final ViewHolder holder) {
        holder.mItemMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = ((AppCompatActivity) mContext).getSupportFragmentManager();
                CategoryOptionsDialog.newInstance(holder.categoryId).show(fm, "CategoryOptionsDialog");
            }
        });
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView mCategoryName = itemView.findViewById(R.id.item_fragment_category_recycler_view_text);
        ImageView mItemMenu = itemView.findViewById(R.id.item_fragment_category_recycler_view_image_menu);
        int categoryId;

        ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
