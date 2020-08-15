package com.example.repairsystem.workerui.order;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.repairsystem.R;

import com.example.repairsystem.ui.dashboard.TabFragment;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class orderFragment extends Fragment {
    private ViewPager pager;
    private WorkerFragmentAdapter fragmentAdapter;
    private List<WokerTabFragment> fragmentList;
    private TabLayout tabLayout;
    private TabFragment fragment1, fragment2, fragment3;
    private List<String> mTitles;
    // private String [] title={"我的报修","我要报修","我的工单"};
    private String[] title = {"任务工单", "进行中", "历史订单"};

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home_dashboard, container, false);

        pager = view.findViewById(R.id.page2);
        tabLayout = view.findViewById(R.id.tab_layout2);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        fragmentList = new ArrayList<>();
        mTitles = new ArrayList<>();
        for (int i = 0; i < title.length; i++) {
            mTitles.add(title[i]);
            fragmentList.add(new WokerTabFragment(title[i]));
        }

        fragmentAdapter=new WorkerFragmentAdapter(getChildFragmentManager(),fragmentList,mTitles);
        pager.setAdapter(fragmentAdapter);
        tabLayout.setupWithViewPager(pager);//与ViewPage建立关系
    }

}
