package com.appsByHarsha.jhub;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.appsByHarsha.jhub.Model.UserModel;
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
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileActivity extends AppCompatActivity {

    CircleImageView editImage;

    ImageView ediiit;

    Uri uri;

    EditText user_et;

    Button updateDetails;

    FirebaseAuth auth;
    FirebaseStorage storage;
    FirebaseDatabase database;

    ProgressDialog progressDialog;

    ImageView back;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);


        editImage = findViewById(R.id.top_user);
        ediiit = findViewById(R.id.edit);

        user_et = findViewById(R.id.user_name);

        updateDetails = findViewById(R.id.update_details);

        storage = FirebaseStorage.getInstance();
        database =FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();

        back = findViewById(R.id.imageView3);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditProfileActivity.this.finish();
            }
        });



        FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        UserModel userModel = snapshot.getValue(UserModel.class);
                        user_et.setText(userModel.getName());
                        Picasso.get().load(userModel.getProfile_photo()).placeholder(R.drawable.placeholder).into(editImage);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


        updateDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog = new ProgressDialog(EditProfileActivity.this);


                progressDialog.setMessage("Updating...");
                progressDialog.show();


                progressDialog.setCancelable(false);


                if(uri==null){

                    database.getReference().child("Users").child(auth.getCurrentUser().getUid()).
                            child("name").setValue(user_et.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            progressDialog.dismiss();
                            EditProfileActivity.this.finish();

                        }
                    });
                }
                else {
                    final StorageReference reference = storage.getReference().child("profile").child(auth.getCurrentUser().getUid());



                    try{
                        reference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                editImage.setImageURI(uri);
                                reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {

                                        database.getReference().child("Users").child(auth.getCurrentUser().getUid()).
                                                child("profile_photo").setValue(uri.toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                progressDialog.dismiss();
                                            }
                                        });

                                        database.getReference().child("Users").child(auth.getCurrentUser().getUid()).
                                                child("name").setValue(user_et.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                progressDialog.dismiss();
                                                EditProfileActivity.this.finish();
                                            }
                                        });

                                    }
                                });
                            }
                        });

                    }catch(Exception e){
                        Toast.makeText(EditProfileActivity.this, "Please select atleast one image", Toast.LENGTH_SHORT).show();
                    }



                }


            }
        });





        editImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.with(EditProfileActivity.this)
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

        uri = data.getData();
        editImage.setImageURI(uri);





    }

}