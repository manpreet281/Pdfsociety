package com.navjot.deepak.manpreet.pdfsociety.Adapters;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.navjot.deepak.manpreet.pdfsociety.Models.Category;
import com.navjot.deepak.manpreet.pdfsociety.R;

public class CategoryListAdapter extends BaseAdapter {

    Context context;
    List<Category> categories;

    public CategoryListAdapter(Context context, List<Category> categories) {
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

    /* private view holder class */
    private class ViewHolder {
        ImageView category_pic_id;
        TextView category_name;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;

        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);


        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_category_list, null);
            holder = new ViewHolder();

            holder.category_pic_id = (ImageView) convertView
                    .findViewById(R.id.category_pic);
            holder.category_name = (TextView) convertView
                    .findViewById(R.id.category_name);

            Category category_pos = categories.get(position);

            holder.category_pic_id.setImageResource(category_pos.getCategory_pic_id());
            holder.category_name.setText(category_pos.getCategory_name());

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        return convertView;
    }
}
 