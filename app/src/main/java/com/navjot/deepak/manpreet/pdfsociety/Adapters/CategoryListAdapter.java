package com.navjot.deepak.manpreet.pdfsociety.Adapters;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.navjot.deepak.manpreet.pdfsociety.Models.Category;
import com.navjot.deepak.manpreet.pdfsociety.R;

public class CategoryListAdapter extends ArrayAdapter {

    Context context;
    List<Category> categories;
    ImageView category_pic_id;
    TextView category_name;

    public CategoryListAdapter(Context context, List<Category> categories) {
        super(context, R.layout.item_category_list);
        this.context = context;
        this.categories = categories;
    }

    @Override
    public int getCount() {
        return categories.size();
    }

    @Override
    public Object getItem(int position) {
        return categories.get(position);
    }

    @Override
    public long getItemId(int position) {
        return categories.indexOf(getItem(position));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

            View row = mInflater.inflate(R.layout.item_category_list, null);

            category_pic_id =  row.findViewById(R.id.category_pic);
            category_name =  row.findViewById(R.id.category_name);

            Category category_pos = categories.get(position);


            Log.d("Pdfsociety", "category_pos: "+category_pos);

            category_pic_id.setImageResource(category_pos.getCategory_pic_id());
            category_name.setText(category_pos.getCategory_name());


        return row;
    }
}
 