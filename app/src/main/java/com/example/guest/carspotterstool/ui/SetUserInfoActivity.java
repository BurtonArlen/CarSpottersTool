package com.example.guest.carspotterstool.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.guest.carspotterstool.R;

import java.io.ByteArrayOutputStream;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_user_info);
        ButterKnife.bind(this);
        mPictureButton.setOnClickListener(this);
        mSubmitUser.setOnClickListener(this);
        mProfilePreview.setOnClickListener(this);
        mSubmitUser.setVisibility(View.GONE);
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

    public void onClick(View v){
        if (v == mPictureButton){
            onLaunchCamera();
            mPictureButton.setVisibility(View.GONE);
            mSubmitUser.setVisibility(View.VISIBLE);
        }
        if (v == mSubmitUser){

        }
        if (v == mProfilePreview){

        }
    }
}
