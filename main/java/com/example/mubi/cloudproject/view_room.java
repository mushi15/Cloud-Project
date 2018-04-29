package com.example.mubi.cloudproject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class view_room extends AppCompatActivity {

    Room current_room;
    ArrayList<Review> review_list;
    RecyclerView recyclerView;
    review_adapter adapterReviewList;
    Firebase root_url;
    LinearLayout roompicturell;
    TextView roomnumbertv, roomPricetv;
    ImageView f1, f2;
    float total_rating;
    RatingBar mainRating;
    Button bookButton;

    User current_user;

    public static int DpToPx(float dp, Context context) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }
    public void LoadUser(){
        SharedPreferences prefs = getSharedPreferences("User_Pref", MODE_PRIVATE);
        String tu = prefs.getString("username", null);
        String tp = prefs.getString("password",null);
        String pic = prefs.getString("picture",null);
        int t = prefs.getInt("type",0);
        current_user = new User(tu,tp,pic,t);
        current_user = new User("J","y","https://firebasestorage.googleapis.com/v0/b/cloudproject-e64fd.appspot.com/o/profilePicturePath%2F1011782022?alt=media&token=1a076626-df1d-4f5d-8cb2-7c83f803ffe5",0);

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_room);
        LoadUser();
        LoadDataFromIntent();
        InitializeVariables();
        SetListner();
        new LoadReviewsFromFirebase().execute();
        DisplayData();
    }
    public void LoadDataFromIntent(){
        current_room = (Room) getIntent().getSerializableExtra("room");

    }
    public void InitializeVariables() {
        root_url = new Firebase(FConstants.rootlink);
        review_list = new ArrayList<>();
        total_rating = 0;
        mainRating = (RatingBar) findViewById(R.id.viewroom_rating);
        roomnumbertv = (TextView) findViewById(R.id.viewroom_roomnumber);
        roomPricetv = (TextView) findViewById(R.id.viewroom_roomprice);
        roompicturell = (LinearLayout) findViewById(R.id.viewroom_picturesll);
        f1 = (ImageView) findViewById(R.id.viewroom_feature1);
        f2 = (ImageView) findViewById(R.id.viewroom_feature2);
        bookButton = (Button) findViewById(R.id.viewroom_bookbutton);

        if(current_room.getAvailable() == 0){
            bookButton.setText("Check Out");
        }
        else
            bookButton.setText("Book Room");
    }
    public void SetListner(){
        bookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(current_room.getAvailable() == 1){
                    root_url.child(FConstants.Booked_Table).child(current_user.getUsername()).child(current_room.getRoomnumber()).setValue("1");
                    root_url.child(FConstants.Room_Table).child(current_room.getRoomnumber()).child(FConstants.Room_Table_available).setValue(0);
                    bookButton.setText("Check Out");
                    current_room.setAvailable(0);
                }
                else if(current_room.getAvailable() == 0){
                    new AlertDialog.Builder(view_room.this)
                            .setTitle("Check Out")
                            .setMessage("Are you sure you want to check out?")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    root_url.child(FConstants.Booked_Table).child(current_user.getUsername()).child(current_room.getRoomnumber()).removeValue();
                                    root_url.child(FConstants.Room_Table).child(current_room.getRoomnumber()).child(FConstants.Room_Table_available).setValue(1);
                                    Intent intent = new Intent(view_room.this,add_review.class);
                                    intent.putExtra("username",current_user.getUsername());
                                    intent.putExtra("roomnumber",current_room.getRoomnumber());
                                    startActivity(intent);
                                    finish();
                                }})
                            .setNegativeButton(android.R.string.no, null).show();
                }
            }
        });
    }
    public void DataLoaded(){
        recyclerView = (RecyclerView) findViewById(R.id.viewroom_recyclerview);
        adapterReviewList = new review_adapter(view_room.this,review_list);
        recyclerView.setAdapter(adapterReviewList);
        final GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 1);
        recyclerView.setLayoutManager(gridLayoutManager);
        mainRating.setRating(total_rating/review_list.size());
    }
    public void LoadSingleImage(final String imageURL){
        int tempPadding = 10;
        ImageView tempImageView = new ImageView(view_room.this);
        tempImageView.setLayoutParams(new RelativeLayout.LayoutParams(DpToPx(100,view_room.this),
                DpToPx(100,view_room.this)));
        tempImageView.setPadding(tempPadding,tempPadding,tempPadding,tempPadding);
        Picasso.with(view_room.this).load(imageURL).resize(DpToPx(100, view_room.this),
                DpToPx(100, view_room.this)) // resizes the image to these dimensions (in pixel)
                .centerCrop().into(tempImageView);
        tempImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(view_room.this,imageURL, Toast.LENGTH_SHORT).show();
            }
        });
        roompicturell.addView(tempImageView);

    }
    public void DisplayData(){
        roomnumbertv.setText("Room :" + current_room.getRoomnumber());
        roomPricetv.setText("Price :" + current_room.getRoomprice() + "$");
        if(current_room.getF1() == 1)
            f1.setVisibility(View.VISIBLE);
        if(current_room.getF2() == 2)
            f2.setVisibility(View.VISIBLE);
        ArrayList<String> pictureURLList = current_room.getPictures();
        for (int i = 0 ; i < pictureURLList.size(); i++){
            LoadSingleImage(pictureURLList.get(i));
        }
    }
    private class LoadReviewsFromFirebase extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            root_url.child(FConstants.Review_Table).child(String.valueOf(current_room.getRoomnumber())).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot != null) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            if (ds != null) {
                                String roomnumber = dataSnapshot.getKey();
                                String username = ds.getKey();
                                int rating = Integer.valueOf(ds.child(FConstants.Review_Table_rating).getValue().toString());
                                String review = ds.child(FConstants.Review_Table_review).getValue().toString();
                                review_list.add(new Review(username,roomnumber,review,rating));
                                total_rating += rating;
                            }
                        }
                    }
                    DataLoaded();
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });
            return null;
        }
    }
}