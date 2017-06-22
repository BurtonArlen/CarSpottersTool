package com.example.guest.carspotterstool.ui;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.guest.carspotterstool.R;
import com.example.guest.carspotterstool.models.PhotoContribution;

import org.parceler.Parcels;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContributionDetailFragment extends Fragment implements View.OnClickListener {
    public PhotoContribution mContribution;
    private ArrayList<PhotoContribution> photoContributions;
    private int mPosition;

    @Bind(R.id.makeText)TextView makeText;
    @Bind(R.id.modelText)TextView modelText;
    @Bind(R.id.yearText)TextView yearText;
    @Bind(R.id.upVoteButton)Button upVoteButton;
    @Bind(R.id.scrollPhoto)ImageView scrollPhoto;
    @Bind(R.id.photoCredit)TextView photoCredit;

    public static ContributionDetailFragment newInstance(ArrayList<PhotoContribution> contributions, Integer position){
        ContributionDetailFragment fragDetail = new ContributionDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable("contributions", Parcels.wrap(contributions));
        args.putInt("position", position);
        fragDetail.setArguments(args);
        return fragDetail;
    }

    public ContributionDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        photoContributions = Parcels.unwrap(getArguments().getParcelable("contributions"));
        mPosition = getArguments().getInt("position");
        mContribution = photoContributions.get(mPosition);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contribution_detail, container, false);
        Log.d("dataPassCheck", mContribution.getMake());
        Log.d("dataPassCheck", mContribution.getModel());
        Log.d("dataPassCheck", mContribution.getSubmitterName());
        Log.d("dataPassCheck", mContribution.getYear());
        Log.d("dataPassCheck", mContribution.getImageEncoded());
        ButterKnife.bind(this, view);
        scrollPhoto.setImageBitmap(stringToBitMap(mContribution.getImageEncoded()));
        photoCredit.setText("Photo Credit: " + mContribution.getSubmitterName());
        makeText.setText("Make: " + mContribution.getMake());
        modelText.setText("Model: " + mContribution.getModel());
        yearText.setText("Year: " + mContribution.getYear());
        upVoteButton.setOnClickListener(this);
        scrollPhoto.setOnClickListener(this);
        return view;
    }
    @Override
    public void onClick(View v){
        if (v == upVoteButton){
            Log.d("vote", "YouVotedSticker.jpg");
            Intent intent = new Intent(getActivity(), IntentUserProfile.class);
            intent.putExtra("uid", mContribution.getSubmitterId());
            intent.putExtra("displayName", mContribution.getSubmitterName());
            startActivity(intent);
        }
        if (v == scrollPhoto){
            Log.d("get submitter Id", "go to users profile");
        }
    }
    public Bitmap stringToBitMap(String encodedString){
        try {
            byte [] encodeByte= Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch(Exception e) {
            e.getMessage();
            return null;
        }
    }
}
