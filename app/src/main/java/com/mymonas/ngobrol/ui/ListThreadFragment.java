package com.mymonas.ngobrol.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mymonas.ngobrol.R;
import com.mymonas.ngobrol.io.RestClient;
import com.mymonas.ngobrol.io.model.ThreadCallback;
import com.mymonas.ngobrol.model.CategoryItem;
import com.mymonas.ngobrol.model.ThreadItem;
import com.mymonas.ngobrol.ui.adapter.ThreadAdapter;
import com.mymonas.ngobrol.util.Clog;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ListThreadFragment extends Fragment {
    public static final String KEY_EXTRA_CATEGORY_DATA = "categoryData";
    public static final String KEY_EXTRA_SORT_POPULAR = "sortPopular";
    private Context mContext;
    private ArrayList<ThreadItem> mThreadList;
    private ThreadAdapter mThreadAdapter;
    private String mCategoryId = null;
    private int mSortPopular = 1;
    private CategoryItem mCategoryData;

    public ListThreadFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();

        if(getArguments() != null) {
            if(getArguments().getSerializable(KEY_EXTRA_CATEGORY_DATA) != null) {
                mCategoryData = (CategoryItem) getArguments().getSerializable(KEY_EXTRA_CATEGORY_DATA);
                mCategoryId = String.valueOf(mCategoryData.getId());
            }

            Clog.d(getArguments().getBoolean(KEY_EXTRA_SORT_POPULAR));
            if(!getArguments().getBoolean(KEY_EXTRA_SORT_POPULAR)) {
                mSortPopular = 0;
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_list_thread, container, false);

        mThreadList = new ArrayList<ThreadItem>();
        mThreadAdapter = new ThreadAdapter(mContext, mThreadList);

        final ProgressBar pBar = (ProgressBar) view.findViewById(R.id.progress_bar);

        pBar.setVisibility(View.VISIBLE);
        Clog.d("mCategoryId : "+mCategoryId);
        RestClient.get().getThreads(mCategoryId, mSortPopular, new Callback<ThreadCallback>() {
            @Override
            public void success(ThreadCallback threadCallback, Response response) {
                pBar.setVisibility(View.GONE);
                if(threadCallback.getCount() > 0) {
                    mThreadList.addAll(threadCallback.getData());
                    mThreadAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                pBar.setVisibility(View.GONE);
                Clog.d(error.getCause().toString());
            }
        });

        ListView threadLv = (ListView) view.findViewById(R.id.thread_list);

        View headerView = inflater.inflate(R.layout.header_thread, threadLv, false);
        TextView tvHeader = (TextView) headerView.findViewById(R.id.header_title);

        if(mCategoryData != null) {
            LinearLayout layoutHeader = (LinearLayout) headerView.findViewById(R.id.layout_header);
            layoutHeader.setBackgroundColor(Color.parseColor(mCategoryData.getColor()));
        }

        if(mSortPopular == 1) {
            tvHeader.setText(mContext.getString(R.string.thread_most_popular));
        } else {
            tvHeader.setText(mContext.getString(R.string.thread_most_recent));
        }

        threadLv.addHeaderView(headerView);

        threadLv.setAdapter(mThreadAdapter);
        threadLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Bundle args = new Bundle();
                args.putSerializable(PostFragment.KEY_EXTRA_THREAD_DATA, mThreadList.get(i-1));

                Intent intent = new Intent(getActivity(), PostActivity.class);
                intent.putExtras(args);
                startActivity(intent);
            }
        });

        return view;
    }
}
