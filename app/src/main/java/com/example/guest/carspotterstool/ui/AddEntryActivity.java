package com.example.guest.carspotterstool.ui;

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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.ByteArrayOutputStream;

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
        PhotoContribution photoContribution = new PhotoContribution(imageUrl);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();
        DatabaseReference ref = FirebaseDatabase.getInstance()
                .getReference(Constants.FIREBASE_CHILD_CONTRIBUTIONS)
                .child("imageContributions");
        DatabaseReference refUser = FirebaseDatabase.getInstance()
                .getReference(Constants.FIREBASE_CHILD_CONTRIBUTIONS)
                .child(uid);
        DatabaseReference pushRef = ref.push();
        DatabaseReference userPushRef = refUser.push();
        String pushId = pushRef.getKey();
        photoContribution.setImageEncoded(imageUrl);
        photoContribution.setPushId(pushId);
        pushRef.setValue(photoContribution);
        userPushRef.setValue(photoContribution);
    }

    public void encodeBitmap(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        String imageUrl = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
        saveToFirebase(imageUrl);
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
            onLaunchCamera();
        }
    }
}
