package com.example.jhub;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

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
                Intent intent = new Intent(RegActivity.this,OtpActivity.class);
                intent.putExtra("mobile", "+91"+phoneNumber_et.getText().toString().trim());
                intent.putExtra("userName",String.valueOf(userName_et.getText()));
                intent.putExtra("RollNumber",String.valueOf(rollNumber_et.getText()));

                startActivity(intent);
                finish();


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