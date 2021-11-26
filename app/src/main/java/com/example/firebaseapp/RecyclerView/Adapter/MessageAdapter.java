package com.example.firebaseapp.RecyclerView.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.firebaseapp.ChatRoom;
import com.example.firebaseapp.R;
import com.example.firebaseapp.RecyclerView.ChatRoom.Message;
import com.example.firebaseapp.User.User;

import java.util.ArrayList;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    private static ArrayList<User> user;
    ArrayList<Message> messages;

    private Context context;

    public MessageAdapter(Context context, ArrayList<User> user, ArrayList<Message> messages)
    {
        this.user=user;
        this.context=context;
        this.messages=messages;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_message,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull  MessageAdapter.ViewHolder holder, int position) {
        User user =MessageAdapter.user.get(position);
        holder.user_name.setText(user.getName());
        Glide.with(context).load(user.getAvatar()).into(holder.user_image);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ChatRoom.class);
                intent.putExtra("name", user.getName());
                intent.putExtra("avatar_url", user.getAvatar());
                intent.putExtra("uid",user.getId());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                view.getContext().startActivity(intent);
            }
        });

        holder.user_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog nagDialog = new Dialog(context,android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
                nagDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                nagDialog.setCancelable(true);
                nagDialog.setContentView(R.layout.popup_image);

                ImageView img=nagDialog.findViewById(R.id.id_popup_image);
                if(user.getAvatar()==null)
                {
                    img.setImageResource(R.drawable.account_icon);
                }
                else
                {
                    img.setImageDrawable(holder.user_image.getDrawable());
                }
                nagDialog.onWindowFocusChanged(true);
                nagDialog.show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return user.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView user_image;
        TextView user_name,user_last_message,user_time;

        ViewHolder(View view)
        {
            super(view);
            user_name=view.findViewById(R.id.id_user_name);
            user_image=view.findViewById(R.id.id_user_image);

        }

    }
}