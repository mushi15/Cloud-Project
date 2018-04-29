package com.example.mubi.cloudproject;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class deleteuser_adapter extends RecyclerView.Adapter<deleteuser_adapter.MyViewHolder> {

    ArrayList<User> usersList;
    Context context;

    public static float pxFromDp(final Context context, final float dp) {
        return dp * context.getResources().getDisplayMetrics().density;
    }

    public deleteuser_adapter(Context context,ArrayList<User> arrayList){
        this.context = context;
        this.usersList = arrayList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView userNameTextView;
        public ImageButton deleteUserButton;
        public CircleImageView CImage;

        public MyViewHolder(View view) {
            super(view);
            userNameTextView = (TextView) view.findViewById(R.id.deleteuseradapter_username);
            deleteUserButton = (ImageButton) view.findViewById(R.id.deleteuseradapter_delete);
            CImage = (CircleImageView) view.findViewById(R.id.deleteuseradapter_picture);
        }
    }

    // returns XML FILE
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.deleteuser_adapterview, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final User singleUser = usersList.get(position);
        holder.userNameTextView.setText(singleUser.getUsername());
        if(!singleUser.getProfileImage().equals(""))
            Picasso.with(context).load(singleUser.getProfileImage()).resize((int) pxFromDp(context,60), (int) pxFromDp(context,60))
                    .centerInside().into(holder.CImage);
        holder.deleteUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Firebase root = new Firebase(FConstants.rootlink);
                root.child(FConstants.User_Table).child(singleUser.getUsername()).removeValue();
                usersList.remove(holder.getAdapterPosition());
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }
}