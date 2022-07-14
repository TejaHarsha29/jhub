package com.appsByHarsha.jhub;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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


public class CrushFragment extends Fragment {
    CircleImageView top_profile;
    TextView top_user_details;

    FirebaseAuth auth;
    FirebaseDatabase database;
    ArrayList<UserModel> list;

    RecyclerView recyclerView;
    UserAdapter adapter;

    TextView roll;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_crush, container, false);

        top_profile = view.findViewById(R.id.top_user);
        top_user_details = view.findViewById(R.id.top_details);
        auth =FirebaseAuth.getInstance();
        database =FirebaseDatabase.getInstance();
        list = new ArrayList<UserModel>();

        recyclerView = view.findViewById(R.id.rv_users);

        roll = view.findViewById(R.id.top_details2);



        adapter = new UserAdapter(getContext(),list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());

        recyclerView.setAdapter(adapter);

        recyclerView.setLayoutManager(linearLayoutManager);


        database.getReference().child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    UserModel user = dataSnapshot.getValue(UserModel.class);
                    user.setUser_id(dataSnapshot.getKey());

                    list.add(user);

                }

                Collections.sort(list,UserModel.userCrush);

                Picasso.get().load(list.get(0).getProfile_photo()).placeholder(R.drawable.man).into(top_profile);

                top_user_details.setText(list.get(0).getName());

                roll.setText(list.get(0).getRollNumber());


                adapter.notifyDataSetChanged();








            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });







        return view;
    }
}