package com.example.firebaseapp.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.example.firebaseapp.R;
import com.example.firebaseapp.RecyclerView.Adapter.MessageAdapter;
import com.example.firebaseapp.RecyclerView.ChatRoom.Message;
import com.example.firebaseapp.User.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class MessageFragment extends Fragment {

    ArrayList<User> user;
    ArrayList<Message> messages;
    MessageAdapter adapter;
    ShimmerRecyclerView recycler;
    FirebaseDatabase database=FirebaseDatabase.getInstance();

    public MessageFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull  LayoutInflater inflater, @Nullable  ViewGroup container, @Nullable  Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_message,container,false);
    }

    @Override
    public void onViewCreated(@NonNull  View view, @Nullable  Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        messages=new ArrayList<>();

        initializeViews();

        recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        recycler.setAdapter(adapter);
        if(user.isEmpty())
            recycler.hideShimmerAdapter();
        recycler.showShimmerAdapter();

        database.getReference().child("Users").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                user.clear();
                for (DataSnapshot dSnap : snapshot.getChildren()) {

                    User user1 = dSnap.getValue(User.class);
                    user.add(user1);
                }
                adapter.notifyDataSetChanged();
                recycler.hideShimmerAdapter();
            }
            @Override
            public void onCancelled(@NonNull  DatabaseError error) {

            }

        });

    }

    private void initializeViews() {
        user=new ArrayList<>();
        adapter=new MessageAdapter(getActivity(),user,messages);
        recycler=getActivity().findViewById(R.id.id_recycler_message);
    }

}