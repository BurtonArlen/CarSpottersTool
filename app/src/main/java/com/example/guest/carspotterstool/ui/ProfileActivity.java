package com.example.guest.carspotterstool.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.guest.carspotterstool.Constants;
import com.example.guest.carspotterstool.R;
import com.example.guest.carspotterstool.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ProfileActivity extends AppCompatActivity {
    private User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_CHILD_USERS).child(uid);
        DatabaseReference pushRef = userRef.push();
        String pushId = pushRef.getKey();
        mUser.setPushId(pushId);
        pushRef.setValue(mUser);
    }
}
