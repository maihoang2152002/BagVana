package com.example.bagvana.Activity.ProductList;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bagvana.Activity.Home.HomeActivity;
import com.example.bagvana.Activity.Order.CartActivity;
import com.example.bagvana.Activity.Product.ProductDetailActivity;
import com.example.bagvana.Activity.Profile.ProfileActivity;
import com.example.bagvana.Activity.Wishlist.WishlistActivity;
import com.example.bagvana.Adapter.ProductListAdapter;
import com.example.bagvana.DTO.Product;
import com.example.bagvana.R;
import com.example.bagvana.fragments.HomeFragment;
import com.example.bagvana.listeners.ItemListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ProductListActivity extends AppCompatActivity implements ItemListener, NavigationBarView.OnItemSelectedListener {

    private RecyclerView recyclerView;
    private ProductListAdapter productListAdapter;
    private ArrayList<Product> productList;
    private String textSearchFirst;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = findViewById(R.id.recyclerviewId);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        productList = new ArrayList<>();

        DatabaseReference databaseReferenceHome = FirebaseDatabase.getInstance().getReference("Product");
        databaseReferenceHome.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    Product product = dataSnapshot.getValue(Product.class);
                    productList.add(product);
                }

                productListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        productListAdapter = new ProductListAdapter(this, productList, this);
        recyclerView.setAdapter(productListAdapter);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_item,menu);
        MenuItem item = menu.findItem(R.id.searchId);
        SearchView searchView = (SearchView) item.getActionView();
        if (textSearchFirst == null) {
            textSearchFirst = getIntent().getStringExtra("query");
            mySearch(textSearchFirst);
        }

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mySearch(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
//                mySearch(newText);
                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void mySearch(String str) {
        ArrayList<Product> productListSearch = new ArrayList<>();

        if (TextUtils.isEmpty(str)) {
            productListAdapter.notifyDataSetChanged();

            productListAdapter = new ProductListAdapter(this, productList, this);
            recyclerView.setAdapter(productListAdapter);
        } else {
            for (Product product : productList) {
                if (product.hasNameSimilarTo(str))
                    productListSearch.add(product);
            }
            productListAdapter.notifyDataSetChanged();

            productListAdapter = new ProductListAdapter(this, productListSearch, this);
            recyclerView.setAdapter(productListAdapter);
        }

    }

    @Override
    public void OnItemPosition(int position) {
        Intent intent = new Intent(this, ProductDetailActivity.class);
        intent.putExtra("product", productList.get(position));
        startActivity(intent);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        Fragment fragment = null;
        switch (item.getItemId()) {
            case R.id.menu_home:
                Intent myIntent4 = new Intent(ProductListActivity.this, HomeActivity.class);
//                myIntent.putExtras(myBundle);
                startActivity(myIntent4);
                break;
            case R.id.menu_fav:
                Intent myIntent3 = new Intent(ProductListActivity.this, WishlistActivity.class);
//                myIntent.putExtras(myBundle);
                startActivity(myIntent3);
                break;
            case R.id.menu_account:
                Intent myIntent1 = new Intent(ProductListActivity.this, ProfileActivity.class);
//                myIntent.putExtras(myBundle);
                startActivity(myIntent1);
                break;
            case R.id.menu_cart:
                Intent myIntent = new Intent(ProductListActivity.this, CartActivity.class);
//                myIntent.putExtras(myBundle);
                startActivity(myIntent);
                break;
        }
        return true;
    }

    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }
}