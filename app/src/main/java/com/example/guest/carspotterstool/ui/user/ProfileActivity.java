package com.example.guest.carspotterstool.ui.user;

import android.content.Context;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.guest.carspotterstool.Constants;
import com.example.guest.carspotterstool.R;
import com.example.guest.carspotterstool.adapters.FirebaseContributionListAdapter;
import com.example.guest.carspotterstool.models.PhotoContribution;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ProfileActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private Context mContext;
    private ArrayList<PhotoContribution> mUserContributions;
    private static final int MAX_WIDTH = 120;
    private static final int MAX_HEIGHT = 120;
    @Bind(R.id.userName) TextView userName;
    @Bind(R.id.profilePicture) ImageView profilePicture;
    @Bind(R.id.recyclerView) RecyclerView recyclerView;
    @Bind(R.id.userScore) TextView mUserScore;
    @Bind(R.id.userStanding) TextView userStanding;
    @Bind(R.id.userContributionCount) TextView userContributionCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Context mContext = getApplicationContext();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();
        ButterKnife.bind(this);
        mAuth = FirebaseAuth.getInstance();
        String displayName = mAuth.getCurrentUser().getDisplayName();
        Uri profilePic = mAuth.getCurrentUser().getPhotoUrl();
        userName.setText(displayName);

        setTitle(displayName + "'s CarSpottersTool");

        Picasso.with(mContext).load(profilePic)
                .resize(MAX_WIDTH, MAX_HEIGHT)
                .centerCrop()
                .into(profilePicture);
        getContributions(uid, displayName);
    }
    private void getContributions(String uid, final String displayName){
        final ArrayList<PhotoContribution> userContributions = new ArrayList<>();
        DatabaseReference userRef = FirebaseDatabase.getInstance()
                .getReference(Constants.FIREBASE_CHILD_CONTRIBUTIONS)
                .child(Constants.FIREBASE_CHILD_USERS)
                .child(uid)
                .child(Constants.FIREBASE_CHILD_CAR_CONTRIBUTIONS);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    userContributions.add(snapshot.getValue(PhotoContribution.class));
                }
                mUserContributions = userContributions;
                int contributionCount = userContributions.size();
                int score = contributionCount*10;
                int standing = 1;
                mUserScore.setText(displayName + "'s Score: " + String.valueOf(score));
                userContributionCount.setText(displayName + "'s Contribution Count: " + String.valueOf(contributionCount));
                userStanding.setText(displayName + "'s Leaderboard Standing of #" + String.valueOf(standing));
                FirebaseContributionListAdapter adapter = new FirebaseContributionListAdapter(getApplicationContext(), mUserContributions);
                recyclerView.setAdapter(adapter);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ProfileActivity.this);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setHasFixedSize(true);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                databaseError.getMessage();
            }
        });
    }
}
