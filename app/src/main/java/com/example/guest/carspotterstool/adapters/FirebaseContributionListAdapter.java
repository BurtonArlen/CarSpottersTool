package com.example.guest.carspotterstool.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.guest.carspotterstool.Constants;
import com.example.guest.carspotterstool.R;
import com.example.guest.carspotterstool.models.PhotoContribution;
import com.example.guest.carspotterstool.ui.ContributionDetailActivity;
import com.example.guest.carspotterstool.ui.SearchActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.parceler.Parcels;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Guest on 6/20/17.
 */

public class FirebaseContributionListAdapter extends RecyclerView.Adapter<FirebaseContributionListAdapter.FirebaseUserViewHolder> {
    private ArrayList<PhotoContribution> userContributions = new ArrayList<>();
    private ArrayList<PhotoContribution> similarMakeContributions = new ArrayList<>();
    private ArrayList<PhotoContribution> similarModelContributions = new ArrayList<>();
    private ArrayList<PhotoContribution> similarContributions = new ArrayList<>();
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
            getSimilarContributionsMake(itemPosition);

            getSimilarContributionsModel(itemPosition);

            compareContributions(similarMakeContributions, similarModelContributions);
            Intent intent = new Intent(mContext, ContributionDetailActivity.class);
            intent.putExtra("position", itemPosition);
            intent.putExtra("contributions", Parcels.wrap(similarContributions));
            mContext.startActivity(intent);
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
        private void getSimilarContributionsMake(int itemPosition){
            final ArrayList<PhotoContribution> makeContributions = new ArrayList<>();
            PhotoContribution selectedContribution = userContributions.get(itemPosition);
            DatabaseReference userRef = FirebaseDatabase.getInstance()
                    .getReference(Constants.FIREBASE_CHILD_CONTRIBUTIONS)
                    .child(Constants.FIREBASE_CHILD_CAR_CONTRIBUTIONS)
                    .child(Constants.FIREBASE_CHILD_MAKE)
                    .child(selectedContribution.getMake());
            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        makeContributions.add(snapshot.getValue(PhotoContribution.class));
                    }
                    similarMakeContributions = makeContributions;
                    Log.d("datacheck", similarMakeContributions.get(0).getPushId());
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    databaseError.getMessage();
                }
            });
        }
        private void getSimilarContributionsModel(int itemPosition){
            final ArrayList<PhotoContribution> modelContributions = new ArrayList<>();
            PhotoContribution selectedContribution = userContributions.get(itemPosition);
            DatabaseReference userRef = FirebaseDatabase.getInstance()
                    .getReference(Constants.FIREBASE_CHILD_CONTRIBUTIONS)
                    .child(Constants.FIREBASE_CHILD_CAR_CONTRIBUTIONS)
                    .child(Constants.FIREBASE_CHILD_MODEL)
                    .child(selectedContribution.getModel());
            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        modelContributions.add(snapshot.getValue(PhotoContribution.class));
                    }
                    similarModelContributions = modelContributions;
                    Log.d("datacheck", similarModelContributions.get(0).getPushId());
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    databaseError.getMessage();
                }
            });
        }
        public void compareContributions(ArrayList<PhotoContribution> make, ArrayList<PhotoContribution> model){
            final ArrayList<PhotoContribution> comparedContributions = new ArrayList<>();
            for (int i=0; i >= make.size(); i++){
                for (int x=0; x >= model.size(); x++){
                    if (make.get(i).getMake() == model.get(x).getMake() && make.get(i).getModel() == model.get(x).getModel()){
                        comparedContributions.add(make.get(i));
                    }
                }
            }
            similarContributions = comparedContributions;
        }
    }
}
