package com.example.guest.carspotterstool.ui;

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
    private String userId;
    private Context mContext;
    private ArrayList<User> mUserList = new ArrayList<>();
    final ArrayList<User> userData = new ArrayList<>();
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
        ButterKnife.bind(this);
        mAuth = FirebaseAuth.getInstance();
//        String displayName = getIntent().getStringExtra("displayName");
        getUser(uid);
        Uri profilePic = mAuth.getCurrentUser().getPhotoUrl();
        Picasso.with(mContext).load(profilePic)
                .resize(MAX_WIDTH, MAX_HEIGHT)
                .centerCrop()
                .into(profilePicture);
    }
    private void setProfile(ArrayList<User> userData){
        setTitle(userData.get(0).getName() + "'s Profile");
        userName.setText(userData.get(0).getName());
        mUserScore.setText(userData.get(0).getName() + "'s Score: " + String.valueOf(userData.get(0).getScore()));
        userContributionCount.setText(userData.get(0).getName() + "'s Contribution Count: " + String.valueOf(userData.get(0).getContributionCount()));
        userStanding.setText(userData.get(0).getName() + "'s Leaderboard Standing of #" + String.valueOf(userData.get(0).getLeaderBoardStanding()));
    }

    private void getUser(String uid){
        final String userId = uid;

        final ArrayList<User> userList = new ArrayList<>();
        DatabaseReference refUserPull = FirebaseDatabase.getInstance()
                .getReference(Constants.FIREBASE_CHILD_CONTRIBUTIONS)
                .child(Constants.FIREBASE_CHILD_USERS)
                .child(Constants.FIREBASE_CHILD_ALL_USERS).child(uid);
        refUserPull.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    userList.add(snapshot.getValue(User.class));
                }
                mUserList = userList;
                Log.d("ArrayContents", mUserList.get(0).toString());
                for (int u = 0; u < userList.size(); u++){
                    if (userList.get(u).getUid().equals(userId)){
                        userData.add(userList.get(u));
                    }
                }
                setProfile(userData);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                databaseError.getMessage();
            }
        });
    }
}
//            mUserScore.setText(displayName + "'s Score: " + String.valueOf(score));
//            userContributionCount.setText(displayName + "'s Contribution Count: " + String.valueOf(contributionCount));
//            userStanding.setText(displayName + "'s Leaderboard Standing of #" + String.valueOf(standing));
//            FirebaseContributionListAdapter adapter = new FirebaseContributionListAdapter(getApplicationContext(), mUserContributions);
//            recyclerView.setAdapter(adapter);
//            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ProfileActivity.this);
//            recyclerView.setLayoutManager(layoutManager);
//            recyclerView.setHasFixedSize(true);
//            }
