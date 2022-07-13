package com.appsByHarsha.jhub.Adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.appsByHarsha.jhub.Model.UserModel;
import com.appsByHarsha.jhub.Model.NotificationModel;
import com.appsByHarsha.jhub.R;
import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.viewHolder>{

    ArrayList<NotificationModel> list;
    Context context;

    public NotificationAdapter(ArrayList<NotificationModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.notification_sample,parent,false);

        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {

        NotificationModel notificationModel = list.get(position);

        notificationModel.setCheckOpen(true);


        FirebaseDatabase.getInstance().getReference().child("Notification").child(FirebaseAuth.getInstance().getUid())
                .child(notificationModel.getNotId()).setValue(notificationModel);

        String type = notificationModel.getType();
        String time = TimeAgo.using(notificationModel.getNotificationAt());


        holder.time.setText(time);


        FirebaseDatabase.getInstance().getReference().child("Users").child(notificationModel.getNotificationBy())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        UserModel userModel = snapshot.getValue(UserModel.class);
                        Picasso.get().load(userModel.getProfile_photo()).placeholder(R.drawable.placeholder).into(holder.profile);

                        if(type.equals("like")){
                            holder.name.setText(Html.fromHtml("<b>"+userModel.getName()+"</b>"+" liked your post"));
                        }
                        else if(type.equals("Comment")){
                            holder.name.setText(Html.fromHtml("<b>"+userModel.getName()+"</b>"+" commented on your post"));
                        }else{
                            holder.name.setText(Html.fromHtml("<b>"+userModel.getName()+"</b>"+" gave you a star"));
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });



        holder.openNoti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });



    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder{

        ImageView profile;
        TextView name,comment,time;
        ConstraintLayout openNoti;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            profile = itemView.findViewById(R.id.usser_pp);
            name = itemView.findViewById(R.id.name);
            time = itemView.findViewById(R.id.about_pp);
            openNoti = itemView.findViewById(R.id.openNoti);
        }
    }
}
