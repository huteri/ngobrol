package com.mymonas.ngobrol.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.mymonas.ngobrol.R;
import com.mymonas.ngobrol.model.CategoryItem;

import java.util.ArrayList;

/**
 * Created by Huteri on 10/31/2014.
 */
public class SpinnerCategoryAdapter extends ArrayAdapter<CategoryItem> {
    private final Context mContext;
    private final ArrayList<CategoryItem> mList;
    private final LayoutInflater mInflater;

    public SpinnerCategoryAdapter(Context context, ArrayList<CategoryItem> list) {
        super(context, R.layout.item_spinner_category, list);
        mContext = context;
        mList = list;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if(view == null) {

            view = mInflater.inflate(R.layout.item_spinner_category, parent, false);
        }

        TextView tvName = (TextView) view.findViewById(R.id.category_name);
        tvName.setText(mList.get(position).getName());

        return view;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if(view == null) {
            view = mInflater.inflate(R.layout.item_spinner_category, parent, false);
        }

        TextView tvName = (TextView) view.findViewById(R.id.category_name);
        tvName.setText(mList.get(position).getName());

        return view;
    }
}
