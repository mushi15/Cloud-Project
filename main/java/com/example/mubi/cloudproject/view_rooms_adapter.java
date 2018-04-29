package com.example.mubi.cloudproject;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.ButtonBarLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.firebase.client.Firebase;

import java.util.ArrayList;

public class view_rooms_adapter extends RecyclerView.Adapter<view_rooms_adapter.MyViewHolder> {

    ArrayList<Room> RoomList;
    Context context;
    int user_type;

    public static float pxFromDp(final Context context, final float dp) {
        return dp * context.getResources().getDisplayMetrics().density;
    }

    public view_rooms_adapter(Context context,ArrayList<Room> arrayList, int user_type){
        this.context = context;
        this.RoomList = arrayList;
        this.user_type = user_type;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView rnumTextView;
        public TextView rpriceTextView;
        public ImageButton deleteRoomButton;
        Button viewRoomButton, updateRoomButton;


        public MyViewHolder(View view) {
            super(view);
            rnumTextView = (TextView) view.findViewById(R.id.deleteroomadapter_rnum);
            rpriceTextView = (TextView) view.findViewById(R.id.deleteroomadapter_rprice);
            viewRoomButton = (Button) view.findViewById(R.id.deleteroomadapter_view);
            updateRoomButton = (Button) view.findViewById(R.id.deleteroomadapter_update);
            deleteRoomButton = (ImageButton) view.findViewById(R.id.deleteroomadapter_delete);
        }
    }

    // returns XML FILE
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.viewrooms_adapterview, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final Room singleRoom = RoomList.get(position);
        holder.rnumTextView.setText(singleRoom.getRoomnumber());
        holder.rpriceTextView.setText(singleRoom.getRoomprice());
        if(user_type == 1) {
            holder.deleteRoomButton.setVisibility(View.VISIBLE);
            holder.deleteRoomButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Firebase root = new Firebase(FConstants.rootlink);
                    root.child(FConstants.Room_Table).child(singleRoom.getRoomnumber()).removeValue();
                    RoomList.remove(holder.getAdapterPosition());
                    notifyDataSetChanged();
                }
            });
            holder.updateRoomButton.setVisibility(View.VISIBLE);
            holder.updateRoomButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context,add_room.class);
                    intent.putExtra("room",RoomList.get(holder.getAdapterPosition()));
                    context.startActivity(intent);
                }
            });
        }
        holder.viewRoomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,view_room.class);
                intent.putExtra("room",RoomList.get(holder.getAdapterPosition()));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return RoomList.size();
    }
}