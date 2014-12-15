package com.mymonas.ngobrol.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.mymonas.ngobrol.R;
import com.mymonas.ngobrol.adapter.ThreadAdapter;
import com.mymonas.ngobrol.io.RestCallback;
import com.mymonas.ngobrol.io.RestClient;
import com.mymonas.ngobrol.io.model.ThreadCallback;
import com.mymonas.ngobrol.model.ThreadItem;
import com.mymonas.ngobrol.model.UserData;
import com.mymonas.ngobrol.ui.post.PostActivity;
import com.mymonas.ngobrol.ui.post.PostFragment;

import java.util.ArrayList;

import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Huteri on 11/2/2014.
 */
public class ThreadProfileFragment extends ScrollTabHolderFragment implements AbsListView.OnScrollListener {

    private static final int THREAD_POSITION = 1;
    private ArrayList<ThreadItem> mThreadList;
    private ListView mListView;
    private FragmentActivity mContext;
    private ThreadAdapter mThreadAdapter;
    private UserData mUserData;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = getActivity();
        Bundle args = getArguments();
        mUserData = (UserData) args.getSerializable(ProfileActivity.KEY_EXTRA_USER_DATA);
        mThreadList = new ArrayList<ThreadItem>();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mListView.setOnScrollListener(this);
        mThreadAdapter = new ThreadAdapter(mContext, mThreadList);
        mListView.setAdapter(mThreadAdapter);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_thread, container, false);

        mListView = (ListView) view.findViewById(R.id.thread_list);

        View placeHolderView = inflater.inflate(R.layout.header_kenburns_placeholder, mListView, false);
        mListView.addHeaderView(placeHolderView, null, false);


        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Bundle args = new Bundle();
                args.putSerializable(PostFragment.KEY_EXTRA_THREAD_DATA, mThreadList.get(i-1));

                Intent intent = new Intent(getActivity(), PostActivity.class);
                intent.putExtras(args);
                startActivity(intent);
            }
        });

        final ProgressBar pBar = (ProgressBar) view.findViewById(R.id.progress_bar);

        pBar.setVisibility(View.VISIBLE);
        RestClient.get().getThreads(null, 0, String.valueOf(mUserData.getId()), new RestCallback<ThreadCallback>(mContext) {
            @Override
            public void success(ThreadCallback threadCallback, Response response) {
                super.success(threadCallback, response);
                pBar.setVisibility(View.GONE);
                if(threadCallback.getCount() > 0) {
                    mThreadList.addAll(threadCallback.getData());
                    mThreadAdapter.notifyDataSetChanged();
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
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if(mScrollTabHolder != null) {
            mScrollTabHolder.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount, THREAD_POSITION);
        }
    }

    @Override
    public void adjustScroll(int scrollHeight) {
        if(scrollHeight == 0 && mListView.getFirstVisiblePosition() >= 1)
            return;

        mListView.setSelectionFromTop(1, scrollHeight);
    }
}
