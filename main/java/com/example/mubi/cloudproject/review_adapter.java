package com.example.mubi.cloudproject;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class review_adapter extends RecyclerView.Adapter<review_adapter.MyViewHolder> {
    public ArrayList<Review> review_list;
    public Context context;

    public static int DpToPx(float dp, Context context) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView username;
        TextView review;
        RatingBar rating;
        CircleImageView profilepic;
        public MyViewHolder(View view) {
            super(view);
            username = (TextView) view.findViewById(R.id.reviewadapter_username);
            review = (TextView) view.findViewById(R.id.reviewadapter_review);
            profilepic = (CircleImageView) view.findViewById(R.id.reviewadapter_picture);
            rating  = (RatingBar) view.findViewById(R.id.reviewadapter_rating);
        }
    }
    public review_adapter(Context context, ArrayList<Review> reviewList) {
        this.context = context;
        this.review_list = reviewList;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.review_adapter, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        Firebase root_url = new Firebase(FConstants.rootlink);
        Review singleReview = review_list.get(position);
        holder.username.setText(singleReview.getUsername());
        holder.review.setText(singleReview.getReview());
        holder.rating.setRating(singleReview.getRating());
        root_url.child(FConstants.User_Table).child(singleReview.getUsername()).child(FConstants.User_Table_imageurl).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    String picPath = dataSnapshot.getValue().toString();
                    if(!picPath.equals(""))
                        Picasso.with(context).load(picPath).resize(DpToPx(100, context), DpToPx(100, context)) // resizes the image to these dimensions (in pixel)
                                .centerCrop().into(holder.profilepic);
                }
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

    }
    @Override
    public int getItemCount() {
        return review_list.size();
    }
}