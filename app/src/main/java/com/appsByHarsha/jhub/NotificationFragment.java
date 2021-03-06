package com.appsByHarsha.jhub;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appsByHarsha.jhub.Adapter.NotificationAdapter;
import com.appsByHarsha.jhub.Model.NotificationModel;
import com.appsByHarsha.jhub.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;


public class NotificationFragment extends Fragment {



    RecyclerView NotiRecyclerView;

    ArrayList<NotificationModel> list;


    FirebaseDatabase database;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_notification, container, false);

        database  = FirebaseDatabase.getInstance();


        NotiRecyclerView = view.findViewById(R.id.commentRv);
        list = new ArrayList<>();

        NotificationAdapter notificationAdapter = new NotificationAdapter(list,getContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        NotiRecyclerView.setLayoutManager(layoutManager);
        NotiRecyclerView.setNestedScrollingEnabled(false);
        NotiRecyclerView.setAdapter(notificationAdapter);




        database.getReference().child("Notification").child(FirebaseAuth.getInstance().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        list.clear();
                        for(DataSnapshot snapshot1: snapshot.getChildren()){
                            NotificationModel notificationModel = snapshot1.getValue(NotificationModel.class);
                            notificationModel.setNotId(snapshot1.getKey());

                            list.add(notificationModel);

                        }

                        Collections.reverse(list);
                        notificationAdapter.notifyDataSetChanged();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });








        return view;
    }
}