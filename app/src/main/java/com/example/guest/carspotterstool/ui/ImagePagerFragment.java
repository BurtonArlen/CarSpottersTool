package com.example.guest.carspotterstool.ui;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.guest.carspotterstool.R;
import com.example.guest.carspotterstool.models.PhotoContribution;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class ImagePagerFragment extends Fragment implements View.OnClickListener{
    private ArrayList<PhotoContribution> photoContributions;
    private PhotoContribution contribution;
    private int mPosition;
    @Bind(R.id.scrollPhoto)ImageView scrollPhoto;
    @Bind(R.id.photoCredit)TextView photoCredit;

    public static ImagePagerFragment newInstance(ArrayList<PhotoContribution> photoContributions, Integer position){
        ImagePagerFragment imagePagerFragment = new ImagePagerFragment();
        Bundle args = new Bundle();
        args.putParcelable("contributions", Parcels.wrap(photoContributions));
        args.putInt("position", position);
        imagePagerFragment.setArguments(args);
        return imagePagerFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        photoContributions = Parcels.unwrap(getArguments().getParcelable("contributions"));
        mPosition = getArguments().getInt("position");
        contribution = photoContributions.get(mPosition);
    }


    public ImagePagerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image_pager, container, false);
        ButterKnife.bind(this, view);
        scrollPhoto.setImageBitmap(stringToBitMap(contribution.getImageEncoded()));
        scrollPhoto.setOnClickListener(this);
        return view;
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

    @Override
    public void onClick(View v){
        if (v == scrollPhoto){
            Log.d("clicked scroll photo", contribution.getMake());
        }
    }

}
