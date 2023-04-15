package com.example.bagvana.fragments;

import static com.example.bagvana.Utils.Utils._list_user;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bagvana.Adapter.ChatBotAdapter;
import com.example.bagvana.R;


public class UsersFragment extends Fragment {

    private RecyclerView recyclerView;
    private ChatBotAdapter userAdapter;

//    private List<User> mUsers;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_users,container,false);
        recyclerView = view.findViewById(R.id.recycle_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        readUsers();

        return view;
    }

    private void readUsers() {
        userAdapter = new ChatBotAdapter(getContext(),_list_user);
        recyclerView.setAdapter(userAdapter);
        Log.e("list user","");

    }
}