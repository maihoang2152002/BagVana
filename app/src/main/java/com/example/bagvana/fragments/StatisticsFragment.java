package com.example.bagvana.fragments;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.bagvana.DTO.Order;
import com.example.bagvana.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StatisticsFragment} factory method to
 * create an instance of this fragment.
 */
public class StatisticsFragment extends Fragment {

    TextView txt_order_processing, txt_order_delivering, txt_order_delivered,
            txt_total_revenue;
    String status;

    public StatisticsFragment(String status) {
        this.status = status;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_admin_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        txt_order_processing = view.findViewById(R.id.txt_order_processing);
        txt_order_delivering = view.findViewById(R.id.txt_order_delivering);
        txt_order_delivered = view.findViewById(R.id.txt_order_delivered);
        txt_total_revenue = view.findViewById(R.id.txt_total_revenue);

        ArrayList<Order> orderListProcessing = new ArrayList<>();
        ArrayList<Order> orderListDelivering = new ArrayList<>();
        ArrayList<Order> orderListDelivered = new ArrayList<>();


        DatabaseReference databaseReferenceHome = FirebaseDatabase.getInstance().getReference("Order");
        databaseReferenceHome.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int temp = 0;
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    Order order = dataSnapshot.getValue(Order.class);
                    if (status.equals("0")) {
                        assert order != null;
                        if (order.getStatus().equals("1")) {
                            orderListProcessing.add(order);
                        } else if (order.getStatus().equals("2")) {
                            orderListDelivering.add(order);
                        } else {
                            orderListDelivered.add(order);
                            temp += order.getTotalPrice();
                        }
                    } else if (status.equals("1")) {
                        assert order != null;
                        String[] strDate = order.getOrderDate().split(" ");
                        LocalDateTime localDateTime = LocalDateTime.now();
                        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                        String formattedDate = localDateTime.format(myFormatObj);

                        if (order.getStatus().equals("1") && strDate[0].equals(formattedDate)) {
                            orderListProcessing.add(order);
                        } else if (order.getStatus().equals("2") && strDate[0].equals(formattedDate)) {
                            orderListDelivering.add(order);
                        } else if (strDate[0].equals(formattedDate)) {
                            orderListDelivered.add(order);
                            temp += order.getTotalPrice();
                        }
                    } else {
                        assert order != null;
                        String[] strDate = order.getOrderDate().split(" ");
                        LocalDateTime localDateTime = LocalDateTime.now().minusDays(1);
                        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                        String formattedDate = localDateTime.format(myFormatObj);

                        if (order.getStatus().equals("1") && strDate[0].equals(formattedDate)) {
                            orderListProcessing.add(order);
                        } else if (order.getStatus().equals("2") && strDate[0].equals(formattedDate)) {
                            orderListDelivering.add(order);
                        } else if (strDate[0].equals(formattedDate)) {
                            orderListDelivered.add(order);
                            temp += order.getTotalPrice();
                        }
                    }
                }

                txt_total_revenue.setText((NumberFormat.getNumberInstance(Locale.US).format(temp)));
                txt_order_processing.setText(String.valueOf(orderListProcessing.size()));
                txt_order_delivering.setText(String.valueOf(orderListDelivering.size()));
                txt_order_delivered.setText(String.valueOf(orderListDelivered.size()));
                BarChart barChart = view.findViewById(R.id.bar_chart);

                List<BarEntry> barEntries = new ArrayList<>();
                barEntries.add(new BarEntry(3f, orderListProcessing.size()));
                barEntries.add(new BarEntry(2f, orderListDelivering.size()));
                barEntries.add(new BarEntry(1f, orderListDelivered.size()));

                BarDataSet barDataSet = new BarDataSet(barEntries, "Bar Data Set");
                barDataSet.setColors(Color.GREEN, Color.CYAN, Color.MAGENTA);
                barDataSet.setValueTextColor(Color.BLACK);

                BarData barData = new BarData(barDataSet);
                barChart.setData(barData);

                barChart.setDrawValueAboveBar(true);
                barChart.getDescription().setEnabled(false);
                barChart.getLegend().setEnabled(false);
                barChart.getAxisRight().setEnabled(false);

                barChart.invalidate();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}