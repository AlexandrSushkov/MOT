package dev.nelson.mot.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import dev.nelson.mot.R;
import dev.nelson.mot.db.model.CategoriesProvider;


public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    private Context mContext;
    private CursorAdapter mCursorAdapter;

    static class ViewHolder extends RecyclerView.ViewHolder{

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }


    public CategoryAdapter(Context context, Cursor cursor ){
        mContext = context;
        mCursorAdapter = new CursorAdapter(mContext, cursor, 0) {
            @Override
            public View newView(Context context, Cursor cursor, ViewGroup parent) {
                return LayoutInflater.from(parent.getContext()).inflate(R.layout.item_fragment_category_recycler_view_, parent, false);
            }

            @Override
            public void bindView(View view, Context context, Cursor cursor) {
                TextView categoryName = (TextView) view.findViewById(R.id.item_fragment_category_recycler_view_text);
                categoryName.setText(cursor.getString(cursor.getColumnIndex(CategoriesProvider.Columns.CATEGORY_NAME)));
            }
        };
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mCursorAdapter.newView(mContext, mCursorAdapter.getCursor(), parent);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        mCursorAdapter.getCursor().moveToPosition(position);
        mCursorAdapter.bindView(holder.itemView, mContext, mCursorAdapter.getCursor());
    }

    @Override
    public int getItemCount() {
        return mCursorAdapter.getCount();
    }

    public void swapCursor(Cursor cursor){
        mCursorAdapter.changeCursor(cursor);
    }

}
