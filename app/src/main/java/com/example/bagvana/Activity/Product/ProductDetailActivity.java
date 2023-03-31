package com.example.bagvana.Activity.Product;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.bagvana.R;

public class ProductDetailActivity extends AppCompatActivity {
    private ImageView imageSelected;
    TextView name, color, price, description;
    String strColor, strName, strPrice, strDescription, img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        imageSelected = findViewById(R.id.img_selected);
        name = findViewById(R.id.name_product);
        color = findViewById(R.id.color_product);
        price = findViewById(R.id.price_product);
        description = findViewById(R.id.description_product);

        strColor = getIntent().getStringExtra("color");
        strName = getIntent().getStringExtra("name");
        strPrice = String.valueOf(getIntent().getIntExtra("price", 0));
        strDescription = getIntent().getStringExtra("description");
        img = getIntent().getStringExtra("image");

        price.setText("$" + strPrice);
        description.setText(strDescription);
        name.setText(strName);
        color.setText(strColor);
        Glide.with(this).load(img)
                .centerCrop().placeholder(R.drawable.ic_account)
                .into(imageSelected);
    }
}