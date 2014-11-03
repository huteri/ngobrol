package com.mymonas.ngobrol.ui.post;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.mymonas.ngobrol.R;
import com.mymonas.ngobrol.model.PostData;
import com.mymonas.ngobrol.model.ThreadItem;
import com.mymonas.ngobrol.adapter.PostAdapter;

import java.util.ArrayList;

/**
 * Created by Huteri on 10/19/2014.
 */
public class PagePostFragment extends Fragment {
    public static final String KEY_EXTRA_POST_DATA = "data";
    private Bundle mArgs = null;
    private ListView mPostListView;

    public PagePostFragment() {


    }

    public static Fragment newInstance(Context context, Bundle args) {
        PagePostFragment fragment = new PagePostFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_page_post, container, false);
        mArgs = getArguments();
        ArrayList<PostData> postData = (ArrayList<PostData>) mArgs.getSerializable(KEY_EXTRA_POST_DATA);
        ThreadItem threadData = (ThreadItem) mArgs.getSerializable(PostFragment.KEY_EXTRA_THREAD_DATA);
        mPostListView = (ListView) view.findViewById(R.id.post_lv);
        PostAdapter postAdapter = new PostAdapter(getActivity(), postData);

        int position = mArgs.getInt("position");
        if (position == 0) {

            View headerView = inflater.inflate(R.layout.header_page_post, mPostListView, false);
            TextView title = (TextView) headerView.findViewById(R.id.title);
            title.setText(threadData.getTitle());
            mPostListView.addHeaderView(headerView);
        }

        mPostListView.setAdapter(postAdapter);

        return view;
    }


}
