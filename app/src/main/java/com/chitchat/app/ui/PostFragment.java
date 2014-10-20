package com.chitchat.app.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.chitchat.app.R;
import com.chitchat.app.io.RestClient;
import com.chitchat.app.io.model.PostCallback;
import com.chitchat.app.io.model.PostData;
import com.chitchat.app.util.Clog;
import com.viewpagerindicator.TitlePageIndicator;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Huteri on 10/18/2014.
 */
public class PostFragment extends Fragment {
    public static final int NUM_POST_PER_PAGE = 5;
    private static final int NUM_POST_REQUEST = 0;
    private Integer currentPage = 1;
    private Bundle mArgs;
    private ViewPager mViewPager;
    private PagerAdapter mPagerAdapter;
    private TitlePageIndicator mIndicator;
    private Integer mThreadId;
    private ArrayList<PostData> mPostData;
    private boolean mIsLastData = false;
    private int mTotalPostData = 0;
    private ProgressBar mProgressBar;

    public PostFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_post, container, false);
        mViewPager = (ViewPager) view.findViewById(R.id.pager);
        mIndicator = (TitlePageIndicator) view.findViewById(R.id.indicator);
        mPagerAdapter = new PagerAdapter(getActivity(), getChildFragmentManager(), mViewPager);

        mIndicator.setViewPager(mViewPager);
        mIndicator.setTextColor(getResources().getColor(R.color.pressed_grey));
        mIndicator.setSelectedColor(getResources().getColor(R.color.accent_color));
        mIndicator.setFooterColor(getResources().getColor(R.color.divider));
        mIndicator.setFooterIndicatorStyle(TitlePageIndicator.IndicatorStyle.Triangle);

        mPostData = new ArrayList<PostData>();
        mArgs = getArguments();
        mThreadId = Integer.valueOf(mArgs.getString("threadId"));

        mProgressBar = (ProgressBar) view.findViewById(R.id.progress_bar);


        getCurrentPostData();


        return view;
    }

    private void getCurrentPostData() {
        Clog.d("");
        if (mPostData.size() == 0 || mTotalPostData > mPostData.size()) {
            mProgressBar.setVisibility(View.VISIBLE);
            RestClient.get().getPosts(mThreadId, NUM_POST_REQUEST, currentPage, new Callback<PostCallback>() {
                @Override
                public void success(PostCallback postCallback, Response response) {
                    mProgressBar.setVisibility(View.GONE);
                    if (postCallback.getSuccess() == 1) {
                        mTotalPostData = postCallback.getTotal();
                        if (postCallback.getCount() > 0) {
                            mPostData.addAll(postCallback.getData());

                            int numPage = (int) Math.ceil(postCallback.getCount() / (float) NUM_POST_PER_PAGE);
                            Clog.d("numPage : " + postCallback.getCount() + "/" + NUM_POST_PER_PAGE + " = " + numPage);

                            Bundle args = new Bundle();
                            args.putSerializable("data", mPostData);
                            int size = mPagerAdapter.getCount();
                            for (int i = size; i < size + numPage; i++)
                                mPagerAdapter.addPage(getActivity().getString(R.string.general_page) + " " + (i + 1), args);

                            mPagerAdapter.notifyDataSetChanged();
                        } else {
                            mIsLastData = true;
                        }

                    }


                }

                @Override
                public void failure(RetrofitError error) {
                    mProgressBar.setVisibility(View.GONE);
                }
            });
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_last:
                if (mTotalPostData > 0) {
                    int totalPage = (int) Math.ceil(mTotalPostData / (float) NUM_POST_PER_PAGE);
                    mIndicator.onPageSelected(totalPage);
                }
                break;
            case R.id.action_first:
                mIndicator.onPageSelected(0);
                break;
            case R.id.action_new:
                break;
        }
        return true;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    private class PagerAdapter extends FragmentStatePagerAdapter implements ViewPager.OnPageChangeListener {

        private final Context mContext;
        ArrayList<PagerInfo> mPager = new ArrayList<PagerInfo>();

        public PagerAdapter(Context context, FragmentManager fm, ViewPager viewPager) {
            super(fm);
            mContext = context;

            mViewPager.setAdapter(this);
            mIndicator.setOnPageChangeListener(this);

        }


        @Override
        public CharSequence getPageTitle(int position) {
            return mPager.get(position).getName();
        }

        public void addPage(String name, Bundle args) {
            PagerInfo pager = new PagerInfo();
            pager.setName(name);
            pager.setArgs(args);

            mPager.add(pager);
            notifyDataSetChanged();
        }


        @Override
        public Fragment getItem(int i) {
            PagerInfo info = mPager.get(i);
            Bundle args = info.getArgs();
            args.putInt("position", i);


            return PagePostFragment.newInstance(mContext, args);
        }

        @Override
        public void onPageScrolled(int i, float v, int i2) {

        }

        @Override
        public void onPageSelected(int i) {
            Clog.d(i + " mPager.size() : " + mPager.size());

            mViewPager.setCurrentItem(i);

            if (i == mPager.size() - 1 && !mIsLastData) {
                currentPage++;
                getCurrentPostData();
            }
        }


        @Override
        public void onPageScrollStateChanged(int i) {

        }


        @Override
        public int getCount() {
            return mPager.size();
        }

        private class PagerInfo {
            private Bundle args;
            private String name;

            public Bundle getArgs() {
                return args;
            }

            public void setArgs(Bundle args) {
                this.args = args;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }
    }
}
