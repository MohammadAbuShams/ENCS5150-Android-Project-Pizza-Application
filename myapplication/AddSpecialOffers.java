package com.example.myapplication;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AddSpecialOffers extends Fragment {

    private RecyclerView recyclerViewSpecialOffers;
    private DataBaseHelper dataBaseHelper;
    private SpecialOfferAdapter specialOfferAdapter;

    public AddSpecialOffers() {
        // Required empty public constructor
    }

    public static AddSpecialOffers newInstance() {
        return new AddSpecialOffers();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_special_offers, container, false);

        recyclerViewSpecialOffers = view.findViewById(R.id.recyclerView_special_offers);
        dataBaseHelper = new DataBaseHelper(getContext());

        // Retrieve special offers from the database
        List<SpecialOffer> specialOffers = dataBaseHelper.getAllSpecialOffers();

        specialOfferAdapter = new SpecialOfferAdapter(getContext(), specialOffers);
        recyclerViewSpecialOffers.setAdapter(specialOfferAdapter);
        recyclerViewSpecialOffers.setLayoutManager(new LinearLayoutManager(getContext()));

        return view;
    }

    private class SpecialOfferAdapter extends RecyclerView.Adapter<SpecialOfferAdapter.OfferViewHolder> {
        private List<SpecialOffer> specialOfferList;
        private LayoutInflater inflater;

        public SpecialOfferAdapter(Context context, List<SpecialOffer> specialOfferList) {
            this.inflater = LayoutInflater.from(context);
            this.specialOfferList = specialOfferList;
        }

        @NonNull
        @Override
        public OfferViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = inflater.inflate(R.layout.special_offer_item, parent, false);
            return new OfferViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull OfferViewHolder holder, int position) {
            SpecialOffer currentOffer = specialOfferList.get(position);
            holder.offerName.setText(currentOffer.getOfferName());

            List<Pizza> offerPizzas = dataBaseHelper.getPizzasByOfferId(currentOffer.getId());
            OfferPizzaAdapter pizzaAdapter = new OfferPizzaAdapter(getContext(), offerPizzas);
            holder.recyclerViewOfferPizzas.setAdapter(pizzaAdapter);
        }

        @Override
        public int getItemCount() {
            return specialOfferList != null ? specialOfferList.size() : 0;
        }

        class OfferViewHolder extends RecyclerView.ViewHolder {
            private TextView offerName;
            private RecyclerView recyclerViewOfferPizzas;

            public OfferViewHolder(View itemView) {
                super(itemView);
                offerName = itemView.findViewById(R.id.offerName);
                recyclerViewOfferPizzas = itemView.findViewById(R.id.recyclerView_offer_pizzas);
                recyclerViewOfferPizzas.setLayoutManager(new LinearLayoutManager(getContext()));
            }
        }
    }

    private class OfferPizzaAdapter extends RecyclerView.Adapter<OfferPizzaAdapter.PizzaViewHolder> {
        private List<Pizza> pizzaList;
        private LayoutInflater inflater;
        private final Context context;

        public OfferPizzaAdapter(Context context, List<Pizza> pizzaList) {
            this.inflater = LayoutInflater.from(context);
            this.pizzaList = pizzaList;
            this.context = context;
        }

        @NonNull
        @Override
        public PizzaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = inflater.inflate(R.layout.pizza_item, parent, false);
            return new PizzaViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull PizzaViewHolder holder, int position) {
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
}
