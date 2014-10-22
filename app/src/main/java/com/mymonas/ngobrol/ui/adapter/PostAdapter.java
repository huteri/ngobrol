package com.mymonas.ngobrol.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.mymonas.ngobrol.R;
import com.mymonas.ngobrol.io.model.PostData;
import com.mymonas.ngobrol.util.Clog;

import java.util.ArrayList;

/**
 * Created by Huteri on 10/19/2014.
 */
public class PostAdapter extends ArrayAdapter<PostData> {
    private final Context mContext;
    private final ArrayList<PostData> mPostData;

    public PostAdapter(Context context, ArrayList<PostData> postData) {
        super(context, R.layout.post_item, postData);
        mContext = context;
        mPostData = postData;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.post_item, parent, false);

        TextView tvText = (TextView) view.findViewById(R.id.text);
        TextView tvName = (TextView) view.findViewById(R.id.name);
        TextView tvDate = (TextView) view.findViewById(R.id.date);

        Clog.d(mPostData.get(position).getText());

        tvText.setText(mPostData.get(position).getText());
        tvName.setText(mPostData.get(position).getUser().getUsername());
        tvDate.setText(mPostData.get(position).getDateCreated());

        return view;

    }
}
