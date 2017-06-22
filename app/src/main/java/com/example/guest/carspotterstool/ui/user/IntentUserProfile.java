package com.example.guest.carspotterstool.ui.user;

import android.content.Context;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.guest.carspotterstool.Constants;
import com.example.guest.carspotterstool.R;
import com.example.guest.carspotterstool.adapters.FirebaseContributionListAdapter;
import com.example.guest.carspotterstool.models.PhotoContribution;
import com.example.guest.carspotterstool.models.User;
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

public class IntentUserProfile extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private Context mContext;
    private User databaseUser;
    private User mDatabaseUser;
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
        String uid = getIntent().getStringExtra("uid");
        String firebaseKey = getIntent().getStringExtra("firebaseKey");
        ButterKnife.bind(this);
        mAuth = FirebaseAuth.getInstance();
        getUser(firebaseKey, uid);
    }
    private void setProfile(User mDatabaseUser){
        setTitle(mDatabaseUser.getDisplayName() + "'s Profile");
        userName.setText(mDatabaseUser.getDisplayName());
        mUserScore.setText(mDatabaseUser.getDisplayName() + "'s Score: " + mDatabaseUser.getScore());
        userContributionCount.setText(mDatabaseUser.getDisplayName() + "'s Contribution Count: " + mDatabaseUser.getContributionCount());
        userStanding.setText(mDatabaseUser.getDisplayName() + "'s Leaderboard Standing of #" + mDatabaseUser.getLeaderBoardStanding());
    }

    public void getUser (String firebaseKey, String uid){
        final String firebaseKeyCurrent;
        firebaseKeyCurrent = firebaseKey;
        DatabaseReference findUser = FirebaseDatabase.getInstance()
                .getReference(Constants.FIREBASE_CHILD_CONTRIBUTIONS)
                .child(Constants.FIREBASE_CHILD_USERS)
                .child(uid)
                .child(firebaseKeyCurrent);
        findUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                databaseUser = dataSnapshot.getValue(User.class);
                mDatabaseUser = databaseUser;
                setProfile(mDatabaseUser);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }
}

