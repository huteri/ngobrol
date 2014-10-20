package com.chitchat.app.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.chitchat.app.R;
import com.chitchat.app.io.model.PostData;

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
        tvText.setText(mPostData.get(position).getText());

        return view;

    }
}
