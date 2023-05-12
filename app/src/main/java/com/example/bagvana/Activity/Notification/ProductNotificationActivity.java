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

import com.example.bagvana.Activity.Product.ProductDetailActivity;
import com.example.bagvana.Adapter.NotificationAdapter;
import com.example.bagvana.DTO.Notification;
import com.example.bagvana.DTO.Order;
import com.example.bagvana.DTO.Product;
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

public class ProductNotificationActivity extends AppCompatActivity implements ItemListener {

    private RecyclerView recycview_newProductNotification;
    private NotificationAdapter notificationAdapter;
    private ArrayList<Notification> notifications;
    private Toolbar toolbar_productNotification;
    DatabaseReference databaseReferenceNewProductNotification = FirebaseDatabase.getInstance().getReference("Notification").child("NewProduct").child(_user.getId());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_notification_acitvity);

        toolbar_productNotification = findViewById(R.id.toolbar_productNotification);
        setSupportActionBar(toolbar_productNotification);
        recycview_newProductNotification = findViewById(R.id.recycview_newProductNotification);

        recycview_newProductNotification.setLayoutManager(new LinearLayoutManager(this));

        notifications = new ArrayList<>();

        loadNewProductNotification();
    }

    private void loadNewProductNotification() {
        databaseReferenceNewProductNotification.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                notifications.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Notification notification = dataSnapshot.getValue(Notification.class);

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
        recycview_newProductNotification.setAdapter(notificationAdapter);
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
        Intent intent = new Intent(this, ProductDetailActivity.class);

        // set status = 1 -> seen
        databaseReferenceNewProductNotification.child(notifications.get(position).getId()).child("status").setValue("1");

        DatabaseReference databaseReferenceOrder = FirebaseDatabase.getInstance().getReference("Product");
        databaseReferenceOrder.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    Product product = dataSnapshot.getValue(Product.class);
                    if(product.getProductID().equals(notifications.get(position).getId())) {

                        intent.putExtra("product", product);
                        startActivity(intent);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    public void setSupportActionBar(androidx.appcompat.widget.Toolbar toolbar) {
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
        loadNewProductNotification();
    }
}