package com.example.guest.carspotterstool.ui.appflow;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.guest.carspotterstool.Constants;
import com.example.guest.carspotterstool.R;
import com.example.guest.carspotterstool.models.User;
import com.example.guest.carspotterstool.ui.contribution.SearchActivity;
import com.example.guest.carspotterstool.ui.contribution.AddEntryActivity;
import com.example.guest.carspotterstool.ui.contribution.ContributeActivity;
import com.example.guest.carspotterstool.ui.user.ProfileActivity;
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
