package com.example.bagvana.Activity.Home;

import static com.example.bagvana.Utils.Utils._user;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.andremion.counterfab.CounterFab;
import com.example.bagvana.Activity.Chatbot.BubbleView;
import com.example.bagvana.Activity.Chatbot.ChatActivity;
import com.example.bagvana.Activity.LoginRegister.SignInActivity;
import com.example.bagvana.Activity.Order.CartActivity;
import com.example.bagvana.Activity.ProductList.ProductListActivity;
import com.example.bagvana.Activity.Profile.ProfileActivity;
import com.example.bagvana.Activity.Voucher.VoucherUserActivity;
import com.example.bagvana.Activity.Wishlist.WishlistActivity;
import com.example.bagvana.DTO.Product;
import com.example.bagvana.R;
import com.example.bagvana.fragments.HomeFragment;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nex3z.notificationbadge.NotificationBadge;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    Menu menu;
    NotificationBadge shopping_badge;
    ImageButton cart_button;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);

        BubbleView button = (BubbleView) LayoutInflater.from(this).inflate(R.layout.bubble_view, null);
        RelativeLayout parent = findViewById(R.id.my_layout);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(150, 150);
        // Set the layout rules
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        params.addRule(RelativeLayout.ALIGN_PARENT_END, RelativeLayout.TRUE);
        params.setMargins(30, 30, 30, 30);

        button.setLayoutParams(params);
        parent.addView(button);

        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new
                ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.menu_home);

        shopping_badge = findViewById(R.id.shopping_badge);
        cart_button = findViewById(R.id.ic_cart);

        cart_button.setOnClickListener(v -> {
            Intent myIntent = new Intent(HomeActivity.this, CartActivity.class);
//                myIntent.putExtras(myBundle);
            startActivity(myIntent);
        });

        DatabaseReference databaseReferenceHome = FirebaseDatabase.getInstance().getReference("Cart").child(_user.getId());
        databaseReferenceHome.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int count = 0;
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    Product product = dataSnapshot.getValue(Product.class);
                    count += product.getAmount();
                }

                if (count != 0) {
                    shopping_badge.setNumber(count);
                } else {
                    shopping_badge.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        loadFragment(new HomeFragment());
        menu = navigationView.getMenu();

        SearchView searchView = findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Handle search query submission
                Intent intent = new Intent(HomeActivity.this, ProductListActivity.class);
                intent.putExtra("query", query);
                startActivity(intent);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Handle search query text changes
                return true;
            }
        });
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Intent myIntent = null;
        switch (item.getItemId()) {
            case R.id.menu_home:
                Fragment fragment = new HomeFragment();
                break;
            case R.id.menu_fav:
                myIntent = new Intent(HomeActivity.this, WishlistActivity.class);
//                myIntent.putExtras(myBundle);
                startActivity(myIntent);
                break;
            case R.id.menu_account:
                myIntent = new Intent(HomeActivity.this, ProfileActivity.class);
//                myIntent.putExtras(myBundle);
                startActivity(myIntent);
                break;
            case R.id.menu_cart:
                myIntent = new Intent(HomeActivity.this, CartActivity.class);
//                myIntent.putExtras(myBundle);
                startActivity(myIntent);
                break;
            case R.id.menu_chat:
                myIntent = new Intent(HomeActivity.this, ChatActivity.class);
//                myIntent.putExtras(myBundle);
                startActivity(myIntent);
                break;
            case R.id.menu_gift:
                myIntent = new Intent(HomeActivity.this, VoucherUserActivity.class);
                startActivity(myIntent);
                break;
            case R.id.menu_logout:
                _user.ResetUser();
                Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
                startActivity(intent);
                finishAffinity();
                break;
        }
        return true;
    }

    boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}