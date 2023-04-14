//package com.example.bagvana;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.annotation.SuppressLint;
//import android.content.Intent;
//import android.media.Image;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.example.bagvana.DTO.Order;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
//
//import java.text.NumberFormat;
//import java.util.ArrayList;
//import java.util.Locale;
//
//public class MainActivity extends AppCompatActivity {
//
//    ImageView back;
//    TextView txt_order_processing, txt_order_delivering, txt_order_delivered,
//            txt_total_revenue;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_statistics);
//
//        back = findViewById(R.id.imageView);
//        txt_order_processing = findViewById(R.id.txt_order_processing);
//        txt_order_delivering = findViewById(R.id.txt_order_delivering);
//        txt_order_delivered = findViewById(R.id.txt_order_delivered);
//        txt_total_revenue = findViewById(R.id.txt_total_revenue);
//
//        ArrayList<Order> orderListProcessing = new ArrayList<>();
//        ArrayList<Order> orderListDelivering = new ArrayList<>();
//        ArrayList<Order> orderListDelivered = new ArrayList<>();
//
//
//        DatabaseReference databaseReferenceHome = FirebaseDatabase.getInstance().getReference("Order");
//        databaseReferenceHome.addValueEventListener(new ValueEventListener() {
//            @SuppressLint("NotifyDataSetChanged")
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                int temp = 0;
//                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//
//                    Order order = dataSnapshot.getValue(Order.class);
//                    temp += order.getTotalPrice();
//                    if (order.getStatus().equals("1")) {
//                        orderListProcessing.add(order);
//                    } else if (order.getStatus().equals("2")) {
//                        orderListDelivering.add(order);
//                    } else { orderListDelivered.add(order); }
//                }
//
//                txt_total_revenue.setText((NumberFormat.getNumberInstance(Locale.US).format(temp)));
//                txt_order_processing.setText(String.valueOf(orderListProcessing.size()));
//                txt_order_delivering.setText(String.valueOf(orderListDelivering.size()));
//                txt_order_delivered.setText(String.valueOf(orderListDelivered.size()));
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//
//    }
//
//    // Task Completed
//    // Thanks for watching
//    //See you in the next video.
//}
