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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    TextView navToRegister;

    Button getOtp_btn;
    EditText phoneNumber_et;
    FirebaseAuth firebaseAuth;
    FirebaseUser currentUser;

    DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        navToRegister = findViewById(R.id.nav_to_register);

        getOtp_btn = findViewById(R.id.getOtpBtn);
        phoneNumber_et = findViewById(R.id.phone);

        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();


        navToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegActivity.class);
                startActivity(intent);
            }
        });

        getOtp_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                ref = FirebaseDatabase.getInstance().getReference().child("Users");
                ref.addValueEventListener(eventistener);


            }
        });
    }

    ValueEventListener eventistener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            Boolean is_user = false;
            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                UserModel userModel = dataSnapshot.getValue(UserModel.class);
                if (userModel.getPhoneNumber().equals("+91" + phoneNumber_et.getText().toString())) {

                    is_user = true;
                    break;


                }


            }

            if (is_user) {
                Intent intent = new Intent(LoginActivity.this, OtpfromLoginActivity.class);
                intent.putExtra("mobile", "+91" + phoneNumber_et.getText().toString().trim());

                ref.removeEventListener(eventistener);
                startActivity(intent);
                finish();
            } else {
                phoneNumber_et.setError("PhoneNumber Not Registered");
            }
        }



        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };


}