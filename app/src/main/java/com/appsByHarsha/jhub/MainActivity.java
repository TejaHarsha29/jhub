package com.appsByHarsha.jhub;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.appsByHarsha.jhub.Model.UserModel;
import com.appsByHarsha.jhub.Model.NotificationModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    ChipNavigationBar chipNavigationBar;
    CircleImageView profile;

    UserModel user;

    ImageView confessions;

    int count;

    ImageView leaderBoard;

    ImageView imageView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();


        confessions = findViewById(R.id.cupid);
        leaderBoard = findViewById(R.id.imageView6);


        leaderBoard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,LeaderBoardActivity.class);
                startActivity(intent);
                Toast.makeText(MainActivity.this, "Leaderboards will be available soon", Toast.LENGTH_SHORT).show();
            }
        });





        confessions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(MainActivity.this,ConfessionsActivity.class);
                startActivity(intent);
            }
        });

















        profile =findViewById(R.id.top_user);



        chipNavigationBar = findViewById(R.id.chipnav);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new HomeFragment()).commit();
        chipNavigationBar.setItemEnabled(R.id.home,true);
        chipNavigationBar.setItemSelected(R.id.home,true);
        bottomMenu();



        try {
            FirebaseDatabase.getInstance().getReference().child("Notification").child(FirebaseAuth.getInstance().getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    count=0;
                    for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                        NotificationModel notificationModel = dataSnapshot.getValue(NotificationModel.class);
                        if(!notificationModel.isCheckOpen())
                        {

                            count = count+1;

                        }
                        else{
                            chipNavigationBar.dismissBadge(R.id.notificate);
                        }
                    }
                    if(count ==0){
                        chipNavigationBar.dismissBadge(R.id.notificate);

                    }
                    else{
                        chipNavigationBar.showBadge(R.id.notificate,count);

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    user = snapshot.getValue(UserModel.class);


                    try {
                        Picasso.get().load(user.getProfile_photo()).placeholder(R.drawable.man).into(profile);

                    }catch (Exception e){

                    }






                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            profile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this,ProfileActivity.class);
                    intent.putExtra("user_id",FirebaseAuth.getInstance().getUid());
                    startActivity(intent);
                }
            });
        }catch (Exception e){
            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }







    }

    private void bottomMenu() {
        chipNavigationBar.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {
                Fragment fragment=null;
                switch (i){
                    case R.id.home:
                        fragment=new HomeFragment();
                        break;
                    case R.id.notificate:
                        fragment=new NotificationFragment();
                        break;
                    case R.id.job:
                        fragment=new PlacementFragment();
                        break;

                    case R.id.search:
                        fragment = new SearchFragment();
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).commit();
            }
        });
    }



    @Override
    public void onStart() {
        super.onStart();
        if(FirebaseAuth.getInstance().getCurrentUser()==null){
            Intent intent = new Intent(MainActivity.this,RegActivity.class);
            startActivity(intent);
            finish();

        }
    }
    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please press BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

}