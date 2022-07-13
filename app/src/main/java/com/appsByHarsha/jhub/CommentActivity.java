package com.appsByHarsha.jhub;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.appsByHarsha.jhub.Model.PostModel;
import com.appsByHarsha.jhub.Adapter.CommentAdapter;
import com.appsByHarsha.jhub.Model.CommentModel;
import com.appsByHarsha.jhub.Model.NotificationModel;
import com.appsByHarsha.jhub.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;

public class CommentActivity extends AppCompatActivity {

    RecyclerView commentRecyclerView;
    EditText commentEditText;
    ImageView commentSendBtn;

    ImageView imageView;


    FirebaseDatabase database;
    FirebaseAuth auth;

    ArrayList<CommentModel> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);


        String postId = getIntent().getStringExtra("postId").toString();

        commentRecyclerView = findViewById(R.id.commentRv);
        commentEditText=findViewById(R.id.commentEt);
        commentSendBtn=findViewById(R.id.send_coment_btn);

        auth = FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();


        list = new ArrayList<>();

        imageView = findViewById(R.id.imageView3);





        commentSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(commentEditText.getText().toString().equals("")){

                }
                else{
                    CommentModel commentModel = new CommentModel();
                    commentModel.setCommentBody(commentEditText.getText().toString());
                    commentModel.setCommentedAt(new Date().getTime());
                    commentModel.setCommentedBy(auth.getUid());
                    database.getReference().child("Posts").child(postId).child("comments").push()
                            .setValue(commentModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            commentEditText.setText("");

                            NotificationModel notificationModel = new NotificationModel();
                            notificationModel.setNotificationBy(FirebaseAuth.getInstance().getUid());
                            notificationModel.setNotificationAt(new Date().getTime());
                            notificationModel.setPostId(postId);
                            notificationModel.setType("Comment");
                            notificationModel.setCheckOpen(false);
                            FirebaseDatabase.getInstance().getReference().child("Posts").child(postId).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    PostModel postModel =snapshot.getValue(PostModel.class);
                                    FirebaseDatabase.getInstance().getReference().child("Notification").child(postModel.getPostedBy()).push()
                                            .setValue(notificationModel);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });




                        }
                    });
                }
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommentActivity.this.finish();
            }
        });


        CommentAdapter adapter = new CommentAdapter(list,CommentActivity.this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(CommentActivity.this);
        commentRecyclerView.setLayoutManager(layoutManager);
        commentRecyclerView.setAdapter(adapter);


        database.getReference().child("Posts").child(postId).child("comments").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                list.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){

                    try {
                        CommentModel commentModel = dataSnapshot.getValue(CommentModel.class);
                        list.add(commentModel);
                    }catch (Exception e){
                        Toast.makeText(CommentActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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