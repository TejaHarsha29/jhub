package com.appsByHarsha.jhub;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.appsByHarsha.jhub.Model.PlacementModel;
import com.appsByHarsha.jhub.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class CustomisePlacementActivity extends AppCompatActivity {

    Button postBtn;
    EditText companyName,eligibility,packageee,link,lastDate,batchh;

    FirebaseDatabase firebaseDatabase;

    ProgressDialog progressDialog;

    ImageView back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customise_placement);

        postBtn = findViewById(R.id.post_btn);

        companyName =findViewById(R.id.user_name);
        eligibility =findViewById(R.id.eligibility);
        packageee =findViewById(R.id.packag);
        link =findViewById(R.id.linkee);
        lastDate =findViewById(R.id.last_date);
        batchh = findViewById(R.id.batch);

        back = findViewById(R.id.imageView2);




        firebaseDatabase = FirebaseDatabase.getInstance();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomisePlacementActivity.this.finish();
            }
        });




        postBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog = new ProgressDialog(CustomisePlacementActivity.this);


                progressDialog.setMessage("Posting...");
                progressDialog.show();


                progressDialog.setCancelable(false);


                String txt = companyName.getText().toString()+" is hiring,Batch : "+batchh.getText().toString()+",Salary : "
                        +packageee.getText().toString()+" Qualification : "+eligibility.getText().toString()+" last Date "+
                        lastDate.getText().toString();



                PlacementModel placementModel = new PlacementModel(txt,link.getText().toString());
                placementModel.setPostedBy(FirebaseAuth.getInstance().getUid());

                firebaseDatabase.getReference().child("Placements").push().setValue(placementModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        CustomisePlacementActivity.this.finish();
                        progressDialog.dismiss();


                    }
                });





            }
        });


    }
}