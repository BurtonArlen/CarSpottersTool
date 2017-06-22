package com.example.guest.carspotterstool.ui.contribution;

import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

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

public class AddEntryActivity extends AppCompatActivity implements View.OnClickListener{
    @Bind(R.id.submitNewContribution) Button submitNewContributionButton;
    @Bind(R.id.subjectImage) ImageView subjectImage;
    @Bind(R.id.yearEntry) EditText yearEntry;
    @Bind(R.id.makeEntry) EditText makeEntry;
    @Bind(R.id.modelEntry) EditText modelEntry;
    private static final int REQUEST_IMAGE_CAPTURE = 111;
    private ArrayList<User> mUserArray = new ArrayList<>();
    private ArrayList<String> mUserFirebaseKeys = new ArrayList<>();
    private PhotoContribution photoContribution;
    private FirebaseAuth mAuth;
    private String firebaseKey;
    private String imageUrlPass;
    private String mModel;
    private String mMake;
    private String mYear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_entry);
        ButterKnife.bind(this);
        submitNewContributionButton.setOnClickListener(this);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getInstance().getCurrentUser();
        String uid = user.getUid();
        getFirebaseCurrentUser(uid);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.camera_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_photo:
                onLaunchCamera();
            default:
                break;
        }
        return false;
    }

    public void onLaunchCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    public void getFirebaseCurrentUser(String uid){
        final ArrayList<User> userArray = new ArrayList<>();
        final ArrayList<String> userFirebaseKeys = new ArrayList<>();
        DatabaseReference checkUser = FirebaseDatabase.getInstance()
                .getReference(Constants.FIREBASE_CHILD_CONTRIBUTIONS)
                .child(Constants.FIREBASE_CHILD_USERS).child(uid);
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
            for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                userArray.add(snapshot.getValue(User.class));
            }
            mUserArray = userArray;
            for (int i = 0; i < mUserArray.size(); i++){
                userFirebaseKeys.add(mUserArray.get(i).getFirebaseKey());
            }
            mUserFirebaseKeys = userFirebaseKeys;
            returnFirebaseKey(mUserFirebaseKeys);
        }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }

    public void returnFirebaseKey(ArrayList<String> keyList){
        firebaseKey = keyList.get(0);
    }

    public void saveToFirebase(String imageUrl){

        if (makeEntry.getText().toString().trim().length() == 0){
            mMake = "unknown";
        } else {
            mMake = makeEntry.getText().toString().trim();
        }
        if (modelEntry.getText().toString().trim().length() == 0){
            mModel = "unknown";
        } else {
            mModel = modelEntry.getText().toString().trim();
        }
        if (yearEntry.getText().toString().trim().length() == 0) {
            mYear = "unknown";
        } else {
            mYear = yearEntry.getText().toString().trim();
        }
        PhotoContribution photoContribution = new PhotoContribution(imageUrl, null, null, null, null, null, null);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String displayName = user.getDisplayName();
        String uid = user.getUid();
        DatabaseReference ref = FirebaseDatabase.getInstance()
                .getReference(Constants.FIREBASE_CHILD_CONTRIBUTIONS)
                .child(Constants.FIREBASE_CHILD_ALL_CONTRIBUTIONS);
        DatabaseReference refUser = FirebaseDatabase.getInstance()
                .getReference(Constants.FIREBASE_CHILD_CONTRIBUTIONS)
                .child(Constants.FIREBASE_CHILD_USERS)
                .child(uid).child(Constants.FIREBASE_CHILD_CAR_CONTRIBUTIONS);
        DatabaseReference refModel = FirebaseDatabase.getInstance()
                .getReference(Constants.FIREBASE_CHILD_CONTRIBUTIONS)
                .child(Constants.FIREBASE_CHILD_CAR_CONTRIBUTIONS)
                .child(Constants.FIREBASE_CHILD_MODEL)
                .child(mModel);
        DatabaseReference refMake = FirebaseDatabase.getInstance()
                .getReference(Constants.FIREBASE_CHILD_CONTRIBUTIONS)
                .child(Constants.FIREBASE_CHILD_CAR_CONTRIBUTIONS)
                .child(Constants.FIREBASE_CHILD_MAKE)
                .child(mMake);
        DatabaseReference refYear = FirebaseDatabase.getInstance()
                .getReference(Constants.FIREBASE_CHILD_CONTRIBUTIONS)
                .child(Constants.FIREBASE_CHILD_CAR_CONTRIBUTIONS)
                .child(Constants.FIREBASE_CHILD_YEAR)
                .child(mYear);
        DatabaseReference pushRef = ref.push();
        DatabaseReference userPushRef = refUser.push();
        DatabaseReference makePushRef = refMake.push();
        DatabaseReference modelPushRef = refModel.push();
        DatabaseReference yearPushRef = refYear.push();
        String pushId = pushRef.getKey();
        photoContribution.setImageEncoded(imageUrl);
        photoContribution.setSubmitterFirebaseKey(firebaseKey);
        photoContribution.setModel(mModel);
        photoContribution.setMake(mMake);
        photoContribution.setYear(mYear);
        photoContribution.setPushId(pushId);
        photoContribution.setSubmitterId(uid);
        photoContribution.setSubmitterName(displayName);
        makePushRef.setValue(photoContribution);
        modelPushRef.setValue(photoContribution);
        yearPushRef.setValue(photoContribution);
        pushRef.setValue(photoContribution);
        userPushRef.setValue(photoContribution);
    }

    public void encodeBitmap(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        String imageUrl = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
        imageUrlPass = imageUrl;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            subjectImage.setImageBitmap(imageBitmap);
            encodeBitmap(imageBitmap);
        }
    }

    @Override
    public void onClick(View v){
        if (v == submitNewContributionButton){
            saveToFirebase(imageUrlPass);
            Intent intent = new Intent(AddEntryActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }
}
