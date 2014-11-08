package com.mymonas.ngobrol.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.makeramen.RoundedImageView;
import com.mymonas.ngobrol.R;
import com.mymonas.ngobrol.model.PostData;
import com.mymonas.ngobrol.ui.profile.ProfileActivity;
import com.mymonas.ngobrol.util.Clog;
import com.mymonas.ngobrol.util.UserUtils;
import com.mymonas.ngobrol.util.Utils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import java.util.ArrayList;

/**
 * Created by Huteri on 10/19/2014.
 */
public class PostAdapter extends ArrayAdapter<PostData> {
    private final Context mContext;
    private final ArrayList<PostData> mPostData;
    private final DisplayImageOptions mImageOptions;

    private OnEditPostListener mOnEditPostListener;

    public void setOnEditPostListener(OnEditPostListener mOnEditPostListener) {
        this.mOnEditPostListener = mOnEditPostListener;
    }

    public interface OnEditPostListener {
        public void onEditPost(PostData postData);
    }


    public PostAdapter(Context context, ArrayList<PostData> postData) {
        super(context, R.layout.item_post, postData);
        mContext = context;
        mPostData = postData;

        mImageOptions = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.drawable.nophoto)
                .showImageOnFail(R.drawable.nophoto)
                .showImageOnLoading(R.drawable.nophoto)
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder holder;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_post, parent, false);

            holder = new ViewHolder();
            holder.tvText = (TextView) view.findViewById(R.id.text);
            holder.tvName = (TextView) view.findViewById(R.id.name);
            holder.tvDate = (TextView) view.findViewById(R.id.date);
            holder.tvPostOrder = (TextView) view.findViewById(R.id.post_order);
            holder.profileImg = (RoundedImageView) view.findViewById(R.id.profile_img);
            holder.btnMenu = (Button) view.findViewById(R.id.btn_post_menu);
            holder.tvLastEdited = (TextView) view.findViewById(R.id.tv_last_edited);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.tvText.setText(mPostData.get(position).getText());
        holder.tvName.setText(mPostData.get(position).getUser().getUsername());
        holder.tvDate.setText(mPostData.get(position).getDateCreated());
        holder.tvPostOrder.setText("#"+(position+1));
        holder.profileImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ProfileActivity.class);
                intent.putExtra(ProfileActivity.KEY_EXTRA_USER_DATA, mPostData.get(position).getUser());
                mContext.startActivity(intent);
            }
        });
        holder.btnMenu.setOnClickListener(getBtnMenuClickListener(position, holder.btnMenu));
        hideMenuOnNoPrivilegeUsers(position, holder);
        showLastEditedIfAvailable(position, holder.tvLastEdited);
        ImageLoader.getInstance().displayImage(mPostData.get(position).getUser().getProfileUrl(), holder.profileImg, mImageOptions);

        return view;

    }

    private void showLastEditedIfAvailable(int position, TextView tvLastEdited) {
        if(mPostData.get(position).getIsModified() == 1) {
            tvLastEdited.setText(mContext.getString(R.string.post_item_last_edited)+" "+mPostData.get(position).getTimestamp());
            tvLastEdited.setVisibility(View.VISIBLE);
        }
    }

    private void hideMenuOnNoPrivilegeUsers(int position, ViewHolder holder) {
        UserUtils userUtils = new UserUtils(mContext);

        Clog.d("userUtils.isModerator : "+userUtils.isModerator());
        if(mPostData.get(position).getUser().getId() != userUtils.getUserId() && !userUtils.isModerator()) {
           holder.btnMenu.setVisibility(View.GONE);

            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) holder.tvDate.getLayoutParams();
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            holder.tvDate.setLayoutParams(layoutParams);
            holder.tvDate.setPadding(holder.tvDate.getPaddingLeft(), holder.tvDate.getPaddingTop(), (int) Utils.dpToPx(mContext, 10), holder.tvDate.getPaddingBottom());

            layoutParams = (RelativeLayout.LayoutParams) holder.tvPostOrder.getLayoutParams();
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            holder.tvPostOrder.setLayoutParams(layoutParams);
            holder.tvPostOrder.setPadding(holder.tvPostOrder.getPaddingLeft(), holder.tvPostOrder.getPaddingTop(), (int) Utils.dpToPx(mContext, 10), holder.tvPostOrder.getPaddingBottom());

        }
    }

    private View.OnClickListener getBtnMenuClickListener(final int position, final Button btnMenu) {
        Clog.d("");
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu menu = new PopupMenu(mContext, btnMenu);
                menu.getMenuInflater().inflate(R.menu.item_post, menu.getMenu());
                menu.setOnMenuItemClickListener(getOnMenuItemPostClickListener(position));
                menu.show();

            }
        };
    }

    private PopupMenu.OnMenuItemClickListener getOnMenuItemPostClickListener(final int position) {
        Clog.d("");
        return new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch(menuItem.getItemId()) {
                    case R.id.edit_post:
                        mOnEditPostListener.onEditPost(mPostData.get(position));
                        break;
                }
                return true;
            }
        };
    }


    private static class ViewHolder {
        TextView tvText;
        TextView tvName;
        TextView tvDate;
        TextView tvPostOrder;
        RoundedImageView profileImg;
        Button btnMenu;
        public TextView tvLastEdited;
    }
}
