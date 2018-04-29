package com.example.mubi.cloudproject;

import java.io.Serializable;
import java.util.ArrayList;

public class Room implements Serializable{
    public String roomnumber, roomprice;
    int f1, f2, available; // 0 for no 1 for yes
    ArrayList<String> pictures;

       public Room(String rn, String rp, int f1, int f2, int available, ArrayList<String> pics){
        this.roomnumber = rn;
        this.roomprice = rp;
        this.f1 = f1;
        this.f2 = f2;
        this.pictures = pics;
        this.available = available;
    }
    public Room(String rn, String rp, int f1, int f2, int available){
        this.roomnumber = rn;
        this.roomprice = rp;
        this.f1 = f1;
        this.f2 = f2;
        this.available = available;
        this.pictures = new ArrayList<>();

    }

    public int getAvailable() {
        return available;
    }

    public void setAvailable(int available) {
        this.available = available;
    }

    public String getRoomnumber() {
        return roomnumber;
    }

    public void setRoomnumber(String roomnumber) {
        this.roomnumber = roomnumber;
    }

    public String getRoomprice() {
        return roomprice;
    }

    public void setRoomprice(String roomprice) {
        this.roomprice = roomprice;
    }

    public int getF1() {
        return f1;
    }

    public void setF1(int f1) {
        this.f1 = f1;
    }

    public int getF2() {
        return f2;
    }

    public void setF2(int f2) {
        this.f2 = f2;
    }

    public ArrayList<String> getPictures() {
        return pictures;
    }

    public void setPictures(ArrayList<String> pictures) {
        this.pictures = pictures;
    }

    public void addPicture(String url){
        pictures.add(url);
    }
}