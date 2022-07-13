package com.appsByHarsha.jhub.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.appsByHarsha.jhub.Model.PlacementModel;
import com.appsByHarsha.jhub.Model.UserModel;
import com.appsByHarsha.jhub.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PlacementAdapter extends RecyclerView.Adapter<PlacementAdapter.viewHolder> {

    ArrayList<PlacementModel> list;
    Context context;

    public PlacementAdapter(ArrayList<PlacementModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.placement_rv_sample,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {

        PlacementModel placementModel = list.get(position);

        holder.details.setText(placementModel.getTxt());
        holder.link.setText(placementModel.getLink());


        FirebaseDatabase.getInstance().getReference().child("Users").child(placementModel.getPostedBy()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserModel userModel = snapshot.getValue(UserModel.class);
                holder.name.setText(userModel.getName());
                holder.rollNo.setText(userModel.getRollNumber());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        ArrayList<String> list = new ArrayList<>();
        list.add("18011U0403");

        FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                UserModel userModel = snapshot.getValue(UserModel.class);

                if(list.contains(userModel.getRollNumber())){
                    holder.optionsButton.setVisibility(View.VISIBLE);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        holder.optionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(context, holder.optionsButton);
                //inflating menu from xml resource
                popup.inflate(R.menu.placement_delete);
                //adding click listener

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.delete:
                                FirebaseDatabase.getInstance().getReference().child("Placements").child(placementModel.getPlacement_id()).
                                        removeValue();
                                return true;

                            default:
                                return false;
                        }
                    }
                });
                //displaying the popup
                popup.show();
            }
        });


        holder.link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Uri uri = Uri.parse(holder.link.getText().toString()); // missing 'http://' will cause crashed
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    context.startActivity(intent);
                }catch (Exception e){
                    Toast.makeText(context,e.getMessage(),Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder{

        ImageView profile,optionsButton;
        TextView name;
        TextView rollNo;
        TextView details;
        TextView link;


        public viewHolder(@NonNull View itemView) {
            super(itemView);
            profile = itemView.findViewById(R.id.usser_pp);
            optionsButton = itemView.findViewById(R.id.options_btn);
            name = itemView.findViewById(R.id.name);
            rollNo = itemView.findViewById(R.id.roll);
            details = itemView.findViewById(R.id.deta);
            link = itemView.findViewById(R.id.linkk);
        }
    }
}
