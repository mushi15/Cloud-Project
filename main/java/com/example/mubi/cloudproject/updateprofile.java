package com.example.mubi.cloudproject;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.firebase.client.Firebase;
import com.google.firebase.storage.FirebaseStorage;

import de.hdodenhof.circleimageview.CircleImageView;

public class updateprofile extends AppCompatActivity {

    CircleImageView CImage;
    EditText UPUsername;
    EditText UPPassword;
    Firebase Root_URL;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.updateprofile);
        Initialize();
    }
    public void Initialize()
    {
        UPUsername = (EditText)findViewById(R.id.UpdatePic_Username);
        UPPassword= (EditText)findViewById(R.id.UpdatePic_Username);
        CImage = (CircleImageView)findViewById(R.id.UpdatePic_ProfilePicture);
        Root_URL = new Firebase(FConstants.rootlink);

    }
}