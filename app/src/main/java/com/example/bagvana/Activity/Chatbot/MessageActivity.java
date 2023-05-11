package com.example.bagvana.Activity.Chatbot;

import static com.example.bagvana.Utils.Utils._list_user;
import static com.example.bagvana.Utils.Utils._user;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bagvana.Adapter.MessageAdapter;
import com.example.bagvana.DTO.Chat;
import com.example.bagvana.DTO.User;
import com.example.bagvana.R;
import com.example.bagvana.databinding.ActivityChatBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageActivity extends AppCompatActivity {
    ActivityChatBinding binding;
    Toolbar toolbar;
    private TextView fullname;
    CircleImageView circleImageView;
    private String id,imageUrl;
    private EditText inputMessage;
    private ImageButton btn_send;
    MessageAdapter messageAdapter;
    List<Chat>   mchat;
    DatabaseReference reference;
    int countMessage = 0;

    RecyclerView recyclerView;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        id = getIntent().getStringExtra("userId").toString();

        circleImageView = (CircleImageView) findViewById(R.id.profileImage);
        fullname = (TextView) findViewById(R.id.textName);
        inputMessage = (EditText)findViewById(R.id.inputMessage);
        btn_send = (ImageButton) findViewById(R.id.btn_send);
        recyclerView = (RecyclerView)findViewById(R.id.recycle_view);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        // header
        User userChat = _list_user.stream()
                .filter(user -> id.equals(user.getId()))
                .findAny()
                .orElse(null);
        if(userChat == null){


        }else{
            if(userChat.getFullname().equals("")){
                fullname.setText(userChat.getUsername());
            }
            else{
                fullname.setText(userChat.getFullname());
            }
            if(!userChat.getAvatar().equals("")){
                Glide.with(MessageActivity.this)
                        .load(userChat.getAvatar())
                        .into(circleImageView);
            }
            else {
                circleImageView.setImageResource(R.drawable.ic_user);
            }

        }
        readMessage(_user.getId(),userChat.getId());
        // body
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = inputMessage.getText().toString();
                if(!msg.equals("")){

                    sendMessage(_user.getId(),userChat.getId(),inputMessage.getText().toString());
                }
                else{
                    inputMessage.setError("Bạn không thể gửi tin nhắn rỗng");

                }
                inputMessage.setText("");
            }

        });


    }
    public void sendMessage(String sender, String receiver, String message){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        HashMap<String,Object> hashMap = new HashMap<>();

        hashMap.put("sender",sender);
        hashMap.put("receiver",receiver);
        hashMap.put("message",message);

        reference.child("Chats").push().setValue(hashMap);
    }
    public void readMessage(String myId, String receiverId){
        mchat = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mchat.clear();
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){

                    Chat chat = snapshot.getValue(Chat.class);
                    if(chat.getReceiver().equals(receiverId)  && chat.getSender().equals(myId) ||
                            chat.getReceiver().equals(myId)  && chat.getSender().equals(receiverId))
                    {
                        mchat.add(chat);
                    }
                    messageAdapter = new MessageAdapter(MessageActivity.this,mchat);

                    recyclerView.setAdapter(messageAdapter);

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }
    public void setSupportActionBar(Toolbar toolbar_order) {
        toolbar_order.setNavigationIcon(R.drawable.ic_back);
        toolbar_order.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}