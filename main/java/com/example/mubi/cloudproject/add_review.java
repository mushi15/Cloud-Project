package com.example.mubi.cloudproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.firebase.client.Firebase;

public class add_review extends AppCompatActivity {
    EditText review_et;
    RatingBar ratingbar;
    Button addreview;

    String roomNumber;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_review);
        loaddata();
        SetStuff();
    }
    public void loaddata(){
        roomNumber = getIntent().getExtras().getString("roomnumber");
        username = getIntent().getExtras().getString("username");
    }
    public void SetStuff(){
        review_et = (EditText) findViewById(R.id.addreview_review);
        ratingbar = (RatingBar) findViewById(R.id.addreview_rating);
        addreview = (Button) findViewById(R.id.addreview_button);

        addreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(review_et.getText().toString().equals(""))
                    Toast.makeText(getApplicationContext(),"Incomplete review", Toast.LENGTH_SHORT).show();
                else {
                    Firebase rootUrl = new Firebase(FConstants.rootlink);

                    rootUrl.child(FConstants.Review_Table).child(roomNumber).child(username).child(FConstants.Review_Table_rating).setValue((int)(ratingbar.getRating()));
                    rootUrl.child(FConstants.Review_Table).child(roomNumber).child(username).child(FConstants.Review_Table_review).setValue(review_et.getText().toString());
                    finish();
                }
            }
        });
    }
}