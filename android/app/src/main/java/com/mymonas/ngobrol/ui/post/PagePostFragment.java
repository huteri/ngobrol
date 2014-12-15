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
import com.mymonas.ngobrol.adapter.PostAdapter;
import com.mymonas.ngobrol.model.PostData;
import com.mymonas.ngobrol.model.ThreadItem;
import com.mymonas.ngobrol.util.Clog;

import java.util.ArrayList;

/**
 * Created by Huteri on 10/19/2014.
 */
public class PagePostFragment extends Fragment implements PostAdapter.OnEditPostListener, PostFragment.OnSetOffsetListview {
    public static final String KEY_EXTRA_POST_DATA = "data";
    public static final java.lang.String KEY_EXTRA_POSITION = "position";
    private Bundle mArgs = null;
    private ListView mPostListView;
    private ArrayList<PostData> mPostData;
    private ThreadItem mThreadData;

    private PostAdapter.OnEditPostListener mOnEditPostListener;
    private int mCurrentPage;


    public PagePostFragment() {
    }

    public static Fragment newInstance(Context context, Bundle args, PostAdapter.OnEditPostListener editPostListener) {
        PagePostFragment fragment = new PagePostFragment();
        fragment.setArguments(args);
        fragment.setOnEditPostListener(editPostListener);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mArgs = getArguments();
        mPostData = (ArrayList<PostData>) mArgs.getSerializable(KEY_EXTRA_POST_DATA);
        mThreadData = (ThreadItem) mArgs.getSerializable(PostFragment.KEY_EXTRA_THREAD_DATA);
        mCurrentPage = mArgs.getInt(KEY_EXTRA_POSITION);

        PostFragment parentFragment = (PostFragment) getParentFragment();
        parentFragment.setOnSetOffsetListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_page_post, container, false);
        mPostListView = (ListView) view.findViewById(R.id.post_lv);
        PostAdapter postAdapter = new PostAdapter(getActivity(), mPostData);

        int position = mArgs.getInt(KEY_EXTRA_POSITION);
        if (position == 0) {
            View headerView = inflater.inflate(R.layout.header_page_post, mPostListView, false);
            TextView title = (TextView) headerView.findViewById(R.id.title);
            title.setText(mThreadData.getTitle());
            mPostListView.addHeaderView(headerView);
        }

        postAdapter.setOnEditPostListener(this);
        mPostListView.setAdapter(postAdapter);
        return view;
    }


    @Override
    public void onEditPost(PostData postData) {
        mOnEditPostListener.onEditPost(postData);
    }

    @Override
    public void onDeletePost(PostData postData) {
        mOnEditPostListener.onDeletePost(postData);
    }

    public void setOnEditPostListener(PostAdapter.OnEditPostListener mOnEditPostListener) {
        this.mOnEditPostListener = mOnEditPostListener;
    }

    @Override
    public void onSetOffset(int page, int position) {
        Clog.d("");

        if(mCurrentPage == page) {
            mPostListView.setSelection(position);
        }
    }
}
