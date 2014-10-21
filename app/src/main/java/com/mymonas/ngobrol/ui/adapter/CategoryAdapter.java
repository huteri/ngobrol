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
 * Created by Huteri on 10/21/2014.
 */
public class CategoryAdapter extends ArrayAdapter<CategoryItem> {

    private final Context mContext;
    private final ArrayList<CategoryItem> mList;

    public CategoryAdapter(Context context,ArrayList<CategoryItem> list) {
        super(context, R.layout.category_item, list);
        mContext = context;
        mList = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.category_item, parent, false);

        TextView name = (TextView) view.findViewById(R.id.name);
        name.setText(mList.get(position).getName());

        return view;
    }
}
