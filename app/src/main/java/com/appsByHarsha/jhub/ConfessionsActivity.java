package com.appsByHarsha.jhub;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.appsByHarsha.jhub.Adapter.ConfessionAdapter;
import com.appsByHarsha.jhub.Model.ConfessionModel;
import com.appsByHarsha.jhub.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class  ConfessionsActivity extends AppCompatActivity {

    ArrayList<ConfessionModel> list =new ArrayList<>();
    ConfessionAdapter adapter;

    ViewPager2 viewPager2;

    ImageView post_confession_btn;

    FirebaseDatabase database;

    ImageView back;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confessions);

        post_confession_btn = findViewById(R.id.add_confession);

        database =FirebaseDatabase.getInstance();

        back = findViewById(R.id.imageView2);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConfessionsActivity.this.finish();
            }
        });


        post_confession_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ConfessionsActivity.this,CreateConfessionActivity.class);
                startActivity(intent);
            }
        });

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.pink));
        }

        viewPager2 = findViewById(R.id.confession_vp);




        adapter = new ConfessionAdapter(ConfessionsActivity.this,list);

        viewPager2.setAdapter(adapter);

        database.getReference().child("Confessions").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    ConfessionModel confessionModel = dataSnapshot.getValue(ConfessionModel.class);
                    confessionModel.setConfession_id(dataSnapshot.getKey());

                    if(confessionModel.getReportCount()<10){
                        list.add(confessionModel);

                    }



                }
                Collections.reverse(list);
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




    }
}