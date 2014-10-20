package com.mymonas.ngobrol.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.mymonas.ngobrol.R;
import com.mymonas.ngobrol.io.model.PostData;
import com.mymonas.ngobrol.ui.adapter.PostAdapter;
import com.mymonas.ngobrol.util.Clog;

import java.util.ArrayList;

/**
 * Created by Huteri on 10/19/2014.
 */
public class PagePostFragment extends Fragment {

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

        Bundle args = getArguments();

        ArrayList<PostData> postData = (ArrayList<PostData>) args.getSerializable("data");
        Clog.d(postData.toString());

        int currentPage = args.getInt("position");
        int offset = (currentPage) * PostFragment.NUM_POST_PER_PAGE;
        Clog.d("Current Page :"+currentPage);
        ArrayList<PostData> currentPostData = new ArrayList<PostData>();

        int nextSet;
        if((offset+PostFragment.NUM_POST_PER_PAGE) > postData.size())
            nextSet = postData.size();
        else
            nextSet = offset+PostFragment.NUM_POST_PER_PAGE;

        for (int i = offset; i < nextSet; i++) {
                currentPostData.add(postData.get(i));
        }

        PostAdapter postAdapter = new PostAdapter(getActivity(), currentPostData);

        mPostListView = (ListView) view.findViewById(R.id.post_lv);
        mPostListView.setAdapter(postAdapter);

        return view;
    }


}
