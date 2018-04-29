package com.example.mubi.cloudproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.firebase.client.Firebase;

public class MainActivity extends AppCompatActivity {

    Firebase rootURL;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rootURL = new Firebase(FConstants.rootlink);


        rootURL.child("Mubashra").setValue("hello");
        rootURL.child("Omar").child("nice").setValue("yo");
        rootURL.child("Omar").child("nice1").setValue("sdf");
        rootURL.child("Omar").child("2").setValue("2114ed");
    }
}
