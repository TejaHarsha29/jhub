package com.example.jhub;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.chaos.view.PinView;
import com.example.jhub.Model.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

public class OtpfromLoginActivity extends AppCompatActivity {

    PinView otp_et;


    String phoneNumber;

    TextView num_text;

    private FirebaseAuth mAuth;


    String otpId;

    Button verify;

    FirebaseDatabase database;

    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpfrom_login);

        otp_et = findViewById(R.id.otp_entry);



        num_text=findViewById(R.id.txt_num);
        verify = findViewById(R.id.button);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        phoneNumber=getIntent().getStringExtra("mobile").toString();


        num_text.setText("Enter the code sent to " + phoneNumber);

        initiateOtp();



        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(otp_et.getText().toString().isEmpty()){

                }
                else if(otp_et.getText().toString().length()!=6){

                }
                else{
                    progressDialog = new ProgressDialog(OtpfromLoginActivity.this);


                    progressDialog.setMessage("logging in...");
                    progressDialog.show();


                    progressDialog.setCancelable(false);

                    PhoneAuthCredential credential= PhoneAuthProvider.getCredential(otpId,otp_et.getText().toString());
                    signInWithPhoneAuthCredential(credential);
                }
            }
        });
    }
    private void initiateOtp() {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumber)       // Phone number to verify
                        .setTimeout(120L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                super.onCodeSent(s, forceResendingToken);
                                otpId=s;

                            }

                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                                signInWithPhoneAuthCredential(phoneAuthCredential);

                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {

                                Toast.makeText(OtpfromLoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();


                            }
                        })          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);



    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = task.getResult().getUser();


                            Intent intent=new Intent(OtpfromLoginActivity.this,MainActivity.class);
                            intent.putExtra("phonenummber",phoneNumber);
                            progressDialog.dismiss();

                            startActivity(intent);
                            finish();




                            // Update UI
                        }
                        else {

                            Toast.makeText(OtpfromLoginActivity.this, "failed", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }
}