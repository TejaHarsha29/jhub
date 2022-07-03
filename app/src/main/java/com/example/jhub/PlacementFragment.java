package com.example.jhub;

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

import com.example.jhub.Adapter.PlacementAdapter;
import com.example.jhub.Adapter.PostAdapter;
import com.example.jhub.Model.PlacementModel;
import com.example.jhub.Model.PostModel;
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



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_placement, container, false);

        postt = view.findViewById(R.id.post);

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