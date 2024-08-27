package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.OrderViewHolder> {
    private Context context;
    private List<Order> orderList;
    private DataBaseHelper db;
    private boolean isAdminView;

    public OrdersAdapter(Context context, List<Order> orderList, boolean isAdminView) {
        this.context = context;
        this.orderList = orderList;
        this.db = new DataBaseHelper(context);
        this.isAdminView = isAdminView;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.order_item, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order currentOrder = orderList.get(position);

        String price = currentOrder.getOrderDetails().split("Total Price: ")[1].split(",")[0];
        holder.orderPrice.setText("Total Price: $" + String.format("%.2f", Double.parseDouble(price)));

        if (isAdminView) {
            holder.customerName.setText(currentOrder.getCustomerName());
            holder.customerName.setVisibility(View.VISIBLE);
            holder.orderDetails.setText(formatOrderDetails(currentOrder.getOrderDetails()));
        } else {
            holder.customerName.setVisibility(View.GONE);
            holder.orderDetails.setText(formatOrderDetails(currentOrder.getOrderDetails()));
            holder.cancelButton.setVisibility(View.VISIBLE);
            holder.cancelButton.setOnClickListener(v -> {
                db.deleteOrder(currentOrder.getOrderId());
                orderList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, orderList.size());
            });
            holder.itemView.setOnClickListener(v -> {
                // Handle the click to show order details
                showOrderDetails(currentOrder);
            });
        }

        holder.orderDate.setText(currentOrder.getOrderDate());
        holder.orderImage.setImageResource(currentOrder.getImageId());


    }

    private String formatOrderDetails(String orderDetails) {
        // Split the order details and format
        String[] detailsArray = orderDetails.split(", ");
        StringBuilder formattedDetails = new StringBuilder();
        for (String detail : detailsArray) {
            if (detail.contains("Pizza:")) {
                formattedDetails.append(detail).append("\n");
            } else if (detail.contains("Size:")) {
                formattedDetails.append(detail).append("\n");
            } else if (detail.contains("Quantity:")) {
                formattedDetails.append(detail).append("\n");
            }
        }
        return formattedDetails.toString();
    }

    private void showOrderDetails(Order order) {
        OrderDetailsFragment fragment = OrderDetailsFragment.newInstance(order);
        ((FragmentActivity) context).getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView customerName;
        TextView orderDetails;
        TextView orderDate;
        TextView orderPrice;
        ImageView orderImage;
        Button cancelButton;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            customerName = itemView.findViewById(R.id.customerName);
            orderDetails = itemView.findViewById(R.id.orderDetails);
            orderDate = itemView.findViewById(R.id.orderDate);
            orderPrice = itemView.findViewById(R.id.orderPrice);
            orderImage = itemView.findViewById(R.id.orderImage);
            cancelButton = itemView.findViewById(R.id.cancelButton);
        }
    }
}
