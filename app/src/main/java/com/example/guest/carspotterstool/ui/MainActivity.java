package com.example.guest.carspotterstool.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.guest.carspotterstool.R;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

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
