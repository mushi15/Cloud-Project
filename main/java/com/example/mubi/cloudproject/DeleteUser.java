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

public class DeleteUser extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<User> firebaseUsers;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delete_user);
        LoadFromFirebase();
    }
    public void SetRV(){
        recyclerView = (RecyclerView) findViewById(R.id.deleteuser_recyclerview);
        deleteuser_adapter adapter = new deleteuser_adapter(DeleteUser.this,firebaseUsers);
        recyclerView.setAdapter(adapter);
        final GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(),1);
        recyclerView.setLayoutManager(gridLayoutManager);
    }
    public void LoadFromFirebase(){
        firebaseUsers = new ArrayList<>();
        Firebase rootURL = new Firebase(FConstants.rootlink);
        rootURL.child(FConstants.User_Table).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot != null) {
                    for (DataSnapshot dataSnapshotChild : dataSnapshot.getChildren()) {
                        if(dataSnapshotChild != null){
                            String userName = dataSnapshotChild.child("username").getValue().toString();
                            String passWord = dataSnapshotChild.child("password").getValue().toString();
                            String profile_url = dataSnapshotChild.child("profileImage").getValue().toString();
                            int type = Integer.valueOf(dataSnapshotChild.child("type").getValue().toString());
                            User tuser = new User(userName,passWord,profile_url,type);
                            firebaseUsers.add(tuser);
                        }
                    }
                    SetRV();
                }
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

    }
}