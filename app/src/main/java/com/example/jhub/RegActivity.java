package com.example.jhub;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jhub.Model.UserModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RegActivity extends AppCompatActivity {

    Button getOtp_btn;
    EditText phoneNumber_et,userName_et,rollNumber_et;
    FirebaseAuth firebaseAuth;
    FirebaseUser currentUser;


    TextView navToLogin;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);

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


        getOtp_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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


                            userName_et.setError("Username Already Registered");



                        }
                        else if(isAlreadyRoll){
                            rollNumber_et.setText("");

                            rollNumber_et.setError("Roll Number Already Registered");



                        }
                        else if(isAlreadyPhone){
                            phoneNumber_et.setText("");
                            phoneNumber_et.setError("Phone Number Already Registered");


                        }

                        else{
                            Intent intent = new Intent(RegActivity.this,OtpActivity.class);
                            intent.putExtra("mobile", "+91"+phoneNumber_et.getText().toString().trim());
                            intent.putExtra("userName",String.valueOf(userName_et.getText()));
                            intent.putExtra("RollNumber",String.valueOf(rollNumber_et.getText()));

                            startActivity(intent);
                            finish();
                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

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