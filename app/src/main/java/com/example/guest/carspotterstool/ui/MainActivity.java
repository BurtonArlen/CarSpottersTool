package com.example.guest.carspotterstool.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.guest.carspotterstool.Constants;
import com.example.guest.carspotterstool.R;
import com.example.guest.carspotterstool.adapters.FirebaseContributionListAdapter;
import com.example.guest.carspotterstool.models.PhotoContribution;
import com.example.guest.carspotterstool.models.User;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private ArrayList<User> mUserList = new ArrayList<>();
    final ArrayList<User> userData = new ArrayList<>();
    private FirebaseAuth mAuth;
    private GoogleApiClient mGClient;
    private Context mContext;
    @Bind(R.id.buttonContributeData) Button mDataContribute;
    @Bind(R.id.buttonSearch) Button mButtonSearch;
    @Bind(R.id.buttonUserProfile) Button mProfileButton;
    @Bind(R.id.buttonContributePhoto) Button mPhotoContribute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mDataContribute.setOnClickListener(this);
        mPhotoContribute.setOnClickListener(this);
        mProfileButton.setOnClickListener(this);
        mButtonSearch.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
        String userName = mAuth.getCurrentUser().getDisplayName();
        setTitle(userName + "'s CarSpottersTool");
        userToFirebase();
    }

    public void userToFirebase(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String displayName = user.getDisplayName();
        String uid = user.getUid();
        User saveUser = new User(null, null, uid);
        DatabaseReference refUser = FirebaseDatabase.getInstance()
                .getReference(Constants.FIREBASE_CHILD_CONTRIBUTIONS)
                .child(Constants.FIREBASE_CHILD_USERS)
                .child(Constants.FIREBASE_CHILD_ALL_USERS).child(uid);
        DatabaseReference userPushRef = refUser.push();
        getUserStats(uid);
        saveUser.setScore(userData.get(0).getScore());
        saveUser.setContributionCount(userData.get(0).getContributionCount());
        saveUser.setName(displayName);
        saveUser.setPushId(uid);
        userPushRef.setValue(saveUser);
    }

    private void getUserStats(String uid) {
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
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                databaseError.getMessage();
            }
        });
    }

    @Override
    public void onClick(View v){
        if (v == mDataContribute){
            Intent intent = new Intent(MainActivity.this, ContributeActivity.class);
            startActivity(intent);
        }
        if (v == mPhotoContribute){
            Intent intent = new Intent(MainActivity.this, AddEntryActivity.class);
            startActivity(intent);
        }
        if (v == mProfileButton){
            Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
            startActivity(intent);
        }
        if (v == mButtonSearch){
            Intent intent = new Intent(MainActivity.this, SearchActivity.class);
            startActivity(intent);
        }
    }
}
