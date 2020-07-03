package com.rainbow.superschedule.adapter;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

/**
 * SuperSchedule
 * Created By Rainbow on 2020/5/8.
 */
public class SchedulePagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> mFragmentList;
    private String[] titles;

    public SchedulePagerAdapter(@NonNull FragmentManager fm, List<Fragment> fragments,String[] titles) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        mFragmentList = fragments;
        this.titles = titles;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
