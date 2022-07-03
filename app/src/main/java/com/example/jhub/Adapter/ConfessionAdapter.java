package com.example.jhub.Adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jhub.Model.ConfessionModel;
import com.example.jhub.Model.LikesModel;
import com.example.jhub.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;

public class ConfessionAdapter extends RecyclerView.Adapter<ConfessionAdapter.viewHolder>{

    Context context;
    ArrayList<ConfessionModel> list =new ArrayList<>();

    public ConfessionAdapter(Context context, ArrayList<ConfessionModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.confession_sample_rv,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {

        ConfessionModel confessionModel = list.get(position);

        holder.confess_text.setText(confessionModel.getConfessionText());

        isLiked(confessionModel.getConfession_id(),holder.like_btn);
        nrLikes(confessionModel.getConfession_id(),holder.likes);

        holder.like_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.like_btn.getTag().equals("like")){
                    LikesModel likesModel = new LikesModel();
                    likesModel.setLikedby(FirebaseAuth.getInstance().getUid());
                    likesModel.setLikedAt(new Date().getTime());
                    FirebaseDatabase.getInstance().getReference().child("Confessions").child(confessionModel.getConfession_id()).child("Likes")
                            .child(FirebaseAuth.getInstance().getUid()).setValue(likesModel);

                }
                else {
                    FirebaseDatabase.getInstance().getReference().child("Confessions").child(confessionModel.getConfession_id()).child("Likes")
                            .child(FirebaseAuth.getInstance().getUid()).removeValue();
                }
            }
        });



        holder.report_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(context, holder.report_btn);
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




    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder{

        TextView confess_text;
        ImageView like_btn;
        ImageView report_btn;
        TextView likes;
        public viewHolder(@NonNull View itemView) {
            super(itemView);

            confess_text = itemView.findViewById(R.id.confess_txt);
            like_btn = itemView.findViewById(R.id.like_btn);
            report_btn = itemView.findViewById(R.id.report_btn);
            likes = itemView.findViewById(R.id.likessss);
        }
    }

    private void isLiked(String postid,ImageView imageView){

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Confessions").child(postid).child("Likes");

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

    private void nrLikes(String postid,TextView textView){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Confessions").child(postid).child("Likes");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                textView.setText(snapshot.getChildrenCount()+" likes");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

}
