package com.example.mubi.cloudproject;

import android.widget.ImageView;

public class User {

    String username;
    String password;
    String profileImage;
    int type;
    public User(){
        username = password = profileImage = "";
    }
    public User(String U, String P, String I, int t)
    {
        username = U;
        password = P;
        profileImage = I;
        type = t;

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        profileImage = profileImage;
    }
}