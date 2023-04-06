package com.example.bagvana.DAO;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.bagvana.DTO.ReceiverInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class ReveiverInfoDAO {
    private ArrayList<ReceiverInfo> receiverInfos = new ArrayList<>();
    private String userID;
    private String count;
    public ReveiverInfoDAO(String userID) {
        this.userID = userID;

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("ReceiverInfo").child(userID);
        Query query = databaseReference.orderByKey().limitToLast(1);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                    System.out.println(childSnapshot.getKey());
                    count = childSnapshot.getKey();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                throw databaseError.toException();
            }
        });

    }

    public ArrayList<ReceiverInfo> getReveiverInfoDAOS() {
        return receiverInfos;
    }

    public void setReveiverInfoDAOS(ArrayList<ReceiverInfo> reveiverInfoDAOS) {
        this.receiverInfos = reveiverInfoDAOS;
    }

    public void addReceiverInfoFirebase(ReceiverInfo receiverInfo) {
        receiverInfos.add(receiverInfo);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("ReceiverInfo").child(userID);

        databaseReference.child(receiverInfo.getAddressID()).setValue(receiverInfo);
    }

    public String getNewAddressID() {

        if(count == "0") {
            return "1";
        }

        int newAddressID = Integer.parseInt(count) + 1;

        return String.valueOf(newAddressID);
    }

    public int getReceiverInfoSize() {
        return receiverInfos.size();
    }
}
