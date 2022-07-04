package com.example.jhub;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.jhub.Model.ConfessionModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;

public class CreateConfessionActivity extends AppCompatActivity {

    EditText confession_txt;

    Button confess_btn;

    FirebaseDatabase database;

    ImageView back;

    ProgressDialog progressDialog;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_confession);

        confession_txt =findViewById(R.id.confess_txt);

        confess_btn = findViewById(R.id.confess_btn);

        database= FirebaseDatabase.getInstance();

        back = findViewById(R.id.imageView2);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateConfessionActivity.this.finish();
            }
        });

        confession_txt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String description = confession_txt.getText().toString();
                if(!description.isEmpty()){
                    confess_btn.setBackground(ContextCompat.getDrawable(CreateConfessionActivity.this,R.drawable.post_bg_highlighted));
                    confess_btn.setTextColor(Color.parseColor("#000000"));
                    confess_btn.setEnabled(true);
                }
                else{
                    confess_btn.setBackground(ContextCompat.getDrawable(CreateConfessionActivity.this,R.drawable.post_bg));
                    confess_btn.setTextColor(Color.parseColor("#4D000000"));
                    confess_btn.setEnabled(false);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });


        confess_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog = new ProgressDialog(CreateConfessionActivity.this);


                progressDialog.setMessage("Posting...");
                progressDialog.show();


                progressDialog.setCancelable(false);


                ConfessionModel confessionModel = new ConfessionModel();
                confessionModel.setConfessionText(confession_txt.getText().toString());
                confessionModel.setPostedAt(new Date().getTime());
                database.getReference().child("Confessions").push().setValue(confessionModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        progressDialog.dismiss();
                        CreateConfessionActivity.this.finish();
                    }
                });
            }
        });


    }
}