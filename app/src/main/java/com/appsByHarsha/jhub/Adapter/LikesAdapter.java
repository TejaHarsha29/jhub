package com.appsByHarsha.jhub.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.appsByHarsha.jhub.Model.UserModel;
import com.appsByHarsha.jhub.Model.LikesModel;
import com.appsByHarsha.jhub.R;
import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class LikesAdapter extends RecyclerView.Adapter<LikesAdapter.viewHolder>{

    ArrayList<LikesModel> list;
    Context context;

    public LikesAdapter(ArrayList<LikesModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.like_rv_sample,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {

        LikesModel likesModel = list.get(position);

        String time = TimeAgo.using(likesModel.getLikedAt());

        FirebaseDatabase.getInstance().getReference().child("Users").child(likesModel.getLikedby()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserModel userModel = snapshot.getValue(UserModel.class);

                holder.name.setText(userModel.getName());
                Picasso.get().load(userModel.getProfile_photo()).placeholder(R.drawable.placeholder).into(holder.profile);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        holder.time.setText(time);






    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder{

        ImageView profile;
        TextView name,time;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            profile = itemView.findViewById(R.id.top_user);
            name = itemView.findViewById(R.id.name);
            time = itemView.findViewById(R.id.about_pp);

        }
    }
}
