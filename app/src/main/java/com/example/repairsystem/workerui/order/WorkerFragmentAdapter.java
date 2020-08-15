package com.example.repairsystem.workerui.order;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.repairsystem.ui.dashboard.TabFragment;

import java.util.List;

public class WorkerFragmentAdapter extends FragmentPagerAdapter {


    private List<WokerTabFragment> mFragmentList;//各导航的Fragment
    private List<String> mTitle; //导航的标题

    public WorkerFragmentAdapter(FragmentManager fragmentManager, List<WokerTabFragment> fragments, List<String>title){
        super(fragmentManager);
        mFragmentList=fragments;
        mTitle=title;

    }
    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }
    @Override
    public CharSequence getPageTitle(int position) {
        return mTitle.get(position);
    }
}
