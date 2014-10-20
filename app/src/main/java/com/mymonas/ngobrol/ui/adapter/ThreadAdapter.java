package com.mymonas.ngobrol.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.mymonas.ngobrol.R;
import com.mymonas.ngobrol.model.ThreadItem;

import java.util.ArrayList;

/**
 * Created by Huteri on 10/16/2014.
 */
public class ThreadAdapter extends ArrayAdapter<ThreadItem>{
    private final ArrayList<ThreadItem> mThreadList;
    private final Context mContext;

    public ThreadAdapter(Context context, ArrayList<ThreadItem> threadList) {
        super(context, R.layout.thread_item, threadList);
        mContext = context;
        mThreadList = threadList;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.thread_item, parent, false);
        TextView title = (TextView) view.findViewById(R.id.title);

        title.setText(mThreadList.get(position).getTitle());
        return view;
    }
}
