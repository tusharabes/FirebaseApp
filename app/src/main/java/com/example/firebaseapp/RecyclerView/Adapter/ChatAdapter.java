package com.example.firebaseapp.RecyclerView.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firebaseapp.R;
import com.example.firebaseapp.RecyclerView.ChatRoom.Message;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter
{
    ArrayList<Message> messages;
    Context context;
    final int ITEM_SENT=1,ITEM_RECEIVED=2;

    public ChatAdapter( Context context,ArrayList<Message> messages) {
        this.messages = messages;
        this.context = context;
    }


    @NonNull

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        if(viewType==ITEM_SENT)
        {
            View view= LayoutInflater.from(context).inflate(R.layout.message_sent,parent,false);
            return new SentViewHolder(view);
        }
        else
        {
            View view=LayoutInflater.from(context).inflate(R.layout.message_received,parent,false);
            return new ReceiveViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull  RecyclerView.ViewHolder holder, int position) {
        Message message=messages.get(position);
        if(holder.getClass()==SentViewHolder.class)
        {
            SentViewHolder viewHolder=(SentViewHolder)holder;
            viewHolder.message.setText(message.getMessage());
        }

        else
        {
            ReceiveViewHolder viewHolder=(ReceiveViewHolder)holder;
            viewHolder.message.setText(message.getMessage());
        }


    }

    @Override
    public int getItemViewType(int position) {
        Message message=messages.get(position);
        if(FirebaseAuth.getInstance().getUid().equals(message.getSenderId()))
        {
            return ITEM_SENT;
        }
        else
        {
            return ITEM_RECEIVED;
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    class SentViewHolder extends RecyclerView.ViewHolder
    {
        TextView message;

        SentViewHolder(View view) {
            super(view);
            message=view.findViewById(R.id.id_message_sent);
        }
    }
    class ReceiveViewHolder extends RecyclerView.ViewHolder {
        TextView message;
        public ReceiveViewHolder(@NonNull  View itemView) {
            super(itemView);
            message=itemView.findViewById(R.id.id_message_received);
        }
    }


}