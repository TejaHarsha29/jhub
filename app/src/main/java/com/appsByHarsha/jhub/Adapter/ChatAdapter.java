package com.appsByHarsha.jhub.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.appsByHarsha.jhub.Model.MessageModel;
import com.appsByHarsha.jhub.R;
import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter{

    ArrayList<MessageModel> messagesModels;
    Context context;

    int SENDER_VIEW_TYPE=1;
    int RECEIVER_VIEW_TYPE=2;

    public ChatAdapter(ArrayList<MessageModel> messagesModels, Context context) {
        this.messagesModels = messagesModels;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if(viewType==SENDER_VIEW_TYPE){
            View view= LayoutInflater.from(context).inflate(R.layout.sample_sender,parent,false);
            return new SenderViewHolder(view);
        }
        else{
            View view= LayoutInflater.from(context).inflate(R.layout.sample_receiver,parent,false);
            return new receiverViewHolder(view);

        }

    }

    @Override
    public int getItemViewType(int position) {

        if(messagesModels.get(position).getUid().equals(FirebaseAuth.getInstance().getUid())){

            return SENDER_VIEW_TYPE;
        }
        else {
            return RECEIVER_VIEW_TYPE;
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        MessageModel messagesModel=messagesModels.get(position);

        if(holder.getClass()==SenderViewHolder.class){
            ((SenderViewHolder)holder).senderMssg.setText(messagesModel.getMessage());
            ((SenderViewHolder)holder).senderTime.setText(TimeAgo.using(messagesModel.getPostedAt()));
        }
        else {
            ((receiverViewHolder)holder).receiverMssg.setText(messagesModel.getMessage());
            ((receiverViewHolder)holder).receiverTime.setText(TimeAgo.using(messagesModel.getPostedAt()));
        }

    }

    @Override
    public int getItemCount() {
        return messagesModels.size();
    }

    public class receiverViewHolder extends RecyclerView.ViewHolder {

        TextView receiverMssg, receiverTime;

        public receiverViewHolder(@NonNull View itemView) {
            super(itemView);

            receiverMssg = itemView.findViewById(R.id.receiverText);
            receiverTime = itemView.findViewById(R.id.recTime);




        }
    }

    public class SenderViewHolder extends RecyclerView.ViewHolder {

        TextView senderMssg,senderTime;
        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);

            senderMssg=itemView.findViewById(R.id.senderText);
            senderTime=itemView.findViewById(R.id.senderTime);

        }
    }


}
