package dev.nelson.mot.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import dev.nelson.mot.R;
import dev.nelson.mot.db.model.CategoriesProvider;

public class CategoryContentActivity extends AppCompatActivity {

    @BindView(R.id.category_content_text)
    TextView mCategoryName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_content);
        ButterKnife.bind(this);
        String s = getIntent().getStringExtra(CategoriesProvider.Columns.CATEGORY_NAME);
        mCategoryName.setText(s);

    }
}
