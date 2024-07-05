package com.example.myapplication;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViewAllOrdersFragment extends Fragment {
    private RecyclerView recyclerView;
    private OrdersAdapter adapter;
    private List<Order> orders = new ArrayList<>();
    private DataBaseHelper dataBaseHelper;
    private TextView summaryTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_all_orders, container, false);
        recyclerView = view.findViewById(R.id.recyclerView_orders);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        dataBaseHelper = new DataBaseHelper(getContext());
        adapter = new OrdersAdapter(getContext(), orders, true); // Set true for isAdminView
        recyclerView.setAdapter(adapter);
        summaryTextView = view.findViewById(R.id.summaryTextView);

        // Fetch data from database
        fetchOrders();

        return view;
    }

    private void fetchOrders() {
        new Thread(() -> {
            List<Order> orders = getAllOrdersWithCustomerDetails();
            getActivity().runOnUiThread(() -> {
                this.orders.clear();
                this.orders.addAll(orders);
                adapter.notifyDataSetChanged();
                updateSummary(); // Update the summary after fetching orders
            });
        }).start();
    }

    private List<Order> getAllOrdersWithCustomerDetails() {
        List<Order> orders = new ArrayList<>();
        Cursor cursor = dataBaseHelper.getAllOrdersWithCustomerDetails();
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

    private void updateSummary() {
        Map<String, Integer> orderCountMap = new HashMap<>();
        Map<String, Double> incomeMap = new HashMap<>();
        double totalIncome = 0;

        for (Order order : orders) {
            String pizzaType = order.getOrderDetails().split(",")[0].replace("Pizza: ", "");
            double price = Double.parseDouble(order.getOrderDetails().split("Total Price: ")[1].split(",")[0]);

            orderCountMap.put(pizzaType, orderCountMap.getOrDefault(pizzaType, 0) + 1);
            incomeMap.put(pizzaType, incomeMap.getOrDefault(pizzaType, 0.0) + price);
            totalIncome += price;
        }

        StringBuilder summary = new StringBuilder();
        for (String pizzaType : orderCountMap.keySet()) {
            summary.append(pizzaType)
                    .append(": Orders = ").append(orderCountMap.get(pizzaType))
                    .append(", Income = $").append(String.format("%.2f", incomeMap.get(pizzaType)))
                    .append("\n");
        }
        summary.append("Total Income: $").append(String.format("%.2f", totalIncome));

        summaryTextView.setText(summary.toString());
    }
}
