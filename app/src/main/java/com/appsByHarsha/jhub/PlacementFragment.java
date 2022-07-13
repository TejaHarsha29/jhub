package com.appsByHarsha.jhub;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.appsByHarsha.jhub.Model.UserModel;
import com.appsByHarsha.jhub.Adapter.PlacementAdapter;
import com.appsByHarsha.jhub.Model.PlacementModel;
import com.appsByHarsha.jhub.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;


public class PlacementFragment extends Fragment {

    RecyclerView placementsRv;

    ImageView postt;

    FirebaseDatabase database;

    FirebaseAuth auth;

    ArrayList<PlacementModel> listOfPosts;


    SwipeRefreshLayout swipeRefreshLayout;

    UserModel userModel;

    ImageView plus,postShadow;

    TextView addPost;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_placement, container, false);

        postt = view.findViewById(R.id.post);
        plus = view.findViewById(R.id.plus);
        addPost = view.findViewById(R.id.addP);
        postShadow = view.findViewById(R.id.post_shafow);




        ArrayList<String> list = new ArrayList<>();
        list.add("18011U0403");
        list.add("18011P0412");
        list.add("19011A0220");
        list.add("18011U0109");
        list.add("19011A0446");
        list.add("18011P0512");




        FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userModel = snapshot.getValue(UserModel.class);
                if(list.contains(userModel.getRollNumber())){
                    postt.setVisibility(View.VISIBLE);
                    plus.setVisibility(View.VISIBLE);
                    addPost.setVisibility(View.VISIBLE);
                    postShadow.setVisibility(View.VISIBLE);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });









        postt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),CustomisePlacementActivity.class);
                startActivity(intent);
            }
        });

        placementsRv = view.findViewById(R.id.dashBoardReV);

        listOfPosts = new ArrayList<>();


        database = FirebaseDatabase.getInstance();

        auth = FirebaseAuth.getInstance();

        swipeRefreshLayout = view.findViewById(R.id.swipe_to_refresh);

        PlacementAdapter placementAdapter = new PlacementAdapter(listOfPosts,getContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        placementsRv.setLayoutManager(linearLayoutManager);
        placementsRv.setNestedScrollingEnabled(false);
        placementsRv.setAdapter(placementAdapter);

        database.getReference().child("Placements").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listOfPosts.clear();

                for(DataSnapshot snapshot1:snapshot.getChildren()){
                    PlacementModel placementModel = snapshot1.getValue(PlacementModel.class);
                    placementModel.setPlacement_id(snapshot1.getKey());

                    listOfPosts.add(placementModel);



                }

                Collections.reverse(listOfPosts);
                placementAdapter.notifyDataSetChanged();






            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                placementAdapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }
        });





















        return view;
    }
}