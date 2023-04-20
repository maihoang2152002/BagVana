package com.example.bagvana.DAO;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.bagvana.DTO.Product;
import com.example.bagvana.DTO.ReceiverInfo;
import com.example.bagvana.DTO.Voucher;
import com.example.bagvana.Utils.Utils;
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
    DatabaseReference databaseReferenceReceiverInfo = FirebaseDatabase.getInstance().getReference("ReceiverInfo");
    private ArrayList<ReceiverInfo> receiverInfos = new ArrayList<>();
    private String count;
    public ReveiverInfoDAO() {
        Query query = databaseReferenceReceiverInfo.orderByKey().limitToLast(1);
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

        readData(new MyCallback() {
            @Override
            public void onCallback(ArrayList<ReceiverInfo> receiverInfos) {
                Log.e("callback", String.valueOf(receiverInfos.size()));
                if(receiverInfo.isDefaultAddress() == true) {
                    for(int i = 0; i <receiverInfos.size(); i ++) {
                        if(receiverInfos.get(i).isDefaultAddress()) {
                            databaseReferenceReceiverInfo.child(Utils._user.getId()).child(receiverInfos.get(i).getAddressID()).child("defaultAddress").setValue(false);
                        }
                    }
                }
            }
        });

        receiverInfos.add(receiverInfo);
        databaseReferenceReceiverInfo.child(Utils._user.getId()).child(receiverInfo.getAddressID()).setValue(receiverInfo);

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

    public void readData(MyCallback myCallback) {
        databaseReferenceReceiverInfo.child(Utils._user.getId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                receiverInfos.clear();
                for(DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    ReceiverInfo receiverInfo = dataSnapshot.getValue(ReceiverInfo.class);
                    receiverInfos.add(receiverInfo);
                }

                myCallback.onCallback(receiverInfos);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public interface MyCallback {
        void onCallback(ArrayList<ReceiverInfo> receiverInfos);
    }
}
