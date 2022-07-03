package com.example.jhub.Adapter;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jhub.CommentActivity;
import com.example.jhub.LikesActivity;
import com.example.jhub.Model.LikesModel;
import com.example.jhub.Model.NotificationModel;
import com.example.jhub.Model.PostModel;
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

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.viewHolder>{

    ArrayList<PostModel> list;
    Context context;



    public PostAdapter(ArrayList<PostModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.dashboard_rv_sample,parent,false);

        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        PostModel model = list.get(position);

        Picasso.get().load(model.getPost_image()).placeholder(R.drawable.placeholder).into(holder.postImage);
        holder.description.setText(model.getDescription());





        holder.optionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(context, holder.optionsButton);
                //inflating menu from xml resource
                popup.inflate(R.menu.options_menu);
                //adding click listener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.report:
                                Toast.makeText(context,"reported Succesfully",Toast.LENGTH_SHORT).show();
                                return true;

                            default:
                                return false;
                        }
                    }
                });
                //displaying the popup
                popup.show();
            }
        });





        FirebaseDatabase.getInstance().getReference().child("Users").child(model.getPostedBy()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserModel userModel = snapshot.getValue(UserModel.class);
                holder.userName.setText(userModel.getName());
                holder.about.setText(userModel.getRollNumber());
                Picasso.get().load(userModel.getProfile_photo()).placeholder(R.drawable.placeholder).into(holder.profile);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




        FirebaseDatabase.getInstance().getReference().child("Posts").child(model.getPost_id()).child("comments")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        holder.numberofComments.setText(snapshot.getChildrenCount()+" comments");
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });





        isLiked(model.getPost_id(),holder.likes);
        nrLikes(model.getPost_id(),holder.likesCount,model);

        isSaved(model.getPost_id(),holder.saveBtn);

        holder.save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.saveBtn.getTag().equals("like")){
                    FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getUid())
                            .child("Saved").child(model.getPost_id()).setValue(true);

                    Toast.makeText(context, "post saved Successfully", Toast.LENGTH_SHORT).show();

                }
                else {
                    FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getUid())
                            .child("Saved").child(model.getPost_id()).removeValue();
                }
            }
        });



        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.likes.getTag().equals("like")){
                    LikesModel likesModel = new LikesModel();
                    likesModel.setLikedby(FirebaseAuth.getInstance().getUid());
                    likesModel.setLikedAt(new Date().getTime());
                    FirebaseDatabase.getInstance().getReference().child("Posts").child(model.getPost_id()).child("Likes")
                            .child(FirebaseAuth.getInstance().getUid()).setValue(likesModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            NotificationModel notificationModel = new NotificationModel();
                            notificationModel.setNotificationBy(FirebaseAuth.getInstance().getUid());
                            notificationModel.setNotificationAt(new Date().getTime());
                            notificationModel.setType("like");
                            notificationModel.setPostId(model.getPost_id());
                            notificationModel.setPostedBy(model.getPostedBy());

                            FirebaseDatabase.getInstance().getReference().child("Notification").child(model.getPostedBy())
                                    .push().setValue(notificationModel);

                        }
                    });

                }
                else {
                    FirebaseDatabase.getInstance().getReference().child("Posts").child(model.getPost_id()).child("Likes")
                            .child(FirebaseAuth.getInstance().getUid()).removeValue();
                }
            }
        });

        holder.profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,ProfileActivity.class);
                intent.putExtra("user_id",model.getPostedBy());
                context.startActivity(intent);
            }
        });
        holder.userName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,ProfileActivity.class);
                intent.putExtra("user_id",model.getPostedBy());
                context.startActivity(intent);
            }
        });
        holder.about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,ProfileActivity.class);
                intent.putExtra("user_id",model.getPostedBy());
                context.startActivity(intent);
            }
        });


        holder.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CommentActivity.class);
                intent.putExtra("postId",model.getPost_id());
                context.startActivity(intent);
            }
        });

        holder.numberofComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CommentActivity.class);
                intent.putExtra("postId",model.getPost_id());
                context.startActivity(intent);
            }
        });

        holder.showLikes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, LikesActivity.class);
                intent.putExtra("postId",model.getPost_id());
                context.startActivity(intent);
            }
        });
















    }











    @Override
    public int getItemCount() {
        return list.size();
    }

    public class  viewHolder extends RecyclerView.ViewHolder{

        ImageView profile,postImage,likes;
        ImageView saveBtn;
        TextView userName,about,comments,share;
        TextView likesCount;
        TextView description;

        LinearLayout linearLayout,comment,save;

        TextView numberofComments;


        ImageView optionsButton;

        TextView showLikes;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            profile = itemView.findViewById(R.id.usser_pp);
            postImage = itemView.findViewById(R.id.post_image);
            userName = itemView.findViewById(R.id.name);
            about =  itemView.findViewById(R.id.roll);
            likes = itemView.findViewById(R.id.like_btn);

            showLikes = itemView.findViewById(R.id.show_likes);

            likesCount = itemView.findViewById(R.id.show_likes);

            numberofComments=itemView.findViewById(R.id.no_of_comments);


            linearLayout = itemView.findViewById(R.id.like_back);

            description = itemView.findViewById(R.id.description);

            comment = itemView.findViewById(R.id.liner_2);

            save = itemView.findViewById(R.id.linear_3);

            saveBtn =itemView.findViewById(R.id.share_btn);

            optionsButton = itemView.findViewById(R.id.options_btn);


        }
    }


    private void isLiked(String postid,ImageView imageView){

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Posts").child(postid).child("Likes");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(FirebaseAuth.getInstance().getUid()).exists()){
                    imageView.setImageResource(R.drawable.like_highlighted);
                    imageView.setTag("liked");
                }
                else{
                    imageView.setImageResource(R.drawable.like);
                    imageView.setTag("like");

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void isSaved(String postid,ImageView imageView){

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getUid())
                .child("Saved");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(postid).exists()){
                    imageView.setImageResource(R.drawable.save_highlighted);
                    imageView.setTag("liked");
                }
                else{
                    imageView.setImageResource(R.drawable.saved);
                    imageView.setTag("like");

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void nrLikes(String postid,TextView textView,PostModel model){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Posts").child(postid).child("Likes");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                textView.setText(snapshot.getChildrenCount()+" likes");

                FirebaseDatabase.getInstance().getReference().child("Posts").child(postid).child("postLike").setValue(snapshot.getChildrenCount());


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }



}
