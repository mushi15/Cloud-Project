package com.example.mubi.cloudproject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.TransactionTooLargeException;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;

public class add_room extends AppCompatActivity {

    Button addimagebutton, addroombutton;
    EditText roomnumberet, roompriceet;
    CheckBox feature1, feature2;

    ArrayList<Uri> gallerypictures, firebasepictures;
    LinearLayout pictureslinearlayout;


    final static int PERMISSION_ALL = 1;
    public static final int GalleryRequestCodeID = 1;

    Firebase root_url;
    public int icounter;
    ProgressDialog progressDialog;

    boolean updating_room;

    public static int DpToPx(float dp, Context context) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }
    //Permissions
    public Boolean CheckPermissions(){
        int writeStorage = getApplicationContext().checkCallingOrSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return writeStorage == PackageManager.PERMISSION_GRANTED;
    }
    public void GetPermissions(){
        String[] PERMISSIONS = {android.Manifest.permission.WRITE_EXTERNAL_STORAGE};
        ActivityCompat.requestPermissions(add_room.this, PERMISSIONS, PERMISSION_ALL);
    }
    public static Bitmap RecizeBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);
        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_room);
        InitializeVariables();
        CheckToLoadRoom();
        SetListners();
    }
    public void CheckToLoadRoom(){
        Room temp_room = (Room) getIntent().getSerializableExtra("room");
        updating_room = false;
        if(temp_room != null){
            updating_room = true;
            roomnumberet.setText(temp_room.getRoomnumber());
            roomnumberet.setFocusable(false);
            roomnumberet.setFocusableInTouchMode(false);
            roomnumberet.setClickable(false);

            roompriceet.setText(temp_room.getRoomprice());

            if(temp_room.getF1() == 1)
                feature1.setChecked(true);
            if(temp_room.getF2() == 1)
                feature2.setChecked(true);


            for(int i = 0 ; i < temp_room.getPictures().size();i++)
                AddImageFromFirebase(temp_room.getPictures().get(i));
        }
    }
    public void AddImageFromFirebase(final String imageURL){
        int tempPadding = 10;
        ImageView tempImageView = new ImageView(add_room.this);
        tempImageView.setLayoutParams(new RelativeLayout.LayoutParams(DpToPx(100,add_room.this),
                DpToPx(100,add_room.this)));
        tempImageView.setPadding(tempPadding,tempPadding,tempPadding,tempPadding);
        Picasso.with(add_room.this).load(imageURL).resize(DpToPx(100, add_room.this), DpToPx(100, add_room.this)) // resizes the image to these dimensions (in pixel)
                .centerCrop().into(tempImageView);

        RelativeLayout tempRelativeLayout = new RelativeLayout(add_room.this);
        tempRelativeLayout.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        TextView tempTextView = new TextView(add_room.this);
        tempTextView.setLayoutParams(new RelativeLayout.LayoutParams(0,
                0));
        tempTextView.setText(imageURL);
        tempRelativeLayout.addView(tempTextView);
        tempRelativeLayout.addView(tempImageView);
        tempRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                                String tempImageURL = ((TextView) ((RelativeLayout)v).getChildAt(0)).getText().toString();
                                for (int i = 0; i < firebasepictures.size(); i++){
                                    if(firebasepictures.get(i).toString().equals(tempImageURL)){
                                        firebasepictures.remove(i);
                                        break;
                                    }
                                }
                ((ViewManager)v.getParent()).removeView(v);

            }
        });
        pictureslinearlayout.addView(tempRelativeLayout);
        firebasepictures.add(Uri.parse(imageURL));
    }
    public void InitializeVariables(){
        root_url = new Firebase(FConstants.rootlink);
        addroombutton = (Button) findViewById(R.id.addroom_uploadroom);
        addimagebutton = (Button) findViewById(R.id.addroom_uploadimage);
        feature1 = (CheckBox) findViewById(R.id.addroom_f1);
        feature2 = (CheckBox) findViewById(R.id.addroom_f2);
        roomnumberet = (EditText) findViewById(R.id.addroom_roomnumber);
        roompriceet = (EditText) findViewById(R.id.addroom_roomprice);
        pictureslinearlayout = (LinearLayout) findViewById(R.id.addroom_picturell);
        gallerypictures = new ArrayList<>();
        firebasepictures = new ArrayList<>();

        progressDialog = new ProgressDialog(add_room.this);
        progressDialog.setMessage("Uploading Room");
        progressDialog.setCancelable(false);
    }
    public void SetListners(){
        addimagebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(CheckPermissions())
                    GetImageFromGallery();
                else
                    GetPermissions();
            }
        });
        addroombutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UploadRoom();
            }
        });
    }
    public void RoomApproved(){
        progressDialog.show();
        String roomNumber, roomPrice;
        roomNumber = roomnumberet.getText().toString();
        roomPrice = roompriceet.getText().toString();
        int f1 = 0, f2 = 0;

        if(feature1.isChecked())
            f1 = 1;
        if(feature2.isChecked())
            f2 = 1;
        Firebase room_url = root_url.child(FConstants.Room_Table).child(roomNumber);
        room_url.child(FConstants.Room_Table_roomprice).setValue(roomPrice);
        room_url.child(FConstants.Room_Table_roomf1).setValue(f1);
        room_url.child(FConstants.Room_Table_roomf2).setValue(f2);
        room_url.child(FConstants.Room_Table_available).setValue(1);

        for(int i = 0 ; i < gallerypictures.size();i++)
            UploadImageOnFirebase(gallerypictures.get(i));

    }
    public void UploadPicURLSOnFirebase(){
        String roomNumber;
        roomNumber = roomnumberet.getText().toString();
        for (int i = 0 ; i < firebasepictures.size() ; i++)
            root_url.child(FConstants.Room_Table).child(roomNumber).child(FConstants.Room_Table_roompictures)
                    .push().setValue(firebasepictures.get(i).toString());

        progressDialog.hide();
    }
    public void UploadImageOnFirebase(Uri uploadProfilePictureURI){
        StorageReference mStorage;
        mStorage = FirebaseStorage.getInstance().getReference();
        StorageReference filePath = mStorage.child("RoomPictures").child(uploadProfilePictureURI.getLastPathSegment());
        filePath.putFile(uploadProfilePictureURI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                firebasepictures.add(taskSnapshot.getDownloadUrl());
                icounter++;
                if(icounter == gallerypictures.size()) {
                    UploadPicURLSOnFirebase();
                }
            }
        });
    }
    public void UploadRoom(){
        String roomNumber, roomPrice;
        roomNumber = roomnumberet.getText().toString();
        roomPrice = roompriceet.getText().toString();

        if(roomNumber.equals("") || roomPrice.equals("") || (gallerypictures.size() == 0 && firebasepictures.size()==0))
            Toast.makeText(getApplicationContext(),"Incomplete Details", Toast.LENGTH_SHORT).show();

        else if(updating_room) {
            root_url.child(FConstants.Room_Table).child(roomNumber).removeValue();
            RoomApproved();
        }
        else{
            root_url.child(FConstants.Room_Table).child(roomNumber).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(!dataSnapshot.exists()){
                        RoomApproved();
                    }
                    else
                        Toast.makeText(getApplicationContext(),"Room Exists", Toast.LENGTH_SHORT).show();
                }
                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });
        }
    }
    public void GetImageFromGallery(){
        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto, GalleryRequestCodeID);
    }
    public void UploadPicture(Bitmap imageBitmap, String imageurl){
        int tempPadding = 10;
        Bitmap recizedBitmap = RecizeBitmap(imageBitmap,imageBitmap.getWidth()/3,imageBitmap.getHeight()/3);
        ImageView tempImageView = new ImageView(add_room.this);
        tempImageView.setLayoutParams(new RelativeLayout.LayoutParams(DpToPx(100,add_room.this),
                DpToPx(100,add_room.this)));
        tempImageView.setImageBitmap(recizedBitmap);
        tempImageView.setPadding(tempPadding,tempPadding,tempPadding,tempPadding);
        tempImageView.setScaleType(ImageView.ScaleType.FIT_XY);

        RelativeLayout tempRelativeLayout = new RelativeLayout(add_room.this);
        tempRelativeLayout.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        TextView tempTextView = new TextView(add_room.this);
        tempTextView.setLayoutParams(new RelativeLayout.LayoutParams(0,
                0));
        tempTextView.setText(imageurl);
        tempRelativeLayout.addView(tempTextView);
        tempRelativeLayout.addView(tempImageView);
        tempRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                                String tempImageURL = ((TextView) ((RelativeLayout)v).getChildAt(0)).getText().toString();
                                for (int i = 0; i < gallerypictures.size(); i++) {
                                    if (gallerypictures.toString().equals(tempImageURL)) {
                                        gallerypictures.remove(i);
                                        break;
                                    }
                                }
                ((ViewManager)v.getParent()).removeView(v);
            }
        });
        pictureslinearlayout.addView(tempRelativeLayout);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GalleryRequestCodeID && resultCode == Activity.RESULT_OK){
            Uri uri = data.getData();
            try {
                gallerypictures.add(uri);
               UploadPicture(MediaStore.Images.Media.getBitmap(add_room.this.getContentResolver(),uri),uri.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
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