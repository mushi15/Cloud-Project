package com.example.mubi.cloudproject;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;


import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;

public class view_rooms extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<Room> firebaseRooms;
    int roomCounter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_rooms);
        roomCounter = 0;
        LoadFromFirebase();
    }
    public void SetRV(){
        recyclerView = (RecyclerView) findViewById(R.id.deleteroom_recyclerview);
        view_rooms_adapter adapter = new view_rooms_adapter(view_rooms.this,firebaseRooms,1);
        recyclerView.setAdapter(adapter);
        final GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(),1);
        recyclerView.setLayoutManager(gridLayoutManager);
    }
    public void LoadImageURL(){
        Firebase rootURL = new Firebase(FConstants.rootlink);
        rootURL.child(FConstants.Room_Table).child(firebaseRooms.get(roomCounter).getRoomnumber()).child(FConstants.Room_Table_roompictures).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds: dataSnapshot.getChildren()){
                        firebaseRooms.get(roomCounter).addPicture(ds.getValue().toString());
                }
                if(++roomCounter == firebaseRooms.size())
                    SetRV();
                else
                    LoadImageURL();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }
    public void LoadFromFirebase(){
        firebaseRooms = new ArrayList<>();
        Firebase rootURL = new Firebase(FConstants.rootlink);
        rootURL.child(FConstants.Room_Table).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot != null) {
                    for (DataSnapshot dataSnapshotChild : dataSnapshot.getChildren()) {
                        if(dataSnapshotChild != null){
                            String Roomnumber = dataSnapshotChild.getKey();
                            int Roomf1 = Integer.valueOf(dataSnapshotChild.child("roomf1").getValue().toString());
                            int Roomf2 = Integer.valueOf(dataSnapshotChild.child("roomf2").getValue().toString());
                            int availability = Integer.valueOf(dataSnapshotChild.child("available").getValue().toString());
                            String RoomPrice = dataSnapshotChild.child("roomprice").getValue().toString();
                            Room troom = new Room(Roomnumber,RoomPrice,Roomf1,Roomf2,availability);
                            if(availability == 1)
                                firebaseRooms.add(troom);
                        }
                    }
                    LoadImageURL();
                }
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

    }
}