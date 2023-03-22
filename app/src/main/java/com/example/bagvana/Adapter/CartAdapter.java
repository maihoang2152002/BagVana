package com.example.bagvana.Adapter;

import android.content.Context;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;

import com.example.bagvana.DTO.Product;

public class CartAdapter extends ArrayAdapter<Product> {
    public CartAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }
}
