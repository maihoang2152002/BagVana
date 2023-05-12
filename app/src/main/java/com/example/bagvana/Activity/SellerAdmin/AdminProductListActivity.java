package com.example.bagvana.Activity.SellerAdmin;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bagvana.Activity.Home.AdminHomeActivity;
import com.example.bagvana.Adapter.AdminProductListAdapter;
import com.example.bagvana.Adapter.ProductListAdapter;
import com.example.bagvana.DTO.EventBus.settingBottomEvent;
import com.example.bagvana.DTO.Product;
import com.example.bagvana.R;
import com.example.bagvana.Utils.Utils;
import com.example.bagvana.listeners.ItemListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

public class AdminProductListActivity extends AppCompatActivity implements ItemListener, NavigationBarView.OnItemSelectedListener {

    private RecyclerView recyclerView;
    private AdminProductListAdapter productListAdapter;
    private ArrayList<Product> productList;
    private String textSearchFirst;
    private Toolbar toolbar;
    private ArrayList<Product> productListSearch;


    public void initBottomNavbar(){
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.findViewById(R.id.menu_account).setVisibility(View.GONE);
        bottomNavigationView.findViewById(R.id.menu_cart).setVisibility(View.GONE);
        bottomNavigationView.findViewById(R.id.menu_fav).setVisibility(View.GONE);
        bottomNavigationView.findViewById(R.id.menu_home).setVisibility(View.GONE);
        bottomNavigationView.findViewById(R.id.menu_add_new_product).setVisibility(View.VISIBLE);

        bottomNavigationView.setOnItemSelectedListener(AdminProductListActivity.this);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        setSupportActionBarAdd(toolbar);



        recyclerView = findViewById(R.id.recyclerviewId);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        initBottomNavbar();
        productList = new ArrayList<>();
        DatabaseReference databaseReferenceHome = FirebaseDatabase.getInstance().getReference("Product");
        databaseReferenceHome.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Utils._productList.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    Product product = dataSnapshot.getValue(Product.class);
                    productList.add(product);
                }
                Utils._productList = productList;

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        productListAdapter = new AdminProductListAdapter(this, Utils._productList, this);
        productListAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(productListAdapter);
//        BottomNavigationView bottomNavigationViewAdmin = findViewById(R.id.bottom_admin);
//        bottomNavigationViewAdmin.setOnItemSelectedListener(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminProductListActivity.this, AdminHomeActivity.class);
                startActivity(intent);
                finish();
            }
        });
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
        productListSearch = new ArrayList<>();

        if (TextUtils.isEmpty(str)) {
            productListAdapter.notifyDataSetChanged();
            productListSearch = Utils._productList;
        } else {
            for (Product product : Utils._productList) {
                if (product.hasNameSimilarTo(str))
                    productListSearch.add(product);
            }
            productListAdapter.notifyDataSetChanged();

        }
        productListAdapter = new AdminProductListAdapter(this, productListSearch, this);
        recyclerView.setAdapter(productListAdapter);

    }

    @Override
    public void OnItemPosition(int position) {
        Intent intent = new Intent(this, UpdateProductActivity.class);
        intent.putExtra("id_product", productListSearch.get(position).getProductID());
        startActivity(intent);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        Fragment fragment = null;

        switch (item.getItemId()) {
            case R.id.menu_add_new_product:
                Intent myIntent4 = new Intent(AdminProductListActivity.this, AddProductActivity.class);
//                myIntent.putExtras(myBundle);
                startActivity(myIntent4);
                break;
//            case R.id.menu_fav:
//                Intent myIntent3 = new Intent(AdminProductListActivity.this, WishlistActivity.class);
////                myIntent.putExtras(myBundle);
//                startActivity(myIntent3);
//                break;
//            case R.id.menu_account:
//                Intent myIntent1 = new Intent(AdminProductListActivity.this, ProfileActivity.class);
////                myIntent.putExtras(myBundle);
//                startActivity(myIntent1);
//                break;
//            case R.id.menu_cart:
//                Intent myIntent = new Intent(AdminProductListActivity.this, CartActivity.class);
////                myIntent.putExtras(myBundle);
//                startActivity(myIntent);
//                break;
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
    private void setSupportActionBar(android.widget.Toolbar toolbar_order) {
        toolbar_order.setNavigationIcon(R.drawable.ic_back);
        toolbar_order.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBus.getDefault().postSticky(new settingBottomEvent());
                finish();
            }
        });
    }
    @SuppressLint("MissingSuperCall")
    @Override
    protected  void onRestart(){
        super.onRestart();
        initBottomNavbar();
    }
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }
    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void settingNavbarBottom(settingBottomEvent event) {
        if(event != null) {
            initBottomNavbar();
        }
    }
}