package com.example.mubi.cloudproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

public class login extends AppCompatActivity {

    EditText LoginUsername;
    EditText LoginPassword;
    Button LoginButton, signupbutton;
    Firebase Root_URL;


    public void CheckLogin(){
            SharedPreferences prefs = getSharedPreferences("User_Pref", MODE_PRIVATE);
            String tu = prefs.getString("username", null);
            String tp = prefs.getString("password",null);
            String pic = prefs.getString("picture",null);
            int t = prefs.getInt("type",0);

        assert tu != null;
        if(!tu.equals("")){
                if(t == 0){
                    Intent intent = new Intent(login.this,user_home.class);
                    startActivity(intent);
                }
            }

    }
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        Initialize();
        Listeners();
    }

    public void Initialize()
    {
        LoginUsername = (EditText)findViewById(R.id.Login_Username);
        LoginPassword= (EditText)findViewById(R.id.Login_Password);
        LoginButton= (Button)findViewById(R.id.Login_LoginButton);
        Root_URL = new Firebase(FConstants.rootlink);
        signupbutton = (Button) findViewById(R.id.login_signup);

    }
    public void Login()
    {
        final String Username = LoginUsername.getText().toString();
        final String Password = LoginPassword.getText().toString();
        Firebase userExists = Root_URL.child(FConstants.User_Table).child(Username);
        userExists.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists())
                    Toast.makeText(getApplicationContext(),"Incorrect credentials.",Toast.LENGTH_SHORT).show();
                else {
                    String rUsername = dataSnapshot.child(FConstants.User_Table_username).getValue().toString();
                    String rPassword = dataSnapshot.child(FConstants.User_Table_password).getValue().toString();
                    String rPictureURL = dataSnapshot.child(FConstants.User_Table_imageurl).getValue().toString();
                    int rType = Integer.valueOf(dataSnapshot.child(FConstants.User_Table_type).getValue().toString());
                    if (rUsername.equals(Username) && rPassword.equals(Password)) {
                        SharedPreferences.Editor editor = getSharedPreferences("User_Pref", MODE_PRIVATE).edit();
                        editor.putString("username",rUsername);
                        editor.putString("password", rPassword);
                        editor.putString("picture", rPictureURL);
                        editor.putInt("type", rType);
                        editor.apply();
                        if(rType == 0){
                            Intent intent = new Intent(login.this,user_home.class);
                            startActivity(intent);
                        }
                        else if(rType == 1){

                        }
                    } else
                        Toast.makeText(getApplicationContext(), "Incorrect credentials.", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    public void Listeners()
    {
        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Login();
            }
        });
        signupbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(login.this, signup.class);
                startActivity(intent);
            }
        });
    }
}