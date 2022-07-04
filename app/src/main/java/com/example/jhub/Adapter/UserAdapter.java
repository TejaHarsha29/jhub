package com.example.jhub.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jhub.Model.BffsModel;
import com.example.jhub.Model.CrushModel;
import com.example.jhub.Model.NotificationModel;
import com.example.jhub.Model.StarsModel;
import com.example.jhub.Model.UserModel;
import com.example.jhub.ProfileActivity;
import com.example.jhub.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.viewHolder> {

    Context context;
    ArrayList<UserModel> list;



    ArrayList<UserModel> filterlistt;

    public void setFilterlistt(ArrayList<UserModel> filterlistt) {
        this.list = filterlistt;
        notifyDataSetChanged();
    }

    public UserAdapter(Context context, ArrayList<UserModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.user_sample,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {

        UserModel user = list.get(position);
        Picasso.get().load(user.getProfile_photo()).placeholder(R.drawable.man).into(holder.user_pp);
        holder.name.setText(user.getName());
        holder.rollNumber.setText(user.getRollNumber());



        holder.user_pp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProfileActivity.class);
                intent.putExtra("user_id",user.getUser_id().toString());
                context.startActivity(intent);
            }
        });
        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProfileActivity.class);
                intent.putExtra("user_id",user.getUser_id().toString());
                context.startActivity(intent);
            }
        });
        holder.rollNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProfileActivity.class);
                intent.putExtra("user_id",user.getUser_id().toString());
                context.startActivity(intent);
            }
        });



        isStarred(user.getUser_id(), holder.star,"Stars",R.drawable.star_highlight,R.drawable.star);
        nrStars(user.getUser_id(),holder.about,"Stars","stars");

        isStarred(user.getUser_id(), holder.crush,"Crush",R.drawable.rose_highlight,R.drawable.rose);
        nrStars(user.getUser_id(),holder.crush_count,"Crush","crushes");

        isStarred(user.getUser_id(), holder.dosth,"BestFriends",R.drawable.friend_highlight,R.drawable.friend);
        nrStars(user.getUser_id(),holder.dosth_count,"BestFriends","bffs");





        holder.star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.star.getTag().equals("like")){
                    StarsModel starsModel =new StarsModel(FirebaseAuth.getInstance().getUid(),new Date().getTime());
                    FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUser_id()).child("Stars")
                            .child(FirebaseAuth.getInstance().getUid()).setValue(starsModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            NotificationModel notificationModel = new NotificationModel();
                            notificationModel.setNotificationBy(FirebaseAuth.getInstance().getUid());
                            notificationModel.setNotificationAt(new Date().getTime());
                            notificationModel.setType("follow");

                            FirebaseDatabase.getInstance().getReference().child("Notification").child(user.getUser_id()).
                                    push().setValue(notificationModel);

                        }
                    });

                }
                else {
                    FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUser_id()).child("Stars")
                            .child(FirebaseAuth.getInstance().getUid()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {

                        }
                    });
                }
            }
        });


        holder.crush.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.crush.getTag().equals("like")){
                    CrushModel crushModel =new CrushModel(FirebaseAuth.getInstance().getUid(),new Date().getTime());
                    FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUser_id()).child("Crush")
                            .child(FirebaseAuth.getInstance().getUid()).setValue(crushModel);

                }
                else {
                    FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUser_id()).child("Crush")
                            .child(FirebaseAuth.getInstance().getUid()).removeValue();
                }
            }
        });


        holder.dosth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.dosth.getTag().equals("like")){
                    BffsModel bffsModel =new BffsModel(FirebaseAuth.getInstance().getUid(),new Date().getTime());
                    FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUser_id()).child("BestFriends")
                            .child(FirebaseAuth.getInstance().getUid()).setValue(bffsModel);

                }
                else {
                    FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUser_id()).child("BestFriends")
                            .child(FirebaseAuth.getInstance().getUid()).removeValue();
                }
            }
        });









    }





    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder{

        ImageView user_pp,star;
        TextView name,about,rollNumber;
        ImageView crush,dosth;
        TextView crush_count,dosth_count;


        public viewHolder(@NonNull View itemView) {
            super(itemView);
            user_pp = itemView.findViewById(R.id.usser_pp);
            star = itemView.findViewById(R.id.star_count);
            name = itemView.findViewById(R.id.name);
            about = itemView.findViewById(R.id.about);


            rollNumber = itemView.findViewById(R.id.roll);

            crush=itemView.findViewById(R.id.rose);
            dosth=itemView.findViewById(R.id.dosth);
            crush_count=itemView.findViewById(R.id.textView12);
            dosth_count=itemView.findViewById(R.id.textView5);





        }
    }

    private void isStarred(String userid,ImageView imageView,String folder,int highlight,int star){

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users").child(userid).child(folder);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(FirebaseAuth.getInstance().getUid()).exists()){
                    imageView.setImageResource(highlight);
                    imageView.setTag("liked");
                }
                else{
                    imageView.setImageResource(star);
                    imageView.setTag("like");

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void nrStars(String userid,TextView textView,String folder,String typee){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users").child(userid).child(folder);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                textView.setText(snapshot.getChildrenCount()+" "+typee);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }


}
