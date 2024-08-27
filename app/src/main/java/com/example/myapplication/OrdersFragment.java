package com.example.myapplication;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class OrdersFragment extends Fragment {
    private RecyclerView recyclerView;
    private OrdersAdapter ordersAdapter;
    private List<Order> orderList = new ArrayList<>();
    private DataBaseHelper db;
    private ProgressBar progressBar;
    private boolean isAdminView = false;

    public static OrdersFragment newInstance(boolean isAdminView) {
        OrdersFragment fragment = new OrdersFragment();
        fragment.isAdminView = isAdminView;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_orders, container, false);
        recyclerView = view.findViewById(R.id.recycler_orders);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        ordersAdapter = new OrdersAdapter(getContext(), orderList, isAdminView);
        recyclerView.setAdapter(ordersAdapter);
        db = new DataBaseHelper(getContext());
        progressBar = view.findViewById(R.id.progressBar_orders);
        loadOrders();
        return view;
    }

    private void loadOrders() {
        progressBar.setVisibility(View.VISIBLE);
        new Thread(() -> {
            List<Order> orders;
            if (isAdminView) {
                orders = getAllOrdersWithCustomerDetails();
            } else {
                String userEmail = SharedPrefManager.getInstance(getContext()).readString("email", "");
                orders = db.getAllOrders(userEmail);
            }

            getActivity().runOnUiThread(() -> {
                orderList.clear();
                orderList.addAll(orders);
                ordersAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
            });
        }).start();
    }

    private List<Order> getAllOrdersWithCustomerDetails() {
        List<Order> orders = new ArrayList<>();
        Cursor cursor = db.getAllOrdersWithCustomerDetails();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int orderId = cursor.getInt(cursor.getColumnIndex("order_id"));
                @SuppressLint("Range") String customerName = cursor.getString(cursor.getColumnIndex("first_name")) + " " +
                        cursor.getString(cursor.getColumnIndex("last_name"));
                @SuppressLint("Range") String orderDetails = cursor.getString(cursor.getColumnIndex("order_details"));
                @SuppressLint("Range") String orderDate = cursor.getString(cursor.getColumnIndex("order_date"));
                @SuppressLint("Range") int imageId = cursor.getInt(cursor.getColumnIndex("image_id"));

                orders.add(new Order( orderId, orderDetails, orderDate,imageId,customerName));
            } while (cursor.moveToNext());
            cursor.close();
        }
        return orders;
    }
}
