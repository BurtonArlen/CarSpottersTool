package com.example.guest.carspotterstool.ui;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.example.guest.carspotterstool.R;
import com.example.guest.carspotterstool.models.PhotoContribution;

import org.parceler.Parcels;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ContributionDetailActivity extends AppCompatActivity {

    @Bind(R.id.container) FrameLayout container;
//    private MoviePagerAdapter adapterViewPager;
    ContributionDetailFragment detailFragment = new ContributionDetailFragment();
    PhotoContribution fragmentFocus;
    ArrayList<PhotoContribution> userContributions = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contribution_detail);
        ButterKnife.bind(this);
        userContributions = Parcels.unwrap(getIntent().getParcelableExtra("contributions"));
        int startingPosition = getIntent().getIntExtra("position", 0);
        fragmentFocus = userContributions.get(startingPosition);
        makeFragment();
    }
        private void makeFragment(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.container, detailFragment.newInstance(fragmentFocus)).commit();

    }
}
