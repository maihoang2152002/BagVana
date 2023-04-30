package com.example.bagvana.fragments;

import static org.greenrobot.eventbus.EventBus.TAG;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.bagvana.Adapter.ListUserAdapter;
import com.example.bagvana.DTO.User;
import com.example.bagvana.R;
import com.example.bagvana.Utils.Utils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AdminAllUserFragment extends Fragment {

    private RecyclerView recyclerView;
    private ListUserAdapter userAdapter;



//    private List<User> mUsers;

    @SuppressLint("MissingInflatedId")
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_admin_all_user,container,false);
        recyclerView = view.findViewById(R.id.recycle_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        readUsers();

        return view;
    }

    @SuppressLint("UseRequireInsteadOfGet")
    private void readUsers() {

        userAdapter = new ListUserAdapter(Objects.requireNonNull(getContext()), Utils._admin_list_user);
        recyclerView.setAdapter(userAdapter);

    }
}