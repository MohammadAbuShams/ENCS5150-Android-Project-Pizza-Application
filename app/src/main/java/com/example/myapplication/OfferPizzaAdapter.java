package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class OfferPizzaAdapter extends RecyclerView.Adapter<OfferPizzaAdapter.PizzaViewHolder> {
    private List<Pizza> pizzaList;
    private LayoutInflater inflater;
    private final Context context;

    public OfferPizzaAdapter(Context context, List<Pizza> pizzaList) {
        this.inflater = LayoutInflater.from(context);
        this.pizzaList = pizzaList;
        this.context = context;
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
        holder.pizzaPrice.setText(String.format("$%.2f", currentPizza.getPrice()));
        holder.pizzaDescription.setText(currentPizza.getDescription());
        holder.pizzaImage.setImageResource(currentPizza.getImgPizza());
    }

    @Override
    public int getItemCount() {
        return pizzaList != null ? pizzaList.size() : 0;
    }

    class PizzaViewHolder extends RecyclerView.ViewHolder {
        private ImageView pizzaImage;
        private TextView pizzaName;
        private TextView pizzaPrice;
        private TextView pizzaDescription;

        public PizzaViewHolder(View itemView) {
            super(itemView);
            pizzaImage = itemView.findViewById(R.id.pizzaImage);
            pizzaName = itemView.findViewById(R.id.pizzaName);
            pizzaPrice = itemView.findViewById(R.id.pizzaPrice);
            pizzaDescription = itemView.findViewById(R.id.pizzaDescription);
        }
    }
}
