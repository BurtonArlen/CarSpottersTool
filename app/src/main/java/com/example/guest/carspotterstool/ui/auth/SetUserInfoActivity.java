package com.example.guest.carspotterstool.ui.auth;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.guest.carspotterstool.Constants;
import com.example.guest.carspotterstool.R;
import com.example.guest.carspotterstool.models.PhotoContribution;
import com.example.guest.carspotterstool.models.User;
import com.example.guest.carspotterstool.ui.appflow.MainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SetUserInfoActivity extends AppCompatActivity implements View.OnClickListener{
    @Bind(R.id.userNameCreate)EditText mUserNameCreate;
    @Bind(R.id.profilePictureUpload)Button mPictureButton;
    @Bind(R.id.profileSubmit)Button mSubmitUser;
    @Bind(R.id.pageHeader)TextView mHeader;
    @Bind(R.id.profilePreview)ImageView mProfilePreview;
    private static final int REQUEST_IMAGE_CAPTURE = 111;
    private String imageUrlPass;
    private FirebaseAuth mAuth;
    private String mUid;
    private String mDisplayName;
    private String mUserName;
    private User databaseUser;
    private ProgressDialog mDialog;
    private String databaseUid;
    private ArrayList<User> mEmptyCheck = new ArrayList<>();
    private ArrayList<String> mDatabaseUids = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_user_info);
        ButterKnife.bind(this);
        createProgressDialog();
        mDialog.show();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getInstance().getCurrentUser();
        String uid = user.getUid();
        mUid = uid;
        checkUser(mUid);
        String userName = user.getDisplayName();
        mUserName = userName;
        mHeader.setText(userName);
        mPictureButton.setOnClickListener(this);
        mSubmitUser.setOnClickListener(this);
        mProfilePreview.setOnClickListener(this);
        mSubmitUser.setVisibility(View.GONE);
    }
    private void createProgressDialog() {
        mDialog = new ProgressDialog(this);
        mDialog.setTitle("Managing Drivers");
        mDialog.setMessage("Checking Status..");
        mDialog.setCancelable(false);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            mProfilePreview.setImageBitmap(imageBitmap);
            encodeBitmap(imageBitmap);
        }
    }

    public void encodeBitmap(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        String imageUrl = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
        imageUrlPass = imageUrl;
    }

    public void onLaunchCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    public void checkUser(String uid){
        final String qUid = uid;
        final ArrayList<String> databaseUids = new ArrayList<>();
        final ArrayList<User> emptyCheck = new ArrayList<>();
        DatabaseReference checkUser = FirebaseDatabase.getInstance()
                .getReference(Constants.FIREBASE_CHILD_CONTRIBUTIONS)
                .child(Constants.FIREBASE_CHILD_USERS).child(uid);
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    emptyCheck.add(snapshot.getValue(User.class));
                }
                mEmptyCheck = emptyCheck;
                for (int j = 0; j < emptyCheck.size(); j++) {
                    databaseUid = emptyCheck.get(j).getUid();
                    databaseUids.add(databaseUid);
                }
                mDatabaseUids = databaseUids;
                checkArrays(qUid, databaseUids);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

    }
    public void checkArrays(String uid, ArrayList<String> dbUid){
        final ArrayList<String> qDbUid = dbUid;
        if (mEmptyCheck.size()>0){
            if (mDatabaseUids.size()>0){
                if (qDbUid.contains(uid)){
                    mDialog.dismiss();
                    toMainActivity();
                } else {
                    jurrasicParkReference();
                    mDialog.dismiss();
                }
            } else {
                jurrasicParkReference();
                mDialog.dismiss();
            }
        } else {
            jurrasicParkReference();
            mDialog.dismiss();
        }
    }

    public void checkUserObject(String databaseFirebaseKey, String firebaseKeyCurrent){
        if (databaseFirebaseKey != null){
            if (databaseFirebaseKey.equals(firebaseKeyCurrent)){
                mDialog.dismiss();
                toMainActivity();
            } else {
                jurrasicParkReference();
            }
        }
    }

    public void confirmUser(String firebaseKey, String uid){
        final String firebaseKeyCurrent;
        final String databaseFirebaseKey;

        firebaseKeyCurrent = firebaseKey;
        DatabaseReference checkUser = FirebaseDatabase.getInstance()
                .getReference(Constants.FIREBASE_CHILD_CONTRIBUTIONS)
                .child(Constants.FIREBASE_CHILD_USERS)
                .child(uid)
                .child(firebaseKeyCurrent);
        checkUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                databaseUser = dataSnapshot.getValue(User.class);
                String databaseFirebaseKey = databaseUser.getFirebaseKey();
                checkUserObject(databaseFirebaseKey, firebaseKeyCurrent);
                Log.d("database UserKey", databaseUser.getFirebaseKey());
                Log.d("UserKey", firebaseKeyCurrent);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });


    }

    public void toMainActivity(){
        Intent intent = new Intent(SetUserInfoActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    public void jurrasicParkReference(){
        for (int q = 0; q <= 10; q++){
            System.out.println("Uh uh uh, you forgot the magic word");
            System.out.println(String.valueOf(q));
        }
    }

    public void saveToFirebase(String displayName, String userName, String imageUrl, String uid){
        if (mUserNameCreate.getText().toString().trim().length() == 0) {
            displayName = userName;
        }
        User newUser = new User(displayName, userName, imageUrl, uid);
        DatabaseReference refUser = FirebaseDatabase.getInstance()
                .getReference(Constants.FIREBASE_CHILD_CONTRIBUTIONS)
                .child(Constants.FIREBASE_CHILD_USERS).child(uid);
        DatabaseReference userPushRef = refUser.push();
        String firebaseKey = userPushRef.getKey();
        newUser.setFirebaseKey(firebaseKey);
        newUser.setName(userName);
        newUser.setDisplayName(displayName);
        newUser.setImageUser(imageUrl);
        newUser.setPushId(uid);
        userPushRef.setValue(newUser);
        confirmUser(firebaseKey, uid);
    }

    public void onClick(View v){
        if (v == mPictureButton){
            onLaunchCamera();
            mPictureButton.setVisibility(View.GONE);
            mSubmitUser.setVisibility(View.VISIBLE);
        }
        if (v == mSubmitUser){
            mDialog.show();
            mDisplayName = mUserNameCreate.getText().toString().trim();
            saveToFirebase(mDisplayName, mUserName, imageUrlPass, mUid);
        }
        if (v == mProfilePreview){
            onLaunchCamera();
        }
    }
}
