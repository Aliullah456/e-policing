package com.example.abcd;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class cpageAdapter extends FragmentPagerAdapter {
    @NonNull

    private int numOfTabs;
    cpageAdapter(FragmentManager fm, int numOfTabs)
    {
        super(fm);
        this.numOfTabs=numOfTabs;
    }
    public Fragment getItem(int position) {
        switch(position)
        {
            case 0:
                complainFragment complainFragment=new complainFragment();
                return complainFragment;
            case 1:
                followFragment followFragment=new followFragment();
                return followFragment;
            case 2:
                profileFragment profileFragment=new profileFragment();
                return profileFragment;
            default:
                return null;

        }
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
}

