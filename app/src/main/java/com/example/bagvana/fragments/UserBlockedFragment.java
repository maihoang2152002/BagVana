package com.example.bagvana.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bagvana.Adapter.ListUserAdapter;
import com.example.bagvana.DTO.User;
import com.example.bagvana.R;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class UserBlockedFragment extends Fragment {
    private RecyclerView recyclerView;
    private ListUserAdapter userAdapter;

    private List<User> list_user_blocked = new ArrayList<>();
    private List<User> list_user_blocked_prev = new ArrayList<>();




//    private List<User> mUsers;

    @SuppressLint("MissingInflatedId")
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_admin_all_user_blocked,container,false);
        recyclerView = view.findViewById(R.id.recycle_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("User");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list_user_blocked.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    User user = dataSnapshot.getValue(User.class);
                    if(user.getStatus().equals("0")){
                        list_user_blocked.add(user);
                    }

                }
                for(User user : list_user_blocked_prev){
                    if(list_user_blocked.indexOf(user) != -1){
                        list_user_blocked.remove(user);
                    }
                }
                List<User> newList = list_user_blocked.stream()
                        .distinct()
                        .collect(Collectors.toList());
                userAdapter = new ListUserAdapter(getContext(), newList);
                recyclerView.setAdapter(userAdapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;
    }

}
