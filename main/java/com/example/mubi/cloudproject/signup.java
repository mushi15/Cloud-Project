package com.example.mubi.cloudproject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

public class signup extends AppCompatActivity {
    EditText SignUpUsername;
    EditText SignUpPassword;
    Button SignUpButton;
    ImageView SignUpImage;
    public static final int GalleryRequestCodeID = 1;
    Bitmap profilePictureBitmap;
    Uri uploadProfilePictureURI, downloadProfilePictureURI;
    Firebase Root_URL;
    StorageReference mStorage;

    ProgressDialog pd ;

    final static int PERMISSION_ALL = 1;

    public Boolean CheckPermissions(){
        int writeStorage = getApplicationContext().checkCallingOrSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return writeStorage == PackageManager.PERMISSION_GRANTED;
    }
    public void GetPermissions(){
        String[] PERMISSIONS = {android.Manifest.permission.WRITE_EXTERNAL_STORAGE};
        ActivityCompat.requestPermissions(signup.this, PERMISSIONS, PERMISSION_ALL);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);
        Initialize();
        Listeners();
    }
    public void Initialize()
    {
        pd = new ProgressDialog(signup.this);
        pd.setMessage("Registering user");
        SignUpUsername = (EditText)findViewById(R.id.SignUp_Username);
        SignUpPassword= (EditText)findViewById(R.id.SignUp_Password);
        SignUpButton= (Button)findViewById(R.id.SignUp_SignUpButton);
        SignUpImage = (ImageView)findViewById(R.id.SignUp_Image);
        Root_URL = new Firebase(FConstants.rootlink);
        mStorage = FirebaseStorage.getInstance().getReference();
    }
    public void GetImageFromGallery(){
        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto, GalleryRequestCodeID);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GalleryRequestCodeID && resultCode == Activity.RESULT_OK){
            Uri uri = data.getData();
            try {
                uploadProfilePictureURI = data.getData();
                profilePictureBitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(),uri);
                SignUpImage.setImageBitmap(profilePictureBitmap);

                SignUpImage.setPadding(0,0,0,0);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void Listeners()
    {
        SignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignupUser();
            }
        });
        SignUpImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(CheckPermissions())
                    GetImageFromGallery();
                else
                    GetPermissions();
            }
        });
    }


    public void AddUser(User newUser){
        Root_URL.child(FConstants.User_Table).child(newUser.getUsername()).setValue(newUser);
        finish();
    }

    public void SignupUser(){
        final String userName  = SignUpUsername.getText().toString();
        final String password = SignUpPassword.getText().toString();
        if(userName.equals("") || password.equals("")){
            Toast.makeText(getApplicationContext(),"Incomplete credientials",Toast.LENGTH_SHORT).show();
            return;
        }
        Firebase userExists = Root_URL.child(FConstants.User_Table).child(userName);
        userExists.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    Toast.makeText(getApplicationContext(), "Username not available", Toast.LENGTH_SHORT).show();
                }
                else
                    UploadImageOnFirebase(new User(userName,password,"",0));

            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    public void UploadImageOnFirebase(final User u){
        pd.show();
        if(uploadProfilePictureURI == null) {
            AddUser(u);
        }
        else {
            StorageReference filePath = mStorage.child("profilePicturePath").child(uploadProfilePictureURI.getLastPathSegment());
            filePath.putFile(uploadProfilePictureURI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    downloadProfilePictureURI = taskSnapshot.getDownloadUrl();
                    u.setProfileImage(downloadProfilePictureURI.toString());
                    AddUser(u);
                }
            });


        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_ALL: {
                if(CheckPermissions()){
                    GetImageFromGallery();
                }
            }
        }
    }
}