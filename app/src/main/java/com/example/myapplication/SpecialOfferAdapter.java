package com.example.myapplication;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SpecialOfferAdapter extends RecyclerView.Adapter<SpecialOfferAdapter.SpecialOfferViewHolder> {

    private List<SpecialOffer> specialOfferList;
    private LayoutInflater inflater;
    private Context context;

    public SpecialOfferAdapter(Context context, List<SpecialOffer> specialOfferList) {
        this.context = context;
        this.specialOfferList = specialOfferList;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public SpecialOfferViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.special_offer_item, parent, false);
        return new SpecialOfferViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SpecialOfferViewHolder holder, int position) {
        SpecialOffer currentOffer = specialOfferList.get(position);
        holder.offerName.setText(currentOffer.getOfferName());
        holder.offerPeriod.setText(currentOffer.getOfferPeriod());

        // Load pizzas associated with the offer
        DataBaseHelper db = new DataBaseHelper(context);
        List<Pizza> pizzas = db.getPizzasByOfferId(currentOffer.getId());
        Log.d("SpecialOfferAdapter", "Offer ID: " + currentOffer.getId() + " has " + pizzas.size() + " pizzas");

        // Remove duplicates explicitly using a Set
        Set<Integer> pizzaIds = new HashSet<>();
        List<Pizza> uniquePizzas = new ArrayList<>();
        for (Pizza pizza : pizzas) {
            if (!pizzaIds.contains(pizza.getID())) {
                uniquePizzas.add(pizza);
                pizzaIds.add(pizza.getID());
            }
        }

        // Calculate the updated price per pizza based on the total offer price proportionally
        double totalPrice = currentOffer.getTotalPrice();
        double totalBasePrice = 0;
        for (Pizza pizza : uniquePizzas) {
            totalBasePrice += pizza.getPrice();
        }

        if (totalBasePrice > 0) {
            for (Pizza pizza : uniquePizzas) {
                double proportion = pizza.getPrice() / totalBasePrice;
                double updatedPrice = totalPrice * proportion;
                pizza.setPrice(updatedPrice);
            }
        } else {
            Log.d("SpecialOfferAdapter", "Total base price is zero, cannot update prices for offer ID: " + currentOffer.getId());
        }

        PizzaAdapter pizzaAdapter = new PizzaAdapter(context, uniquePizzas);
        holder.recyclerViewOfferPizzas.setAdapter(pizzaAdapter);
        holder.recyclerViewOfferPizzas.setLayoutManager(new LinearLayoutManager(context));
    }

    @Override
    public int getItemCount() {
        return specialOfferList.size();
    }

    class SpecialOfferViewHolder extends RecyclerView.ViewHolder {
        private TextView offerName;
        private TextView offerPeriod;
        private RecyclerView recyclerViewOfferPizzas;

        public SpecialOfferViewHolder(@NonNull View itemView) {
            super(itemView);
            offerName = itemView.findViewById(R.id.offerName);
            offerPeriod = itemView.findViewById(R.id.offerPeriod);
            recyclerViewOfferPizzas = itemView.findViewById(R.id.recyclerView_offer_pizzas);
        }
    }
}
