package com.appsByHarsha.jhub;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.appsByHarsha.jhub.Adapter.ChatAdapter;
import com.appsByHarsha.jhub.Model.MessageModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;


public class DarkRoomFragment extends Fragment {

    ImageView send_btn;
    EditText message_et;


    RecyclerView recyclerView;

    ArrayList<MessageModel> list;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_dark_room, container, false);

        send_btn = view.findViewById(R.id.send);
        message_et = view.findViewById(R.id.message);

        recyclerView = view.findViewById(R.id.chat_recycler);

        list=new ArrayList<>();


        final ChatAdapter chatAdapter=new ChatAdapter(list,getContext());
        recyclerView.setAdapter(chatAdapter);

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());

        recyclerView.setLayoutManager(linearLayoutManager);

        FirebaseDatabase.getInstance().getReference().child("DarkRoom").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    MessageModel messageModel = dataSnapshot.getValue(MessageModel.class);
                    list.add(messageModel);
                }

                chatAdapter.notifyDataSetChanged();

                recyclerView.scrollToPosition(list.size() - 1);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });






        send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MessageModel messageModel = new MessageModel(FirebaseAuth.getInstance().getUid(),message_et.getText().toString()
                ,new Date().getTime());

                FirebaseDatabase.getInstance().getReference().child("DarkRoom").push().setValue(messageModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        message_et.setText("");

                    }
                });


            }
        });









        return view;
    }


}