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
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import dev.nelson.mot.R;
import dev.nelson.mot.activity.CategoryContentActivity;
import dev.nelson.mot.db.model.CategoriesProvider;
import dev.nelson.mot.dialog.DialogCategoryOptions;


public class CategoriesAdapter extends CursorRecyclerAdapter<CategoriesAdapter.ViewHolder> {

    private Context mContext;

    public CategoriesAdapter(Context context, Cursor c) {
        super(c);
        mContext = context;
    }

    @Override
    public CategoriesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_fragment_category_recycler_view_, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, Cursor cursor) {
        holder.mCategoryName.setText(cursor.getString(cursor.getColumnIndex(CategoriesProvider.Columns.CATEGORY_NAME)));
        holder.categoryId = (int)cursor.getLong(cursor.getColumnIndex(CategoriesProvider.Columns._ID));
        setOnItemClickListener(holder);
        setOnItemMenuClickListener(holder);
    }

    private void setOnItemClickListener(final ViewHolder holder){
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, CategoryContentActivity.class);
                intent.putExtra(CategoriesProvider.Columns.CATEGORY_NAME, holder.mCategoryName.getText().toString());
                mContext.startActivity(intent);
            }
        });
    }

    private void setOnItemMenuClickListener(final ViewHolder holder){
        holder.mItemMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = ((AppCompatActivity) mContext).getSupportFragmentManager();
                DialogCategoryOptions.newInstance(holder.categoryId).show(fm, "tag");
            }
        });
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.item_fragment_category_recycler_view_text)
        TextView mCategoryName;
        @BindView(R.id.item_fragment_category_recycler_view_image_menu)
        ImageView mItemMenu;
        int categoryId;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
