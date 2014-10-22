package com.mymonas.ngobrol.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mymonas.ngobrol.R;
import com.mymonas.ngobrol.io.RestClient;
import com.mymonas.ngobrol.io.model.BaseCallback;
import com.mymonas.ngobrol.io.model.PostCallback;
import com.mymonas.ngobrol.io.model.PostData;
import com.mymonas.ngobrol.util.Clog;
import com.mymonas.ngobrol.util.UserUtil;
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
    private Integer mCurrentRequestedPage = 1;
    private Bundle mArgs;
    private ViewPager mViewPager;
    private PagerAdapter mPagerAdapter;
    private TitlePageIndicator mIndicator;
    private Integer mThreadId;
    private ArrayList<PostData> mPostData;
    private boolean mIsLastData = false;
    private int mTotalPostData = 0;
    private ProgressBar mProgressBar;
    private UserUtil mUserUtil;
    private int mCurrentPage = 0;

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
        mIndicator.setSelectedColor(getResources().getColor(R.color.theme_color));
        mIndicator.setFooterColor(getResources().getColor(R.color.divider));
        mIndicator.setFooterIndicatorStyle(TitlePageIndicator.IndicatorStyle.Triangle);

        mPostData = new ArrayList<PostData>();
        mArgs = getArguments();
        mThreadId = Integer.valueOf(mArgs.getString("threadId"));
        mUserUtil = new UserUtil(getActivity());

        mProgressBar = (ProgressBar) view.findViewById(R.id.progress_bar);


        getCurrentPostData();


        return view;
    }

    private void getCurrentPostData() {
        Clog.d("");
        if (mPostData.size() == 0 || mTotalPostData > mPostData.size()) {
            mProgressBar.setVisibility(View.VISIBLE);
            RestClient.get().getPosts(mThreadId, NUM_POST_REQUEST, mCurrentRequestedPage, new Callback<PostCallback>() {
                @Override
                public void success(PostCallback postCallback, Response response) {
                    mProgressBar.setVisibility(View.GONE);
                    if (postCallback.getSuccess() == 1) {
                        mTotalPostData = postCallback.getTotal();
                        if (postCallback.getCount() > 0) {
                            mPostData.addAll(postCallback.getData());

                            int numPage = (int) Math.ceil(postCallback.getCount() / (float) NUM_POST_PER_PAGE);
                            Clog.d("numPage : " + postCallback.getCount() + "/" + NUM_POST_PER_PAGE + " = " + numPage);

                            Bundle args;
                            ArrayList<PostData> tempList;

                            int size = mPagerAdapter.getCount();
                            int maxPage = size+numPage;
                            for (int i = size; i < maxPage; i++) {
                                Clog.d("Add page with position : " + i);
                                tempList = getPostDataBasedOnPosition(i);

                                args = new Bundle();
                                args.putSerializable("data", tempList);

                                mPagerAdapter.addPage(getActivity().getString(R.string.general_page) + " " + (i + 1)+ "/"+maxPage, args);
                            }


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

    private ArrayList<PostData> getPostDataBasedOnPosition(int pos) {
        int offset = pos * NUM_POST_PER_PAGE;
        ArrayList<PostData> currentPostData = new ArrayList<PostData>();

        int nextSet;
        if ((offset + NUM_POST_PER_PAGE) > mPostData.size())
            nextSet = mPostData.size();
        else
            nextSet = offset + NUM_POST_PER_PAGE;

        for (int j = offset; j < nextSet; j++) {
            currentPostData.add(mPostData.get(j));
        }
        return currentPostData;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_last:
                if (mTotalPostData > 0) {
                    int totalPage = (int) Math.ceil(mTotalPostData / (float) NUM_POST_PER_PAGE);
                    mViewPager.setCurrentItem(totalPage);
                }
                break;
            case R.id.action_first:
                mIndicator.onPageSelected(0);
                break;
            case R.id.action_new:
                addNewPost();
                break;
        }
        return true;
    }

    private void addNewPost() {
        // error on no user available
        if (!mUserUtil.isAvailable()) {
            Toast.makeText(getActivity(), getActivity().getString(R.string.message_no_user), Toast.LENGTH_SHORT).show();
            return;
        }
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setTitle(getActivity().getString(R.string.post_add_new));

        final View view = ((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.dialog_post_add_new, null);
        TextView whoPost = (TextView) view.findViewById(R.id.whopost);
        whoPost.setText(getActivity().getString(R.string.dialog_whopost) + " " + mUserUtil.getUsername());

        dialog.setView(view);

        dialog.setPositiveButton(getActivity().getString(R.string.general_ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialogInterface, int i) {
                ProgressBar pBar = (ProgressBar) view.findViewById(R.id.pBar);
                EditText text = (EditText) view.findViewById(R.id.text);
                pBar.setVisibility(View.VISIBLE);

                RestClient.get().submitPost(mThreadId, mUserUtil.getUserId(), mUserUtil.getAPI(), mUserUtil.getAndroidId(), text.getText().toString(), new Callback<BaseCallback>() {
                    @Override
                    public void success(BaseCallback baseCallback, Response response) {
                        if (baseCallback.getSuccess() == 1) {
                            reloadTheFragment();
                            Toast.makeText(getActivity(), getActivity().getString(R.string.post_success), Toast.LENGTH_SHORT).show();
                            dialogInterface.dismiss();

                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        dialogInterface.dismiss();
                    }
                });

            }
        });
        dialog.setNegativeButton(getActivity().getString(R.string.general_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        dialog.create();
        dialog.show();
    }

    private void reloadTheFragment() {
      mPostData.clear();
      mPagerAdapter.removeAllPages();
      getCurrentPostData();

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
            Clog.d(i);
            PagerInfo info = mPager.get(i);
            Bundle args = info.getArgs();

            return PagePostFragment.newInstance(mContext, args);
        }

        @Override
        public void onPageScrolled(int i, float v, int i2) {

        }

        @Override
        public void onPageSelected(int i) {
            Clog.d(i);
            mCurrentPage = i;
            mViewPager.setCurrentItem(i);

            if (i == mPager.size() - 1 && !mIsLastData) {
                mCurrentRequestedPage++;
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

        public void removeAllPages() {
            mPager.clear();
            notifyDataSetChanged();
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
