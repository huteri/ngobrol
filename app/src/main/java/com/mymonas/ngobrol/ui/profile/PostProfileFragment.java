package com.mymonas.ngobrol.ui.profile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.mymonas.ngobrol.R;
import com.mymonas.ngobrol.adapter.PostProfileAdapter;
import com.mymonas.ngobrol.io.RestCallback;
import com.mymonas.ngobrol.io.RestClient;
import com.mymonas.ngobrol.io.model.PostCallback;
import com.mymonas.ngobrol.model.PostData;
import com.mymonas.ngobrol.model.UserData;
import com.mymonas.ngobrol.ui.post.PostActivity;
import com.mymonas.ngobrol.ui.post.PostFragment;

import java.util.ArrayList;

import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Huteri on 11/11/2014.
 */
public class PostProfileFragment extends ScrollTabHolderFragment implements AbsListView.OnScrollListener, AdapterView.OnItemClickListener {

    private static final int POST_POSITION = 2;
    private Context mContext;
    private UserData mUserData;
    private ArrayList<PostData> mPostList;
    private ListView mListView;
    private PostProfileAdapter mPostAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        Bundle args = getArguments();
        mUserData = (UserData) args.getSerializable(ProfileActivity.KEY_EXTRA_USER_DATA);
        mPostList = new ArrayList<PostData>();

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mListView.setOnScrollListener(this);
        mPostAdapter = new PostProfileAdapter(mContext, mPostList);
        mListView.setAdapter(mPostAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // reuse fragment list thread layout
        View view = inflater.inflate(R.layout.fragment_list_thread, container, false);

        mListView = (ListView) view.findViewById(R.id.thread_list);

        View placeHolder = inflater.inflate(R.layout.header_kenburns_placeholder, mListView, false);
        mListView.addHeaderView(placeHolder, null, false);

        mListView.setOnItemClickListener(this);

        final ProgressBar pBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        pBar.setVisibility(View.VISIBLE);

        RestClient.get().getPosts(0, mUserData.getId(), 0, 0, new RestCallback<PostCallback>(mContext) {
            @Override
            public void success(PostCallback o, Response response) {
                super.success(o, response);
                pBar.setVisibility(View.GONE);
                if(o.getCount() > 0) {
                    mPostList.addAll(o.getData());
                    mPostAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                super.failure(error);
                pBar.setVisibility(View.GONE);
            }
        });

        return view;
    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int i) {

    }

    @Override
    public void onScroll(AbsListView absListView, int i, int i2, int i3) {
        if(mScrollTabHolder != null) {
            mScrollTabHolder.onScroll(absListView, i, i2, i3, POST_POSITION);
        }
    }

    @Override
    public void adjustScroll(int scrollHeight) {
        super.adjustScroll(scrollHeight);

       if(scrollHeight == 0 && mListView.getFirstVisiblePosition() >= 1)
           return;

        mListView.setSelectionFromTop(1, scrollHeight);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Bundle args = new Bundle();
        args.putSerializable(PostFragment.KEY_EXTRA_THREAD_DATA, mPostList.get(i-1).getThread());
        args.putInt(PostFragment.KEY_EXTRA_SET_OFFSET_POST, mPostList.get(i-1).getId());

        Intent intent = new Intent(mContext, PostActivity.class);
        intent.putExtras(args);
        startActivity(intent);
    }
}
