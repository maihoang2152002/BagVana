package com.example.bagvana.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bagvana.Activity.Chatbot.MessageActivity;
import com.example.bagvana.Activity.Home.AdminHomeActivity;
import com.example.bagvana.Activity.Profile.EditProfileActivity;
import com.example.bagvana.Activity.SellerAdmin.AdminEditProfileUserActivity;
import com.example.bagvana.DTO.User;
import com.example.bagvana.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ListUserAdapter extends RecyclerView.Adapter<ListUserAdapter.ViewHolder> {
    private Context mcontext;
    private List<User> mUser;


    public ListUserAdapter(@NonNull Context context, @NonNull List<User> users) {
//        super(context,users);
        this.mcontext = context;
        this.mUser = users;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.user_item_admin,parent,false);

        return new ViewHolder(view);
    }
    @NonNull
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = mUser.get(position);
        final boolean[] checkedLocked = {true};
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

        if(user.getStatus().equals("0")){
            holder.lockedUser.setImageResource(R.drawable.ic_lock_person);

        }
        else{
            holder.lockedUser.setImageResource(R.drawable.ic_lock_open);
        }
        holder.lockedUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(user.getStatus().equals("0")){
                    checkedLocked[0] = false;
                    noticeLockedUser(user, holder, checkedLocked[0]);

                }
                else{
                    checkedLocked[0] = true;
                    noticeLockedUser(user, holder, checkedLocked[0]);

                }
            }
        });

        holder.editUserProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mcontext, AdminEditProfileUserActivity.class);
                intent.putExtra("userId",user.getId());
                mcontext.startActivity(intent);
            }
        });

    }
    private void noticeLockedUser(User user,ViewHolder holder, boolean typeNotice) {
        AlertDialog.Builder alert = new AlertDialog.Builder(mcontext);
        alert.setCancelable(false);
        alert.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alert.setPositiveButton("Chắc chắn", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // khoa
                if(typeNotice){
                    Toast.makeText(mcontext,"Khóa thành công",Toast.LENGTH_SHORT).show();
                    user.setStatus("0");
                    holder.lockedUser.setImageResource(R.drawable.ic_lock_person);
                }
                else{
                    Toast.makeText(mcontext,"Mở khóa thành công",Toast.LENGTH_SHORT).show();
                    user.setStatus("1");
                    holder.lockedUser.setImageResource(R.drawable.ic_lock_person);
                }

                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("User");
                reference.child(user.getId()).setValue(user);

            }
        });
        if(typeNotice){
            alert.setTitle("Khóa người dùng!");
            alert.setMessage("Bạn chắc chắn muốn khóa người dùng này?");
        }
        else{
            alert.setTitle("Mở khóa người dùng!");
            alert.setMessage("Bạn chắc chắn muốn mở khóa người dùng này?");
        }

        alert.show();
    }
    @Override
    public int getItemCount() {
        return mUser.size();
    }

    public  class ViewHolder extends RecyclerView.ViewHolder{

        public TextView username;
        public ImageView image_profile,editUserProfile, lockedUser;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.username);
            image_profile = itemView.findViewById(R.id.image_profile);
            editUserProfile = itemView.findViewById(R.id.btnEditUser);
            lockedUser = itemView.findViewById(R.id.btnLockUser);

        }
    }
}
