package dev.nelson.mot.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import dev.nelson.mot.R;
import dev.nelson.mot.db.model.CategoriesProvider;


public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder>{

    private Context mContext;
    private CursorAdapter mCursorAdapter;
    private TextView categoryName;

    public CategoryAdapter(Context context, Cursor cursor ){
        mContext = context;
        mCursorAdapter = new CursorAdapter(mContext, cursor, 0) {
            @Override
            public View newView(Context context, Cursor cursor, ViewGroup parent) {
                return LayoutInflater.from(parent.getContext()).inflate(R.layout.item_fragment_category_recycler_view_, parent, false);
            }

            @Override
            public void bindView(View view, Context context, Cursor cursor) {
                categoryName = (TextView) view.findViewById(R.id.item_fragment_category_recycler_view_text);
                ImageView itemMenu = (ImageView) view.findViewById(R.id.item_fragment_category_recycler_view_image_menu);
                categoryName.setText(cursor.getString(cursor.getColumnIndex(CategoriesProvider.Columns.CATEGORY_NAME)));
                itemMenu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(mContext, categoryName.getText().toString(), Toast.LENGTH_SHORT).show();
                    }
                });
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



    //butter knife doesn't work here %(
    class ViewHolder extends RecyclerView.ViewHolder{

        ViewHolder(View itemView) {
            super(itemView);
//            ButterKnife.bind(this, itemView);
        }
    }
}
