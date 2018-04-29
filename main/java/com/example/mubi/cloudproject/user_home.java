package com.example.mubi.cloudproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class user_home extends AppCompatActivity {
    Button availablerooms, bookedrooms, logout, updateprofile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_home);
        Initialize();
        Listners();
    }
    public void Initialize(){
        availablerooms = (Button) findViewById(R.id.userhome_availablerooms);
        bookedrooms = (Button) findViewById(R.id.userhome_bookedrooms);
        logout = (Button) findViewById(R.id.userhome_logout);
        updateprofile = (Button) findViewById(R.id.userhome_updateprofile);
    }
    public void Listners(){
        availablerooms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(user_home.this, view_rooms.class);
                startActivity(intent);
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = getSharedPreferences("User_Pref", MODE_PRIVATE).edit();
                editor.putString("username","");
                editor.putString("password", "");
                editor.putString("picture", "");
                editor.putInt("type", -1);
                editor.apply();

                Intent intent = new Intent(user_home.this,login.class);
                startActivity(intent);
                finish();
            }
        });
    }
}