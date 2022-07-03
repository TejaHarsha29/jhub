package com.example.jhub;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jhub.Adapter.PostAdapter;
import com.example.jhub.Model.PostModel;
import com.example.jhub.Model.UserModel;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {


    FirebaseAuth auth;
    FirebaseStorage storage;
    FirebaseDatabase database;

    RecyclerView dashBoardRv;
    ArrayList<PostModel> listOfPosts;
    SwipeRefreshLayout swipeRefreshLayout;
    PostAdapter postAdapter;


    CircleImageView editImage;

    ImageView ediiit;

    ImageView settings;

    TextView name,roll;

    TextView starCount,crushCount,bffsCount,postCount,likesCount;

    TextView naamm;

    ImageView back;

    ActivityResultLauncher<String> mTakePhoto;

    long k;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        editImage = findViewById(R.id.usser_pp);

        auth =FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();

        database = FirebaseDatabase.getInstance();

        editImage.setImageResource(R.drawable.man);

        settings = findViewById(R.id.setting);

        name = findViewById(R.id.userName);

        roll = findViewById(R.id.roll);

        starCount =findViewById(R.id.star_count);

        ediiit = findViewById(R.id.edit);

        naamm = findViewById(R.id.naam);

        back = findViewById(R.id.imageView3);

        crushCount = findViewById(R.id.crush_count);

        bffsCount = findViewById(R.id.bffs_count);


        dashBoardRv = findViewById(R.id.dashBoardReV);

        postCount = findViewById(R.id.posstsno);

        likesCount=findViewById(R.id.likes_count);

        listOfPosts = new ArrayList<>();



        swipeRefreshLayout = findViewById(R.id.swipe_to_refresh);


        String uid = getIntent().getStringExtra("user_id").toString();


        postAdapter = new PostAdapter(listOfPosts,ProfileActivity.this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ProfileActivity.this);
        dashBoardRv.setLayoutManager(linearLayoutManager);
        dashBoardRv.setNestedScrollingEnabled(false);
        dashBoardRv.setHasFixedSize(false);
         dashBoardRv.setAdapter(postAdapter);

        database.getReference().child("Posts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listOfPosts.clear();
                k=0;
                for(DataSnapshot snapshot1: snapshot.getChildren()){
                    PostModel postModel = snapshot1.getValue(PostModel.class);
                    postModel.setPost_id(snapshot1.getKey());
                    if(postModel.getPostedBy().equals(uid)){
                        listOfPosts.add(postModel);

                        k=k+postModel.getPostLike();


                    }
                }



                likesCount.setText(k+" likes");

                postCount.setText(listOfPosts.size()+" Posts");



                Collections.reverse(listOfPosts);

                postAdapter.notifyDataSetChanged();




            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });






        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                postAdapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }
        });









        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);

        reference.child("Stars").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                starCount.setText(snapshot.getChildrenCount()+" Stars");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        reference.child("BestFriends").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                bffsCount.setText(snapshot.getChildrenCount()+" Bffs");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        reference.child("Crush").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                crushCount.setText(snapshot.getChildrenCount()+" Crushes");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProfileActivity.this.finish();
            }
        });




        if(!uid.equals(FirebaseAuth.getInstance().getUid())){
            ediiit.setVisibility(View.GONE);
            editImage.setEnabled(false);
            settings.setVisibility(View.GONE);
        }



        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                Intent intent = new Intent(ProfileActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();

            }
        });


        database.getReference().child("Users").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    UserModel user = snapshot.getValue(UserModel.class);

                    Picasso.get().load(user.getProfile_photo()).placeholder(R.drawable.man).into(editImage);
                    name.setText(user.getName());
                    roll.setText(user.getRollNumber());


                    naamm.setText(user.getName());


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




        editImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.with(ProfileActivity.this)
                        .crop()		//Crop image(Optional), Check Customization for more option
                        .compress(1024)			//Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        final StorageReference reference = storage.getReference().child("profile").child(auth.getCurrentUser().getUid());


        try{
            reference.putFile(data.getData()).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    editImage.setImageURI(data.getData());
                    reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            database.getReference().child("Users").child(auth.getCurrentUser().getUid()).
                                    child("profile_photo").setValue(uri.toString());
                        }
                    });
                }
            });

        }catch(Exception e){
            Toast.makeText(ProfileActivity.this, "Please select atleast one image", Toast.LENGTH_SHORT).show();
        }




    }
}