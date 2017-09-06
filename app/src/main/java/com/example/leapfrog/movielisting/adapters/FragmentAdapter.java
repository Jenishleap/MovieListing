package com.example.leapfrog.movielisting.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.leapfrog.movielisting.fragments.MovieListFragment;

import java.util.List;

import static com.example.leapfrog.movielisting.MovieListActivity.NOWPLAYING;
import static com.example.leapfrog.movielisting.MovieListActivity.POPULAR;
import static com.example.leapfrog.movielisting.MovieListActivity.TOPRATED;
import static com.example.leapfrog.movielisting.MovieListActivity.UPCOMING;


public class FragmentAdapter extends FragmentPagerAdapter {
    List<Fragment> fragments;
    private static int NUM_OF_FRAGMENTS = 4;


    public FragmentAdapter(FragmentManager fm) {
        super(fm);

    }


    public FragmentAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
//        return this.fragments.get(position);


        switch (position) {
            case 0:
                return MovieListFragment.newInstance(POPULAR);
            case 1:
                return MovieListFragment.newInstance(TOPRATED);
            case 2:
                return MovieListFragment.newInstance(UPCOMING);
            case 3:
                return MovieListFragment.newInstance(NOWPLAYING);
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return NUM_OF_FRAGMENTS;
    }


    @Override
    public CharSequence getPageTitle(int position) {


        switch (position) {
            case 0:
                return "POPULAR";
            case 1:
                return "TOPRATED";
            case 2:
                return "UPCOMING";
            case 3:
                return "NOWPLAYING";
            default:
                return null;

        }
    }
}
