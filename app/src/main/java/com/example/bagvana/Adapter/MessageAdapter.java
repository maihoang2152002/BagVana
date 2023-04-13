package com.example.bagvana.Adapter;

import static com.example.bagvana.Utils.Utils._user;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bagvana.DTO.Chat;
import com.example.bagvana.R;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
    public static final int MSG_SENDER = 0;
    public static final int MSG_RECEIVER = 1;
    private Context mcontext;
    private List<Chat> mChat;
    private String imageUrl;
//    private


    public MessageAdapter(@NonNull Context context, @NonNull List<Chat> chats, String imageUrl) {
//        super(context,users);
        this.mcontext = context;
        this.mChat = chats;
        this.imageUrl = imageUrl;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == MSG_SENDER){
            View view = LayoutInflater.from(mcontext).inflate(R.layout.sender_chat_layout,parent,false);
            Log.e(" sender","" );

            return new ViewHolder(view);
        }else{
            View view = LayoutInflater.from(mcontext).inflate(R.layout.reciever_chat_layout,parent,false);
            return new ViewHolder(view);
        }

    }
    @NonNull
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Chat chat = mChat.get(position);
        holder.show_message.setText(chat.getMessage());
//        if()
    }

    @Override
    public int getItemCount() {
        return mChat.size();
    }

    public  class ViewHolder extends RecyclerView.ViewHolder{

        public TextView show_message;
        public ImageView image_profile;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            show_message = itemView.findViewById(R.id.show_message);
//            image_profile = itemView.findViewById(R.id.image_profile);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(mChat.get(position).getSender().equals(_user.getId())){
            return MSG_SENDER;
        }else{
            return MSG_RECEIVER;
        }


    }
}
