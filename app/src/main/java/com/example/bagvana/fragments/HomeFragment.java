//package com.example.bagvana.fragments;
//
//import android.annotation.SuppressLint;
//import android.os.Bundle;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.fragment.app.Fragment;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import com.example.bagvana.Adapter.HomeAdapter;
//import com.example.bagvana.DTO.Product;
//import com.example.bagvana.R;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
//
//import java.util.ArrayList;
//
///**
// * A simple {@link Fragment} subclass.
// * Use the {@link HomeFragment} factory method to
// * create an instance of this fragment.
// */
//public class HomeFragment extends Fragment {
//    private RecyclerView recyclerview_home;
//    private ArrayList<Product> productList;
//    private HomeAdapter homeAdapter;
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_home, container, false);
//    }
//
//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//        recyclerview_home = view.findViewById(R.id.recyclerview_home);
//
//        recyclerview_home.setHasFixedSize(true);
//        recyclerview_home.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
////        recyclerview_home.setLayoutManager(new GridLayoutManager(this, 2));
////        recyclerview_home.setLayoutManager(new LinearLayoutManager(this));
//
//        productList = new ArrayList<>();
//
//        DatabaseReference databaseReferenceHome = FirebaseDatabase.getInstance().getReference("Product");
//        databaseReferenceHome.addValueEventListener(new ValueEventListener() {
//            @SuppressLint("NotifyDataSetChanged")
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//
//                    Product product = dataSnapshot.getValue(Product.class);
//                    productList.add(product);
//                }
//
//                homeAdapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//
//        homeAdapter = new HomeAdapter(getContext(), productList);
//
//        recyclerview_home.setAdapter(homeAdapter);
//    }
//}

package com.example.bagvana.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bagvana.Activity.Product.ProductDetailActivity;
import com.example.bagvana.Adapter.HomeAdapter;
import com.example.bagvana.DTO.Product;
import com.example.bagvana.R;
import com.example.bagvana.listeners.ItemListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements ItemListener {
    private RecyclerView recyclerview_home;
    private ArrayList<Product> productList;
    private HomeAdapter homeAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerview_home = view.findViewById(R.id.recyclerview_home);

        recyclerview_home.setHasFixedSize(true);
        recyclerview_home.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
//        recyclerview_home.setLayoutManager(new GridLayoutManager(this, 2));
//        recyclerview_home.setLayoutManager(new LinearLayoutManager(this));

        productList = new ArrayList<>();

        DatabaseReference databaseReferenceHome = FirebaseDatabase.getInstance().getReference("Product");
        databaseReferenceHome.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    Product product = dataSnapshot.getValue(Product.class);
                    productList.add(product);
                    Log.e("product", product.getName());
                }

                homeAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        homeAdapter = new HomeAdapter(getContext(), productList, this);

        recyclerview_home.setAdapter(homeAdapter);
    }

    @Override
    public void OnItemPosition(int position) {
        Intent intent = new Intent(getContext(), ProductDetailActivity.class);
        intent.putExtra("name", productList.get(position).getName());
        intent.putExtra("image", productList.get(position).getImage());
        intent.putExtra("color", productList.get(position).getColor());
        intent.putExtra("description", productList.get(position).getDescription());
        intent.putExtra("amount", productList.get(position).getAmount());
        intent.putExtra("price", productList.get(position).getPrice());

        startActivity(intent);
    }
}