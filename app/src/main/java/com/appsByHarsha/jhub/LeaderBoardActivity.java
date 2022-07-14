package com.appsByHarsha.jhub;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.appsByHarsha.jhub.Adapter.UserAdapter;
import com.appsByHarsha.jhub.Model.UserModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;

import de.hdodenhof.circleimageview.CircleImageView;

public class LeaderBoardActivity extends AppCompatActivity  implements
        AdapterView.OnItemSelectedListener{




    Spinner spinner;

    String output="Star";

    String[] type={"Star","Crush","BFF"};

    Fragment fragment;

    TextView title;

    ImageView imageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leader_board);






        spinner = findViewById(R.id.coursesspinner);
        spinner.setOnItemSelectedListener(LeaderBoardActivity.this);

        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,type);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spinner.setAdapter(aa);

        title = findViewById(R.id.naam);

        imageView = findViewById(R.id.imageView3);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LeaderBoardActivity.this.finish();
            }
        });






        getSupportFragmentManager().beginTransaction().replace(R.id.container,new StarFragment()).commit();







    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        fragment = null;
        if(type[position]=="Star"){

            title.setText("Star of JNTUH");

            fragment = new StarFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.container,fragment).commit();



        }
        else if(type[position]=="BFF"){
            title.setText("BFF of JNTUH");
            fragment = new BffsFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.container,fragment).commit();




        }
        else if(type[position]=="Crush"){
            title.setText("Crush of JNTUH");
            fragment = new CrushFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.container,fragment).commit();


        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }



}