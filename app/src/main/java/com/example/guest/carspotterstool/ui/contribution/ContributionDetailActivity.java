package com.example.guest.carspotterstool.ui.contribution;

import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.example.guest.carspotterstool.R;
import com.example.guest.carspotterstool.adapters.ImagePagerAdapter;
import com.example.guest.carspotterstool.models.PhotoContribution;

import org.parceler.Parcels;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ContributionDetailActivity extends AppCompatActivity {
    @Bind(R.id.viewPager) ViewPager viewPager;
    private ImagePagerAdapter adapterViewPager;
    ArrayList<PhotoContribution> userContributions = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contribution_detail);
        ButterKnife.bind(this);
        userContributions = Parcels.unwrap(getIntent().getParcelableExtra("contributions"));
        int startingPosition = getIntent().getIntExtra("position", 0);
        FragmentManager fm = getSupportFragmentManager();
        adapterViewPager = new ImagePagerAdapter(fm, userContributions);
        viewPager.setAdapter(adapterViewPager);
        viewPager.setCurrentItem(startingPosition);
//        makeImagePager(startingPosition, userContributions);
    }
//    private void makeImagePager(int position, ArrayList<PhotoContribution> userContributions){
//        FragmentManager fm = getSupportFragmentManager();
//        adapterViewPager = new ImagePagerAdapter(fm, userContributions);
//        viewPager.setAdapter(adapterViewPager);
//        viewPager.setCurrentItem(position);
//    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        userContributions.clear();
    }

}
