package com.example.guest.carspotterstool.ui;

import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.guest.carspotterstool.Constants;
import com.example.guest.carspotterstool.R;
import com.example.guest.carspotterstool.adapters.FirebaseContributionListAdapter;
import com.example.guest.carspotterstool.models.PhotoContribution;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ContributeActivity extends AppCompatActivity implements View.OnClickListener {
    private String mDatabaseChild;
    private ArrayList<PhotoContribution> mContributions;
    @Bind(R.id.recyclerView) RecyclerView recyclerView;
    @Bind(R.id.buttonMakeQuery) Button searchTypeButtonMake;
    @Bind(R.id.buttonModelQuery) Button searchTypeButtonModel;
    @Bind(R.id.buttonYearQuery) Button searchTypeButtonYear;
    @Bind(R.id.searchTypeSelect) ConstraintLayout searchTypeSelect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contribute);
        ButterKnife.bind(this);
        recyclerView.setVisibility(View.GONE);
        setClickListeners();

    }

    public void setClickListeners(){
        searchTypeButtonYear.setOnClickListener(this);
        searchTypeButtonMake.setOnClickListener(this);
        searchTypeButtonModel.setOnClickListener(this);
    }

    public void changeVisibilityVisible(){
        searchTypeSelect.setVisibility(View.GONE);
    }

    public String setDatabaseChild(String databaseChild){
        return mDatabaseChild = databaseChild;
    }

    @Override
    public void onClick(View v) {
        if (v == searchTypeButtonMake) {
            setDatabaseChild("make");
            String query = "unknown";
            getContributionsBySearch(query);
            changeVisibilityVisible();
            recyclerView.setVisibility(View.VISIBLE);
        }
        if (v == searchTypeButtonModel) {
            setDatabaseChild("model");
            String query = "unknown";
            getContributionsBySearch(query);
            changeVisibilityVisible();
            recyclerView.setVisibility(View.VISIBLE);
        }
        if (v == searchTypeButtonYear) {
            setDatabaseChild("year");
            String query = "unknown";
            getContributionsBySearch(query);
            changeVisibilityVisible();
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    private void getContributionsBySearch(String query) {
        final ArrayList<PhotoContribution> userContributions = new ArrayList<>();
        Log.d("databaseChild", mDatabaseChild);
        Log.d("databaseQuery", query);
        DatabaseReference userRef = FirebaseDatabase.getInstance()
                .getReference(Constants.FIREBASE_CHILD_CONTRIBUTIONS)
                .child(Constants.FIREBASE_CHILD_CAR_CONTRIBUTIONS)
                .child(mDatabaseChild)
                .child(query);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    userContributions.add(snapshot.getValue(PhotoContribution.class));
                }
                mContributions = userContributions;
                FirebaseContributionListAdapter adapter = new FirebaseContributionListAdapter(getApplicationContext(), mContributions);
                recyclerView.setAdapter(adapter);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ContributeActivity.this);
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
