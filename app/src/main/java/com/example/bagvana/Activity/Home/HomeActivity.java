package com.example.bagvana.Activity.Home;

import static com.example.bagvana.Utils.Utils._user;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.andremion.counterfab.CounterFab;
import com.example.bagvana.Activity.Chatbot.ChatActivity;
import com.example.bagvana.Activity.LoginRegister.SignInActivity;
import com.example.bagvana.Activity.Order.CartActivity;
import com.example.bagvana.Activity.OrderList.OrderListActivity;
import com.example.bagvana.Activity.ProductList.ProductListActivity;
import com.example.bagvana.Activity.Profile.ProfileActivity;
import com.example.bagvana.R;
import com.example.bagvana.Activity.Wishlist.WishlistActivity;
import com.example.bagvana.fragments.HomeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    Menu menu;
    CounterFab counterFabChat, counterFabCart;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        counterFabChat = (CounterFab) findViewById(R.id.btnChat);
//        textView = findViewById(R.id.textView);
        toolbar = findViewById(R.id.toolbar);

        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new
                ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.menu_home);

//        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
//        bottomNavigationView.setOnItemSelectedListener(this);

        loadFragment(new HomeFragment());
        menu = navigationView.getMenu();
//        menu.findItem(R.id.ic_logout).setVisible(false);
//        menu.findItem(R.id.ic_account).setVisible(false);
        counterFabChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, ChatActivity.class);
//                myIntent.putExtras(myBundle);
                startActivity(intent);
            }
        });
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
            case R.id.menu_login:
                myIntent = new Intent(HomeActivity.this, SignInActivity.class);
//                myIntent.putExtras(myBundle);
                startActivity(myIntent);
                break;
            case R.id.menu_chat:
                myIntent = new Intent(HomeActivity.this, ChatActivity.class);
//                myIntent.putExtras(myBundle);
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