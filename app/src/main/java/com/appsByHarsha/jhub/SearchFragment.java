package com.appsByHarsha.jhub;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.appsByHarsha.jhub.Adapter.UserAdapter;
import com.appsByHarsha.jhub.Model.UserModel;
import com.appsByHarsha.jhub.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;


public class SearchFragment extends Fragment {

    FirebaseAuth auth;
    FirebaseDatabase database;
    ArrayList<UserModel> list;

    RecyclerView recyclerView;

    SearchView searchView;

    UserAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        auth =FirebaseAuth.getInstance();
        database =FirebaseDatabase.getInstance();
        list = new ArrayList<UserModel>();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view =  inflater.inflate(R.layout.fragment_search, container, false);
        recyclerView = view.findViewById(R.id.usersRV);

        adapter = new UserAdapter(getContext(),list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());

        recyclerView.setAdapter(adapter);

        recyclerView.setLayoutManager(linearLayoutManager);

        searchView = view.findViewById(R.id.searchView);

        searchView.clearFocus();





        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                filterList(newText);

                return true;
            }
        });




        database.getReference().child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    UserModel user = dataSnapshot.getValue(UserModel.class);
                    user.setUser_id(dataSnapshot.getKey());
                    if(dataSnapshot.getKey().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){

                    }
                    else {
                        list.add(user);
                    }
                }

                Collections.sort(list,UserModel.userStar);
                adapter.notifyDataSetChanged();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });







        return view;
    }

    private void filterList(String text) {
        ArrayList<UserModel> filetredList = new ArrayList<>();

        for(UserModel model : list){
            if(model.getName().toLowerCase(Locale.ROOT).contains(text.toLowerCase(Locale.ROOT)) || model.getRollNumber().toLowerCase(Locale.ROOT).contains(text.toLowerCase(Locale.ROOT))){
                filetredList.add(model);
            }
        }

        if(filetredList.isEmpty()){
            Toast.makeText(getContext(), "No users", Toast.LENGTH_SHORT).show();
        }
        else{
            adapter.setFilterlistt(filetredList);
        }

    }
}