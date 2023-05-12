package com.example.bagvana.Activity.Notification;

import static com.example.bagvana.Utils.Utils._user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.bagvana.Activity.OrderList.OrderDetailActivity;
import com.example.bagvana.Adapter.NotificationAdapter;
import com.example.bagvana.DAO.NotificationDAO;
import com.example.bagvana.DTO.Notification;
import com.example.bagvana.DTO.Order;
import com.example.bagvana.R;
import com.example.bagvana.listeners.ItemListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class NotificationActivity extends AppCompatActivity implements ItemListener {
    private RecyclerView recycview_updateOrderNotification;
    private LinearLayout linear_newProductNotification;
    private NotificationAdapter notificationAdapter;
    private ArrayList<Notification> notifications;
    private TextView txt_sizeNotification;
    private Toolbar toolbar_notification;
    DatabaseReference databaseReferenceUpdateOrderNotification = FirebaseDatabase.getInstance().getReference("Notification").child("UpdateOrder").child(_user.getId());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        toolbar_notification = findViewById(R.id.toolbar_notification);
        setSupportActionBar(toolbar_notification);
        recycview_updateOrderNotification = findViewById(R.id.recycview_updateOrderNotification);
        linear_newProductNotification = findViewById(R.id.linear_newProductNotification);
        txt_sizeNotification = findViewById(R.id.txt_sizeNotification);
        txt_sizeNotification.setVisibility(View.INVISIBLE);
        loadSizeOfNewProductNotification();

        recycview_updateOrderNotification.setLayoutManager(new LinearLayoutManager(this));

        notifications = new ArrayList<>();

        loadUpdateOrderNotification();

        linear_newProductNotification.setOnClickListener(v -> {
            Intent myIntent = new Intent(NotificationActivity.this, ProductNotificationActivity.class);
            startActivity(myIntent);
        });
    }

    private void loadSizeOfNewProductNotification() {
        NotificationDAO notificationDAO = new NotificationDAO();
        notificationDAO.readNotificationOfNewProduct(new NotificationDAO.MyCallback() {
            @Override
            public void onCallback(long newNotification) {
                if(newNotification != 0) {
                    txt_sizeNotification.setVisibility(View.VISIBLE);
                    txt_sizeNotification.setText(String.valueOf(newNotification));
                } else {
                    txt_sizeNotification.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    private void loadUpdateOrderNotification() {
        databaseReferenceUpdateOrderNotification.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                notifications.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Notification notification = dataSnapshot.getValue(Notification.class);

                    // set điều kiện ngày 1 tuần
                    try {
                        if(compareDate(notification.getTime())) {
                            notifications.add(notification);
                        }
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }


                }

                notificationAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        notificationAdapter = new NotificationAdapter(this, notifications, this);
        recycview_updateOrderNotification.setAdapter(notificationAdapter);
    }

    public boolean compareDate(String inputDate) throws ParseException {

        // Ép kiểu
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        SimpleDateFormat inputDateFormat = new SimpleDateFormat("HH:mm dd/MM/yyyy");

        Date date = inputDateFormat.parse(inputDate);

        SimpleDateFormat outputDateFormat = new SimpleDateFormat("dd/MM/yyyy");

        String outputDate = outputDateFormat.format(date);

        Date today = sdf.parse(outputDate);

        // So sánh
        Calendar calendar = Calendar.getInstance();

        calendar.add(Calendar.DAY_OF_YEAR, -7);

        Date oneWeekAgo = calendar.getTime();

        if (today.compareTo(oneWeekAgo) > 0) {
            return false;
        }

        return true;
    }

    @Override
    public void OnItemPosition(int position) {
        Intent intent = new Intent(this, OrderDetailActivity.class);

        // set status = 1 -> seen
        databaseReferenceUpdateOrderNotification.child(notifications.get(position).getId()).child("status").setValue("1");

        DatabaseReference databaseReferenceOrder = FirebaseDatabase.getInstance().getReference("Order");
        databaseReferenceOrder.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    Order order = dataSnapshot.getValue(Order.class);
                    if(order.getOrderID().equals(notifications.get(position).getId())) {

                        intent.putExtra("order", order);
                        startActivity(intent);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void setSupportActionBar(Toolbar toolbar) {
        toolbar.setNavigationIcon(R.drawable.ic_back);
//        toolbar.setNavigationOnClickListener(view -> finish());

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();

            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        loadUpdateOrderNotification();
        loadSizeOfNewProductNotification();
    }
}