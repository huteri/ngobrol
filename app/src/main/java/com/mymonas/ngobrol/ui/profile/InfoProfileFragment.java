package com.mymonas.ngobrol.ui.profile;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

import com.mymonas.ngobrol.R;
import com.mymonas.ngobrol.model.UserData;
import com.mymonas.ngobrol.adapter.InfoProfileListAdapter;
import com.mymonas.ngobrol.model.InfoProfileListItem;
import com.mymonas.ngobrol.util.Clog;

import java.util.ArrayList;

/**
 * Created by Huteri on 10/28/2014.
 */
public class InfoProfileFragment extends ScrollTabHolderFragment implements AbsListView.OnScrollListener {

    private ArrayList<InfoProfileListItem> mListItems;
    private UserData mUserData;
    private ListView mListView;
    private int mPosition;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        mUserData = (UserData) args.getSerializable(ProfileActivity.KEY_EXTRA_USER_DATA);
        mPosition = args.getInt(ProfileActivity.KEY_EXTRA_POSITION);

        mListItems = new ArrayList<InfoProfileListItem>();

        InfoProfileListItem item = new InfoProfileListItem();
        item.setText(mUserData.getAboutMe());
        item.setTitle(getActivity().getString(R.string.profile_about_title));

        mListItems.add(item);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Clog.d("");

        View view = inflater.inflate(R.layout.fragment_info_profile, container, false);

        mListView = (ListView) view.findViewById(R.id.listview);

        View placeHolderView = inflater.inflate(R.layout.header_kenburns_placeholder, mListView, false);
        mListView.addHeaderView(placeHolderView, null, false);

        return view;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Clog.d("");
        mListView.setOnScrollListener(this);

        InfoProfileListAdapter adapter = new InfoProfileListAdapter(getActivity(), mListItems);
        mListView.setAdapter(adapter);

    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int i) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

        if (mScrollTabHolder != null)
            mScrollTabHolder.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount, mPosition);

    }

    @Override
    public void adjustScroll(int scrollHeight) {
        if(scrollHeight == 0 && mListView.getFirstVisiblePosition() >= 1) {
            return;
        }

        mListView.setSelectionFromTop(1, scrollHeight);
    }
}
