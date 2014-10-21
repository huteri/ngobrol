package com.mymonas.ngobrol.ui;

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

/**
 * Created by Huteri on 10/21/2014.
 */
public class ThreadFragment extends Fragment{
    private ViewPager mViewPager;
    private ThreadPagerAdapter mPagerAdapter;

    public ThreadFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_thread, container, false);

        mPagerAdapter = new ThreadPagerAdapter(getActivity().getSupportFragmentManager());
        mViewPager = (ViewPager) view.findViewById(R.id.pager);
        mViewPager.setAdapter(mPagerAdapter);

        PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) view.findViewById(R.id.tabs);
        tabs.setDividerColor(getResources().getColor(R.color.divider));
        tabs.setTextColor(getResources().getColor(R.color.theme_color));
        tabs.setIndicatorColor(getResources().getColor(R.color.theme_color));
        tabs.setShouldExpand(true);
        tabs.setViewPager(mViewPager);

        return view;
    }

    public class ThreadPagerAdapter extends FragmentStatePagerAdapter {

        String[] titles = {"Categories", "Hot Threads", "New Threads"};

        public ThreadPagerAdapter(FragmentManager fm) {
            super(fm);

        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }

        @Override
        public Fragment getItem(int i) {
            return Fragment.instantiate(getActivity(), HotThreadFragment.class.getName());
        }

        @Override
        public int getCount() {
            return titles.length;
        }
    }



}
