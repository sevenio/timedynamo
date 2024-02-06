package com.tvisha.trooptime.activity.activity.Helper;

/*public class MainPagerAdapter {
}*/

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.tvisha.trooptime.activity.activity.HomeFragment;


public class MainPagerAdapter extends FragmentStatePagerAdapter {

    //integer to count number of tabs
    int tabCount;

    //Constructor to the class
    public MainPagerAdapter(FragmentManager fm, int tabCount) {
        super(fm);
        //Initializing tab count
        this.tabCount = tabCount;
    }

    //Overriding method getItem
    @Override
    public Fragment getItem(int position) {
        //Returning the current tabs
        switch (position) {
            case 0:
                //BroadcastFragment tab1 = new BroadcastFragment();
                HomeFragment tab1 = new HomeFragment();
                return tab1;


            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}
