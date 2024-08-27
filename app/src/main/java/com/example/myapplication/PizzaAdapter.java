package com.example.myapplication;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PizzaAdapter extends RecyclerView.Adapter<PizzaAdapter.PizzaViewHolder> {
    private List<Pizza> pizzaList;
    private LayoutInflater inflater;
    private final Context context;
    private DataBaseHelper db;
    private String userEmail;

    public PizzaAdapter(Context context, List<Pizza> pizzaList) {
        this.inflater = LayoutInflater.from(context);
        this.pizzaList = pizzaList;
        this.context = context;
        this.db = new DataBaseHelper(context);
        this.userEmail = SharedPrefManager.getInstance(context).readString("email", "");
    }

    @Override
    public PizzaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.pizza_item, parent, false);
        return new PizzaViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PizzaViewHolder holder, int position) {
        Pizza currentPizza = pizzaList.get(position);
        holder.pizzaName.setText(currentPizza.getName());
        holder.pizzaSize.setText(currentPizza.getSize());
        holder.pizzaPrice.setText(String.format("$%.2f", currentPizza.getPrice()));
        holder.pizzaDescription.setText(currentPizza.getDescription());
        holder.pizzaImage.setImageResource(currentPizza.getImgPizza());
        Log.d("PizzaAdapter", "Setting image for pizza: " + currentPizza.getName() + " with resource ID: " + currentPizza.getImgPizza());

        updateFavoriteIcon(holder.imgFav, currentPizza.isFavorite());

        holder.imgFav.setOnClickListener(v -> {
            boolean isFavorite = !currentPizza.isFavorite();
            currentPizza.setFavorite(isFavorite);

            Log.d("PizzaFavorite", "Pizza " + currentPizza.getName() + " is now " + (isFavorite ? "a favorite" : "not a favorite"));

            toggleFavorite(userEmail, currentPizza);
            updateFavoriteIcon(holder.imgFav, isFavorite);

            notifyItemChanged(holder.getAdapterPosition());
        });
        holder.orderButton.setOnClickListener(v -> {
            showOrderDialog(currentPizza);
        });
    }

    private void updateFavoriteIcon(ImageButton button, boolean isFavorite) {
        if (isFavorite) {
            button.setImageResource(R.drawable.ic_favorite);
        } else {
            button.setImageResource(R.drawable.ic_favorite_border);
        }
    }

    private void toggleFavorite(String email, Pizza pizza) {
        if (pizza.isFavorite()) {
            db.insertFavorite(email, pizza.getID());
        } else {
            db.removeFavorite(email, pizza.getID());
        }
        Log.d("PizzaAdapter", "Toggled favorite state for pizza ID: " + pizza.getID());
    }

    private void showOrderDialog(Pizza pizza) {
        OrderDialogFragment orderDialogFragment = new OrderDialogFragment();
        Bundle args = new Bundle();
        args.putDouble("base_price", pizza.getPrice());
        args.putString("base_size", pizza.getSize());
        args.putInt("image_id", pizza.getImgPizza());
        orderDialogFragment.setArguments(args);

        orderDialogFragment.setOnOrderSubmitListener((size, price, quantity) -> {
            String orderDetails = "Pizza: " + pizza.getName() + ", Size: " + size + ", Quantity: " + quantity + ", Total Price: " + price;
            String orderDate = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new java.util.Date());
            db.insertOrder(userEmail, orderDetails, orderDate, pizza.getImgPizza(), pizza.getID());
            Log.d("PizzaAdapter", "Order placed: " + orderDetails);
        });

        orderDialogFragment.show(((FragmentActivity) context).getSupportFragmentManager(), "OrderDialog");
    }

    public void setPizzas(List<Pizza> newList) {
        this.pizzaList = newList;
        notifyDataSetChanged();  // Notify the adapter about the updated list
    }

    @Override
    public int getItemCount() {
        return pizzaList != null ? pizzaList.size() : 0;
    }

    class PizzaViewHolder extends RecyclerView.ViewHolder {
        private ImageView pizzaImage;
        private TextView pizzaName;
        private TextView pizzaSize;
        private TextView pizzaPrice;
        private TextView pizzaDescription;
        ImageButton imgFav;
        Button orderButton;

        public PizzaViewHolder(View itemView) {
            super(itemView);
            pizzaImage = itemView.findViewById(R.id.pizzaImage);
            imgFav = itemView.findViewById(R.id.imgFav);
            pizzaName = itemView.findViewById(R.id.pizzaName);
            pizzaSize = itemView.findViewById(R.id.pizzaSize);
            pizzaPrice = itemView.findViewById(R.id.pizzaPrice);
            pizzaDescription = itemView.findViewById(R.id.pizzaDescription);
            orderButton = itemView.findViewById(R.id.orderButton);
        }
    }
}
