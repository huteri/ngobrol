package com.mymonas.ngobrol.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.astuetz.PagerSlidingTabStrip;
import com.mymonas.ngobrol.R;
import com.mymonas.ngobrol.model.CategoryItem;

import java.util.ArrayList;

/**
 * Created by Huteri on 10/21/2014.
 */
public class ThreadFragment extends Fragment {
    private ViewPager mViewPager;
    private ThreadPagerAdapter mPagerAdapter;
    private boolean mIsCategoryActivity = false;
    private CategoryItem mCategoryData;

    public ThreadFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null && getArguments().getSerializable(CategoryThreadActivity.KEY_EXTRA_CATEGORY_DATA) != null) {
            mIsCategoryActivity = true;
            mCategoryData = (CategoryItem) getArguments().getSerializable(CategoryThreadActivity.KEY_EXTRA_CATEGORY_DATA);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_thread, container, false);

        PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) view.findViewById(R.id.tabs);
        mPagerAdapter = new ThreadPagerAdapter(getActivity().getSupportFragmentManager());
        mViewPager = (ViewPager) view.findViewById(R.id.pager);
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setAdapter(mPagerAdapter);

        Bundle args = new Bundle();
        args.putBoolean(ListThreadFragment.KEY_EXTRA_SORT_POPULAR, true);

        if (mIsCategoryActivity) {
            tabs.setIndicatorColor(Color.parseColor(mCategoryData.getColor()));
            args.putString(ListThreadFragment.KEY_EXTRA_CATEOGORY_ID, String.valueOf(mCategoryData.getId()));
            mPagerAdapter.addPage(getActivity().getString(R.string.general_hot_threads), ListThreadFragment.class, args, R.drawable.ic_tab_popular);

            args = new Bundle();
            args.putBoolean(ListThreadFragment.KEY_EXTRA_SORT_POPULAR, false);
            mPagerAdapter.addPage("New", ListThreadFragment.class, args, R.drawable.ic_tab_recent);
        } else {
            mPagerAdapter.addPage(getActivity().getString(R.string.general_categories), CategoryFragment.class, args, R.drawable.ic_tab_category);
            mPagerAdapter.addPage(getActivity().getString(R.string.general_hot_threads), ListThreadFragment.class, args, R.drawable.ic_tab_popular);

            args = new Bundle();
            args.putBoolean(ListThreadFragment.KEY_EXTRA_SORT_POPULAR, false);
            mPagerAdapter.addPage("New", ListThreadFragment.class, args, R.drawable.ic_tab_recent);
            mViewPager.setCurrentItem(1);

        }

        tabs.setViewPager(mViewPager);


        return view;
    }

    public class ThreadPagerAdapter extends FragmentStatePagerAdapter implements PagerSlidingTabStrip.IconTabProvider {

        ArrayList<PagerInfo> mPager = new ArrayList<PagerInfo>();

        @Override
        public int getPageIconResId(int position) {
            return mPager.get(position).getIcon();
        }

        class PagerInfo {
            private final Class<?> clss;
            private final Bundle args;
            private final String name;
            private final int icon;

            PagerInfo(Class<?> clss, Bundle args, String name, int icon) {
                this.clss = clss;
                this.args = args;
                this.name = name;
                this.icon = icon;
            }

            public Class<?> getClss() {
                return clss;
            }

            public Bundle getArgs() {
                return args;
            }

            public String getName() {
                return name;
            }

            public int getIcon() {
                return icon;
            }
        }

        public void addPage(String name, Class<?> clss, Bundle args, int icon) {
            PagerInfo pagerInfo = new PagerInfo(clss, args, name, icon);
            mPager.add(pagerInfo);
            notifyDataSetChanged();
        }

        public ThreadPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mPager.get(position).getName();
        }

        @Override
        public Fragment getItem(int i) {
            return Fragment.instantiate(getActivity(), mPager.get(i).getClss().getName(), mPager.get(i).getArgs());
        }

        @Override
        public int getCount() {
            return mPager.size();
        }
    }


}
