package com.mymonas.ngobrol.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.StateListDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mymonas.ngobrol.R;
import com.mymonas.ngobrol.model.CategoryItem;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import java.util.ArrayList;

/**
 * Created by Huteri on 10/21/2014.
 */
public class CategoryAdapter extends ArrayAdapter<CategoryItem> {

    private final Context mContext;
    private final ArrayList<CategoryItem> mList;
    private final DisplayImageOptions mImageOptions;

    public CategoryAdapter(Context context,ArrayList<CategoryItem> list) {
        super(context, R.layout.category_item, list);
        mContext = context;
        mList = list;

        mImageOptions = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.drawable.ic_question)
                .showImageOnFail(R.drawable.ic_question)
                .showImageOnLoading(R.drawable.ic_question)
                .resetViewBeforeLoading(true)
                .cacheOnDisk(true)
                .cacheInMemory(true)
                .considerExifParams(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .displayer(new FadeInBitmapDisplayer(300))
                .build();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.category_item, parent, false);


        LinearLayout layout = (LinearLayout) view.findViewById(R.id.layout);
        TextView name = (TextView) view.findViewById(R.id.name);
        ImageView icon = (ImageView) view.findViewById(R.id.icon);

        int color = Color.parseColor(mList.get(position).getColor());
        int newColor = Color.argb(100, Color.red(color), Color.green(color), Color.blue(color));

        StateListDrawable drawable = new StateListDrawable();
        drawable.addState(new int[] { android.R.attr.state_pressed}, new ColorDrawable(newColor));
        drawable.addState(new int[] { -android.R.attr.state_pressed}, new ColorDrawable(color));

        layout.setBackgroundDrawable(drawable);
        name.setText(mList.get(position).getName());

        ImageLoader.getInstance().displayImage(mList.get(position).getIcon(), icon, mImageOptions);

        return view;
    }
}
