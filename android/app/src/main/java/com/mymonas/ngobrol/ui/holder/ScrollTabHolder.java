package com.mymonas.ngobrol.ui.holder;

import android.widget.AbsListView;

/**
 * Created by Huteri on 10/26/2014.
 */
public interface ScrollTabHolder {

    void adjustScroll(int scrollHeight);

    void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount, int pagePosition);
}
