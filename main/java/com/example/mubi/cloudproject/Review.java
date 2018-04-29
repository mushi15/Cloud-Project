package com.example.mubi.cloudproject;

public class Review {
    String username, roomnumber, review;
    int rating;

    public Review(String username, String roomnumber, String review, int rating){
        this.username = username;
        this.roomnumber = roomnumber;
        this.review = review;
        this.rating = rating;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRoomnumber() {
        return roomnumber;
    }

    public void setRoomnumber(String roomnumber) {
        this.roomnumber = roomnumber;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}