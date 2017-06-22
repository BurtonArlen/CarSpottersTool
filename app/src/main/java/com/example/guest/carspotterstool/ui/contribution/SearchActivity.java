package com.example.guest.carspotterstool.ui.contribution;

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

public class SearchActivity extends AppCompatActivity implements View.OnClickListener {
    private String mDatabaseChild;
    private ArrayList<PhotoContribution> mContributions;
    @Bind(R.id.recyclerView) RecyclerView recyclerView;
    @Bind(R.id.buttonMakeQuery) Button searchTypeButtonMake;
    @Bind(R.id.buttonModelQuery) Button searchTypeButtonModel;
    @Bind(R.id.buttonYearQuery) Button searchTypeButtonYear;
    @Bind(R.id.searchTypeSelect) ConstraintLayout searchTypeSelect;
    @Bind(R.id.searchTermTitle) TextView searchTermTitle;
    @Bind(R.id.searchEditText) EditText searchEditText;
    @Bind(R.id.searchSubmitButton) Button searchSubmitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        recyclerView.setVisibility(View.GONE);
        setClickListeners();
        changeVisibilityGone();
    }

    public void setClickListeners(){
        searchTypeButtonYear.setOnClickListener(this);
        searchTypeButtonMake.setOnClickListener(this);
        searchTypeButtonModel.setOnClickListener(this);
        searchSubmitButton.setOnClickListener(this);
    }

    public void changeVisibilityGone(){
        searchTermTitle.setVisibility(View.GONE);
        searchSubmitButton.setVisibility(View.GONE);
        searchEditText.setVisibility(View.GONE);
    }

    public void changeVisibilityVisible(String searchTerm){
        searchTermTitle.setText("Please enter a " + searchTerm);
        searchTermTitle.setVisibility(View.VISIBLE);
        searchSubmitButton.setVisibility(View.VISIBLE);
        searchEditText.setVisibility(View.VISIBLE);
        searchTypeSelect.setVisibility(View.GONE);
    }

    public String setDatabaseChild(String databaseChild){
        return mDatabaseChild = databaseChild;
    }

    @Override
    public void onClick(View v) {
        if (v == searchTypeButtonMake) {
            String searchTerm = "Vehicle Make";
            setDatabaseChild("make");
            changeVisibilityVisible(searchTerm);
        }
        if (v == searchTypeButtonModel) {
            String searchTerm = "Vehicle Manufacturer";
            setDatabaseChild("model");
            changeVisibilityVisible(searchTerm);
        }
        if (v == searchTypeButtonYear) {
            String searchTerm = "Manufacturing Year";
            setDatabaseChild("year");
            changeVisibilityVisible(searchTerm);
        }
        if (v == searchSubmitButton) {
            String query = searchEditText.getText().toString().trim();
            if (query != "") {
                changeVisibilityGone();
                getContributionsBySearch(query);
                recyclerView.setVisibility(View.VISIBLE);
            } else {
                searchEditText.setError("Please Enter Search Terms");
            }
        }
    }

    private void getContributionsBySearch(String query) {
        final ArrayList<PhotoContribution> userContributions = new ArrayList<>();
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

                Log.d("ArrayContents", mContributions.get(0).toString());

                FirebaseContributionListAdapter adapter = new FirebaseContributionListAdapter(getApplicationContext(), mContributions);
                recyclerView.setAdapter(adapter);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(SearchActivity.this);
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
