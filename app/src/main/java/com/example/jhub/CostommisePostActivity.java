package com.example.jhub;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jhub.Model.PostModel;
import com.example.jhub.Model.UserModel;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.Date;

public class CostommisePostActivity extends AppCompatActivity {

    EditText post_description;

    Button post_button;

    ImageView addImage,postImage;

    ActivityResultLauncher<String> mTakePhoto;

    FirebaseStorage storage;

    Uri uri;

    TextView userrrname,roll;

    ImageView back;



    ProgressDialog progressDialog;


    FirebaseDatabase database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_costommise_post);

        post_description = findViewById(R.id.post_description);
        post_button = findViewById(R.id.post_btn);
        addImage = findViewById(R.id.add_image);
        postImage = findViewById(R.id.post_image_edit);

        storage = FirebaseStorage.getInstance();
        database =FirebaseDatabase.getInstance();

        userrrname = findViewById(R.id.name);
        roll = findViewById(R.id.roll);

        back = findViewById(R.id.imageView2);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        UserModel userModel = snapshot.getValue(UserModel.class);
                        userrrname.setText(userModel.getName());
                        roll.setText(userModel.getRollNumber());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        post_description.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String description = post_description.getText().toString();
                if(!description.isEmpty()){
                    post_button.setBackground(ContextCompat.getDrawable(CostommisePostActivity.this,R.drawable.post_bg_highlighted));
                    post_button.setTextColor(Color.parseColor("#000000"));
                    post_button.setEnabled(true);
                }
                else{
                    post_button.setBackground(ContextCompat.getDrawable(CostommisePostActivity.this,R.drawable.post_bg));
                    post_button.setTextColor(Color.parseColor("#4D000000"));
                    post_button.setEnabled(false);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });






        postImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ImagePicker.with(CostommisePostActivity.this)
                        .crop()	    			//Crop image(Optional), Check Customization for more option
                        .compress(1024)			//Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                        .start();

            }
        });


        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ImagePicker.with(CostommisePostActivity.this)
                        .crop()	    			//Crop image(Optional), Check Customization for more option
                        .compress(1024)			//Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                        .start();

            }
        });

        post_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(uri==null){
                    Toast.makeText(CostommisePostActivity.this, "Please select images to proceed", Toast.LENGTH_SHORT).show();
                }
                else{
                    progressDialog = new ProgressDialog(CostommisePostActivity.this);


                    progressDialog.setMessage("Posting...");
                    progressDialog.show();


                    progressDialog.setCancelable(false);




                    final StorageReference reference = storage.getReference().child("Posts").child(new Date().getTime()+"");
                    reference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    PostModel postModel = new PostModel();
                                    postModel.setPost_image(uri.toString());
                                    postModel.setPostedBy(FirebaseAuth.getInstance().getUid());
                                    postModel.setDescription(post_description.getText().toString());
                                    postModel.setPostedAt(new Date().getTime());

                                    database.getReference().child("Posts").push().setValue(postModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(CostommisePostActivity.this, "posted Successfully", Toast.LENGTH_SHORT).show();

                                            Intent intent = new Intent(CostommisePostActivity.this,MainActivity.class);
                                            startActivity(intent);

                                            progressDialog.dismiss();
                                            finish();
                                        }

                                    });



                                }
                            });
                        }
                    });
                }



            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        uri = data.getData();



        postImage.setVisibility(View.VISIBLE);
        postImage.setImageURI(uri);
        post_button.setBackground(ContextCompat.getDrawable(CostommisePostActivity.this,R.drawable.post_bg_highlighted));
        post_button.setTextColor(Color.parseColor("#000000"));
        post_button.setEnabled(true);
    }
}