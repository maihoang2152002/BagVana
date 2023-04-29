package com.example.bagvana.Adapter;

import static com.example.bagvana.Utils.Utils._user;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bagvana.Activity.Chatbot.ChatActivity;
import com.example.bagvana.Activity.Chatbot.MessageActivity;
import com.example.bagvana.DTO.User;
import com.example.bagvana.R;

import java.util.List;

public class ChatBotAdapter extends RecyclerView.Adapter<ChatBotAdapter.ViewHolder> {
    private Context mcontext;
    private List<User> mUser;


    public ChatBotAdapter(@NonNull Context context, @NonNull List<User> users) {
//        super(context,users);
        this.mcontext = context;
        this.mUser = users;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.user_item,parent,false);

        return new ViewHolder(view);
    }
    @NonNull
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = mUser.get(position);
        if(user.getFullname().equals("")){
            holder.username.setText(user.getUsername());
        }
        else{
            holder.username.setText(user.getFullname());

        }
        if(!user.getAvatar().equals("")){
            Glide.with(mcontext)
                    .load(user.getAvatar())
                    .into(holder.image_profile);
        }
        else {
            holder.image_profile.setImageResource(R.drawable.ic_user);
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mcontext, MessageActivity.class);
                intent.putExtra("userId",user.getId());

                mcontext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mUser.size();
    }

    public  class ViewHolder extends RecyclerView.ViewHolder{

        public TextView username;
        public ImageView image_profile;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.username);
            image_profile = itemView.findViewById(R.id.image_profile);
        }
    }
}
