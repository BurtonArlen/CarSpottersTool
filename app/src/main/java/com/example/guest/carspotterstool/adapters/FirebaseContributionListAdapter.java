package com.example.guest.carspotterstool.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.guest.carspotterstool.R;
import com.example.guest.carspotterstool.models.PhotoContribution;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Guest on 6/20/17.
 */

public class FirebaseContributionListAdapter extends RecyclerView.Adapter<FirebaseContributionListAdapter.FirebaseUserViewHolder> {
    private ArrayList<PhotoContribution> userContributions = new ArrayList<>();
    private Context mContext;
    public FirebaseContributionListAdapter(Context context, ArrayList<PhotoContribution> contributions){
        mContext = context;
        userContributions = contributions;
    }

    @Override
    public FirebaseContributionListAdapter.FirebaseUserViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contribution_tile, parent, false);
        FirebaseUserViewHolder viewHolder = new FirebaseUserViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(FirebaseContributionListAdapter.FirebaseUserViewHolder holder, int position){
        holder.bindContributions(userContributions.get(position));
    }

    @Override
    public int getItemCount(){
        return userContributions.size();
    }

    public  class FirebaseUserViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        @Bind(R.id.pictureView) ImageView pictureView;
        @Bind(R.id.makeText) TextView makeText;
        @Bind(R.id.modelText) TextView modelText;
        @Bind(R.id.yearText) TextView yearText;
        @Bind(R.id.photoCredit) TextView photoCredit;
        private Context mContext;
        public FirebaseUserViewHolder(View itemView){
            super(itemView);
            ButterKnife.bind(this, itemView);
            mContext = itemView.getContext();
            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View v){
            int itemPosition = getLayoutPosition();
            Log.d("placeholder log",String.valueOf(itemPosition));
        }
        public void bindContributions(PhotoContribution contribution){
            makeText.setText(contribution.getMake());
            modelText.setText(contribution.getModel());
            yearText.setText(contribution.getYear());
            photoCredit.setText("Photo Credit: " + contribution.getSubmitterName());
            pictureView.setImageBitmap(stringToBitMap(contribution.getImageEncoded()));
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
}
