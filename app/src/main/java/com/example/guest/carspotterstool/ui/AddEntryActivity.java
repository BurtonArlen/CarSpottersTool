package com.example.guest.carspotterstool.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;

import butterknife.Bind;
import butterknife.ButterKnife;

public class AddEntryActivity extends AppCompatActivity implements View.OnClickListener{
    @Bind(R.id.submitNewContribution) Button submitNewContributionButton;
    @Bind(R.id.subjectImage) ImageView subjectImage;
    @Bind(R.id.yearEntry) EditText yearEntry;
    @Bind(R.id.makeEntry) EditText makeEntry;
    @Bind(R.id.modelEntry) EditText modelEntry;
    private static final int REQUEST_IMAGE_CAPTURE = 111;
    private PhotoContribution photoContribution;
    private String imageUrlPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_entry);
        ButterKnife.bind(this);
        submitNewContributionButton.setOnClickListener(this);
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

    public void saveToFirebase(String imageUrl){
        Log.d("year", yearEntry.getText().toString());
        Log.d("make", makeEntry.getText().toString());
        Log.d("model", modelEntry.getText().toString());
        PhotoContribution photoContribution = new PhotoContribution(imageUrl, null, null, null, null, null);
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
                .child(modelEntry.getText().toString());
        DatabaseReference refMake = FirebaseDatabase.getInstance()
                .getReference(Constants.FIREBASE_CHILD_CONTRIBUTIONS)
                .child(Constants.FIREBASE_CHILD_CAR_CONTRIBUTIONS)
                .child(Constants.FIREBASE_CHILD_MAKE)
                .child(makeEntry.getText().toString());
        DatabaseReference refYear = FirebaseDatabase.getInstance()
                .getReference(Constants.FIREBASE_CHILD_CONTRIBUTIONS)
                .child(Constants.FIREBASE_CHILD_CAR_CONTRIBUTIONS)
                .child(Constants.FIREBASE_CHILD_YEAR)
                .child(yearEntry.getText().toString());
        DatabaseReference pushRef = ref.push();
        DatabaseReference userPushRef = refUser.push();
        DatabaseReference makePushRef = refMake.push();
        DatabaseReference modelPushRef = refModel.push();
        DatabaseReference yearPushRef = refYear.push();
        String pushId = pushRef.getKey();
        photoContribution.setImageEncoded(imageUrl);
        if (makeEntry.getText().toString() != ""){
            photoContribution.setMake(makeEntry.getText().toString());
        }
        if (modelEntry.getText().toString() != ""){
            photoContribution.setModel(modelEntry.getText().toString());
        }
        if (yearEntry.getText().toString() != ""){
            photoContribution.setYear(yearEntry.getText().toString());
        }
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
