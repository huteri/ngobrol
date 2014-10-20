package com.mymonas.ngobrol.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mymonas.ngobrol.R;
import com.mymonas.ngobrol.ui.model.DrawerListItem;

import java.util.ArrayList;

/**
 * Created by Huteri on 10/15/2014.
 */
public class DrawerListAdapter extends ArrayAdapter<DrawerListItem>{


    private final Context mContext;
    private final ArrayList<DrawerListItem> mDrawerList;

    public DrawerListAdapter(Context context, ArrayList<DrawerListItem> drawerList) {
        super(context, R.layout.drawer_list_item, drawerList);

        mContext = context;
        mDrawerList = drawerList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.drawer_list_item, parent, false);

        TextView tv = (TextView) view.findViewById(R.id.drawer_textview);
        ImageView iv = (ImageView) view.findViewById(R.id.drawer_imageview);

        tv.setText(mDrawerList.get(position).getTitle());
        iv.setBackgroundResource(mDrawerList.get(position).getRes());

        return view;
    }
}
