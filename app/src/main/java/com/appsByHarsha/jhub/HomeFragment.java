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

import com.appsByHarsha.jhub.Model.PostModel;
import com.appsByHarsha.jhub.Adapter.PostAdapter;
import com.appsByHarsha.jhub.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;


public class HomeFragment extends Fragment {


    RecyclerView dashBoardRv;

    ArrayList<PostModel> listOfPosts;

    ImageView postt;

    FirebaseDatabase database;

    FirebaseAuth auth;

    SwipeRefreshLayout swipeRefreshLayout;
    PostAdapter postAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        dashBoardRv = view.findViewById(R.id.dashBoardReV);


        postt = view.findViewById(R.id.post);


        postt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),CostommisePostActivity.class);
                startActivity(intent);
            }
        });


        listOfPosts = new ArrayList<>();

        database = FirebaseDatabase.getInstance();

        auth = FirebaseAuth.getInstance();

        swipeRefreshLayout = view.findViewById(R.id.swipe_to_refresh);








        postAdapter = new PostAdapter(listOfPosts,getContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        dashBoardRv.setLayoutManager(linearLayoutManager);
        dashBoardRv.setNestedScrollingEnabled(false);

        dashBoardRv.setAdapter(postAdapter);





        database.getReference().child("Posts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listOfPosts.clear();
                for(DataSnapshot snapshot1: snapshot.getChildren()){
                    PostModel postModel = snapshot1.getValue(PostModel.class);
                    postModel.setPost_id(snapshot1.getKey());
                    listOfPosts.add(postModel);

                }




                Collections.reverse(listOfPosts);

                postAdapter.notifyDataSetChanged();






            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                postAdapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }
        });










        return view;
    }

}