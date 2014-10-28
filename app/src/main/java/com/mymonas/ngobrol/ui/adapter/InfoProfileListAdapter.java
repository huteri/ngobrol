package com.mymonas.ngobrol.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.mymonas.ngobrol.R;
import com.mymonas.ngobrol.ui.model.InfoProfileListItem;
import com.mymonas.ngobrol.util.Clog;

import java.util.ArrayList;

/**
 * Created by Huteri on 10/28/2014.
 */
public class InfoProfileListAdapter extends ArrayAdapter<InfoProfileListItem> {

    private final Context mContext;
    private final ArrayList<InfoProfileListItem> mList;

    public InfoProfileListAdapter(Context context, ArrayList<InfoProfileListItem> list) {
        super(context, R.layout.item_info_profile, list);
        mContext = context;
        mList = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Clog.d("");
        View view = convertView;
        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_info_profile, parent, false);

            holder = new ViewHolder();

            holder.tvTitle = (TextView) view.findViewById(R.id.title);
            holder.tvText = (TextView) view.findViewById(R.id.text);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.tvTitle.setText(mList.get(position).getTitle());
        holder.tvText.setText(mList.get(position).getText());

        return view;
    }

    private static class ViewHolder {
        TextView tvTitle, tvText;
    }
}
