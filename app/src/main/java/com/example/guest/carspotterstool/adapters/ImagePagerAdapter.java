package com.example.guest.carspotterstool.adapters;

import android.support.v4.app.FragmentPagerAdapter;
import com.example.guest.carspotterstool.models.PhotoContribution;
import com.example.guest.carspotterstool.ui.fragments.ContributionDetailFragment;

import java.util.ArrayList;

/**
 * Created by Guest on 6/21/17.
 */

public class ImagePagerAdapter extends FragmentPagerAdapter {
    private ArrayList<PhotoContribution> mContributions;
    public ImagePagerAdapter(android.support.v4.app.FragmentManager fm, ArrayList<PhotoContribution> contributions){
        super(fm);
        mContributions = contributions;
    }
    @Override
    public android.support.v4.app.Fragment getItem(int position) {
        return ContributionDetailFragment.newInstance(mContributions, position);
    }
    @Override
    public int getCount() {
        return mContributions.size();
    }
    @Override
    public CharSequence getPageTitle(int position) {
        CharSequence year = mContributions.get(position).getYear();
        return year;
    }
}
