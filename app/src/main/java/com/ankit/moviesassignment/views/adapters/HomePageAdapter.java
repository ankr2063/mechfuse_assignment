package com.ankit.moviesassignment.views.adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.ankit.moviesassignment.views.fragments.NowPlayingFragment;
import com.ankit.moviesassignment.views.fragments.UpcomingFragment;

public class HomePageAdapter extends FragmentStatePagerAdapter {

    int mNumOfTabs;

    public HomePageAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                NowPlayingFragment tab1 = new NowPlayingFragment();
                return tab1;
            case 1:
                UpcomingFragment tab2 = new UpcomingFragment();
                return tab2;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
