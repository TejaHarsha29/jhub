package com.example.jhub;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.jhub.Adapter.CommentAdapter;
import com.example.jhub.Adapter.LikesAdapter;
import com.example.jhub.Model.CommentModel;
import com.example.jhub.Model.LikesModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class LikesActivity extends AppCompatActivity {

    RecyclerView commentRecyclerView;



    FirebaseDatabase database;
    FirebaseAuth auth;

    ArrayList<LikesModel> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_likes);


        String postId = getIntent().getStringExtra("postId").toString();

        commentRecyclerView = findViewById(R.id.commentRv);



        auth = FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();


        list = new ArrayList<>();

        LikesAdapter adapter = new LikesAdapter(list,LikesActivity.this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(LikesActivity.this);
        commentRecyclerView.setLayoutManager(layoutManager);
        commentRecyclerView.setAdapter(adapter);

        database.getReference().child("Posts").child(postId).child("Likes").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                list.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){

                    try {
                        LikesModel likesModel = dataSnapshot.getValue(LikesModel.class);
                        list.add(likesModel);
                    }catch (Exception e){
                        Toast.makeText(LikesActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }


                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}