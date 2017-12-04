package com.navjot.deepak.manpreet.pdfsociety.Activities;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.navjot.deepak.manpreet.pdfsociety.Adapters.CategoryListAdapter;
import com.navjot.deepak.manpreet.pdfsociety.Models.Category;
import com.navjot.deepak.manpreet.pdfsociety.R;

public class CategoryListActivity extends AppCompatActivity implements OnItemClickListener {

    TypedArray category_pic;
    String[] category_name;
    List<Category> categories;
    ListView mylistview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_list);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        categories = new ArrayList<Category>();

        category_pic = getResources().obtainTypedArray(R.array.categories_pics);

        category_name = getResources().getStringArray(R.array.categories_name);




        for (int i = 0; i < category_name.length; i++) {
            Category item = new Category(category_name[i], category_pic.getResourceId(i,-1));
            categories.add(item);
        }

        mylistview = (ListView) findViewById(R.id.list);
        CategoryListAdapter adapter = new CategoryListAdapter(this, categories);
        mylistview.setAdapter(adapter);
        mylistview.setOnItemClickListener(this);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        startActivity(
                new Intent(CategoryListActivity.this, CategoryActivity.class)
                    .putExtra("CategoryName", categories.get(position).getCategory_name())
        );
    }

    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
