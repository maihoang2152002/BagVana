package com.example.bagvana.fragments;

import static com.example.bagvana.Utils.Utils._list_user;
import static com.example.bagvana.Utils.Utils._user;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bagvana.Adapter.ChatBotAdapter;
import com.example.bagvana.DTO.Chat;
import com.example.bagvana.DTO.User;
import com.example.bagvana.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class ChatsFragment extends Fragment {

    private RecyclerView recyclerView;
    private ChatBotAdapter userAdapter;

    private List<User> mUsers;
    private List<String> usersList;


    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chats,container,false);
        recyclerView = view.findViewById(R.id.recycle_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        usersList = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                usersList.clear();
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    Chat chat = snapshot.getValue(Chat.class);
                    if(chat.getSender().equals(_user.getId())){
                        usersList.add(chat.getReceiver());
                        Log.e("receiver",chat.getReceiver());
                    }
                    if(chat.getReceiver().equals(_user.getId())){
                        usersList.add(chat.getSender());
                        Log.e("sender",chat.getSender());

                    }
                }
                readChats();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;
    }

    private void readChats() {
        mUsers = new ArrayList<>();
        int i = 0;
        for(String id:usersList){
            Log.e("id userlist",id);
            User userChat = _list_user.stream()
                    .filter(user -> id.equals(user.getId()))
                    .findAny()
                    .orElse(null);
            if(userChat == null){
                Log.e("object","null");

            }else{
                mUsers.add(userChat);
                i++;
                Log.e("add lan ","" + i);
            }
        }

        userAdapter = new ChatBotAdapter(getContext(),mUsers);
        recyclerView.setAdapter(userAdapter);


    }
}