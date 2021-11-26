package com.example.firebaseapp;

import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firebaseapp.RecyclerView.Adapter.ChatAdapter;
import com.example.firebaseapp.RecyclerView.ChatRoom.Message;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ChatRoom extends AppCompatActivity {
    EditText text;
    ImageView send;
    ChatAdapter adapter;
    ArrayList<Message> messages;
    FirebaseAuth auth;
    String sender,receiver;
    FirebaseDatabase database;
    String senderRoom,receiverRoom;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chatroom_activity);
        getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.dark_login));
        initViews();
        messages=new ArrayList<>();
        adapter=new ChatAdapter(this,messages);
        auth=FirebaseAuth.getInstance();
        String Uid=auth.getUid();
        database=FirebaseDatabase.getInstance();
        recyclerView=findViewById(R.id.id_chats);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        getSupportActionBar().setTitle(getIntent().getStringExtra("name"));
        String receiverUid=getIntent().getStringExtra("uid");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        senderRoom=Uid+receiverUid;
        receiverRoom=receiverUid+Uid;

//        Save all the messages
        send.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        if(!TextUtils.isEmpty(text.getText()))
                                        ChatRoom.this.saveMessages();

                                    }
                                });
        fetchData();
    }

    private void saveMessages() {

                String txt=text.getText().toString();
                Message msg =new Message(FirebaseAuth.getInstance().getUid(),txt);

                    database.getReference().child("Chats")
                            .child(senderRoom)
                            .child("Messages")
                            .push()
                            .setValue(msg)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    database.getReference().child("Chats")
                                            .child(receiverRoom)
                                            .child("Messages")
                                            .push()
                                            .setValue(msg)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {

                                                }
                                            });

                                }
                            });

        text.setText("");


    }

    void fetchData()
    {
        database.getReference()
                .child("Chats")
                .child(senderRoom)
                .child("Messages").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull  DataSnapshot snapshot) {
                messages.clear();
                for(DataSnapshot dSnap : snapshot.getChildren())
                {
                    Message msg=dSnap.getValue(Message.class);
                    messages.add(msg);
                }
                adapter.notifyDataSetChanged();
                recyclerView.scrollToPosition(messages.size()-1);
            }

            @Override
            public void onCancelled(@NonNull  DatabaseError error) {

            }
        });


    }

    //    Initializing the views
    private void initViews()
    {
        text=findViewById(R.id.id_message);
        send=findViewById(R.id.id_send);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}
