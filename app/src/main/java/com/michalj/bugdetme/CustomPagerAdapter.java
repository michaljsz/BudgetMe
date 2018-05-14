package com.michalj.bugdetme;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class CustomPagerAdapter extends FragmentStatePagerAdapter {

    private int mNumOfTabs;

    public CustomPagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                HomeFragment tab1 = new HomeFragment();
                return tab1;
            case 1:
                ExpensesListFragment tab2 = new ExpensesListFragment();
                return tab2;
            case 2:
                StatsFragment tab3 = new StatsFragment();
                return  tab3;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}