package com.appsByHarsha.jhub;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.appsByHarsha.jhub.Model.UserModel;
import com.appsByHarsha.jhub.Model.UserModel;
import com.appsByHarsha.jhub.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RegActivity extends AppCompatActivity {

    TextView termsAndCond;

    Button getOtp_btn;
    EditText phoneNumber_et,userName_et,rollNumber_et;
    FirebaseAuth firebaseAuth;
    FirebaseUser currentUser;


    TextView navToLogin;

    ProgressDialog progressDialog;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);

        termsAndCond = findViewById(R.id.termsAndConditions);

        getOtp_btn = findViewById(R.id.getOtpBtn);
        userName_et = findViewById(R.id.user_name);
        rollNumber_et = findViewById(R.id.roll_no);
        phoneNumber_et=findViewById(R.id.phone);

        firebaseAuth=FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();

        navToLogin = findViewById(R.id.nav_to_login);

        navToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });


        termsAndCond.setText("By clicking Register,you are accepting the terms of use,privacy policy of the app");

        SpannableString ss =new SpannableString(termsAndCond.getText().toString());

        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                try {
                    Uri uri = Uri.parse("https://pages.flycricket.io/jhub/privacy.html"); // missing 'http://' will cause crashed
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }catch (Exception e){
                    Toast.makeText(RegActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        };

        ss.setSpan(clickableSpan,42,70, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        termsAndCond.setText(ss);
        termsAndCond.setMovementMethod(LinkMovementMethod.getInstance());


        getOtp_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = new ProgressDialog(RegActivity.this);


                progressDialog.setMessage("Signing in...");
                progressDialog.show();


                progressDialog.setCancelable(false);


                FirebaseDatabase.getInstance().getReference().child("Users").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        Boolean isAlreadyUser = false;
                        Boolean isAlreadyRoll = false;
                        Boolean isAlreadyPhone = false;
                        for(DataSnapshot snapshot1:snapshot.getChildren()){
                            UserModel userModel = snapshot1.getValue(UserModel.class);
                            if(userModel.getName().equals(userName_et.getText().toString())){
                                isAlreadyUser = true;
                                break;
                            }
                            else if(userModel.getRollNumber().equals(rollNumber_et.getText().toString())){
                                isAlreadyRoll = true;
                                break;
                            }
                            else if(userModel.getPhoneNumber().equals("+91"+phoneNumber_et.getText().toString())){
                                isAlreadyPhone = true;
                                break;
                            }
                        }

                        if(isAlreadyUser){

                            userName_et.setSelectAllOnFocus(true);
                            progressDialog.dismiss();


                            userName_et.setError("Username Already Registered");



                        }
                        else if(isAlreadyRoll){
                            rollNumber_et.setText("");

                            progressDialog.dismiss();
                            rollNumber_et.setError("Roll Number Already Registered");



                        }
                        else if(isAlreadyPhone){
                            progressDialog.dismiss();
                            phoneNumber_et.setText("");
                            phoneNumber_et.setError("Phone Number Already Registered");


                        }
                        else if(userName_et.getText().toString().isEmpty()){
                            progressDialog.dismiss();
                            Toast.makeText(RegActivity.this, "UserName cannot be empty", Toast.LENGTH_SHORT).show();
                        }
                        else if(rollNumber_et.getText().toString().isEmpty()){
                            progressDialog.dismiss();

                            Toast.makeText(RegActivity.this, "RollNumber cannot be empty", Toast.LENGTH_SHORT).show();
                        }

                        else if(userName_et.getText().toString().length()<5){
                            progressDialog.dismiss();

                            Toast.makeText(RegActivity.this, "UserName must be more than 4 characters", Toast.LENGTH_SHORT).show();
                        }
                        else if(!rollNumber_et.getText().toString().substring(2,4).equals("01")){
                            progressDialog.dismiss();

                            Toast.makeText(RegActivity.this, "invalid rollNumber", Toast.LENGTH_SHORT).show();
                        }

                        else{
                            Intent intent = new Intent(RegActivity.this,OtpActivity.class);
                            intent.putExtra("mobile", "+91"+phoneNumber_et.getText().toString().trim());
                            intent.putExtra("userName",String.valueOf(userName_et.getText()));
                            intent.putExtra("RollNumber",String.valueOf(rollNumber_et.getText()));
                            progressDialog.dismiss();


                            startActivity(intent);
                            finish();
                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        progressDialog.dismiss();


                    }
                });








            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(currentUser!=null){
            Intent intent = new Intent(RegActivity.this,MainActivity.class);
            startActivity(intent);
            finish();
        }

    }
}