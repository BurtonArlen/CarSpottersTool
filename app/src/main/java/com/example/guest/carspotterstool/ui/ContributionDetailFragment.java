package com.example.guest.carspotterstool.ui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.guest.carspotterstool.R;
import com.example.guest.carspotterstool.models.PhotoContribution;

import org.parceler.Parcels;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContributionDetailFragment extends Fragment implements View.OnClickListener{
    public PhotoContribution mContribution = new PhotoContribution();
    @Bind(R.id.viewPager)ViewPager viewPager;
    @Bind(R.id.makeText)TextView makeText;
    @Bind(R.id.modelText)TextView modelText;
    @Bind(R.id.yearText)TextView yearText;
    @Bind(R.id.upVoteButton)Button upVoteButton;

    public static ContributionDetailFragment newInstance(PhotoContribution mContribution){
        ContributionDetailFragment fragDetail = new ContributionDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable("contribution", Parcels.wrap(mContribution));
        fragDetail.setArguments(args);
        return fragDetail;
    }


    public ContributionDetailFragment() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contribution_detail, container, false);
        ButterKnife.bind(this, view);
        upVoteButton.setOnClickListener(this);
        makeText.setText(mContribution.getMake());
        modelText.setText(mContribution.getModel());
        yearText.setText(mContribution.getYear());


        // Inflate the layout for this fragment
        return view;
    }
    @Override
    public void onClick(View v){
        if (v == upVoteButton){
            Log.d("vote", "YouVotedSticker.jpg");
        }
    }

}
