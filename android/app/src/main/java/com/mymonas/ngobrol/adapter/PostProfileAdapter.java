package com.mymonas.ngobrol.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.mymonas.ngobrol.R;
import com.mymonas.ngobrol.model.PostData;

import java.util.ArrayList;

/**
 * Created by Huteri on 11/11/2014.
 */
public class PostProfileAdapter extends ArrayAdapter<PostData> {
    private final Context mContext;
    private final ArrayList<PostData> mPostList;

    public PostProfileAdapter(Context context, ArrayList<PostData> postList) {
        super(context, R.layout.item_post_profile, postList);
        mContext = context;
        mPostList = postList;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder holder;

        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_post_profile, parent, false);

            holder = new ViewHolder();
            holder.tvTitle = (TextView) view.findViewById(R.id.title);
            holder.tvPreviewText = (TextView) view.findViewById(R.id.preview_text);
            holder.tvCategory = (TextView) view.findViewById(R.id.category);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.tvTitle.setText(mPostList.get(position).getThread().getTitle());
        holder.tvPreviewText.setText(mPostList.get(position).getText());
        holder.tvCategory.setText(mPostList.get(position).getThread().getCategory().getName());

        return view;
    }

    private class ViewHolder {
        public TextView tvTitle;
        public TextView tvPreviewText;
        public TextView tvCategory;
    }
}
