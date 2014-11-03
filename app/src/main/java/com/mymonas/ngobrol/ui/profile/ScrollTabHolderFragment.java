package com.mymonas.ngobrol.ui.profile;

import android.support.v4.app.Fragment;
import android.widget.AbsListView;

import com.mymonas.ngobrol.ui.holder.ScrollTabHolder;

/**
 * Created by Huteri on 10/26/2014.
 */
public class ScrollTabHolderFragment extends Fragment implements ScrollTabHolder{

    protected ScrollTabHolder mScrollTabHolder;

    public void setScrollTabHolder(ScrollTabHolder mScrollTabHolder) {
        this.mScrollTabHolder = mScrollTabHolder;
    }

    @Override
    public void adjustScroll(int scrollHeight) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount, int pagePosition) {

    }
}
