package com.example.guest.carspotterstool.ui;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.guest.carspotterstool.R;
import com.example.guest.carspotterstool.models.PhotoContribution;

import org.parceler.Parcels;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContributionDetailFragment extends Fragment {
    public ArrayList<PhotoContribution> mContributions = new ArrayList<>();

    Activity activity;

    public static ContributionDetailFragment newInstance(ArrayList<PhotoContribution> mContributions){
        ContributionDetailFragment fragDetail = new ContributionDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable("contributions", Parcels.wrap(mContributions));
        fragDetail.setArguments(args);
        return fragDetail;
    }


    public ContributionDetailFragment() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_contribution_detail, container, false);
    }

}
