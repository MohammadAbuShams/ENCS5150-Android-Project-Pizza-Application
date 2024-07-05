package com.example.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SpecialOffersFragment extends Fragment {

    private RecyclerView recyclerViewSpecialOffers;
    private DataBaseHelper dataBaseHelper;
    private SpecialOfferAdapter specialOfferAdapter;

    public SpecialOffersFragment() {
        // Required empty public constructor
    }

    public static SpecialOffersFragment newInstance() {
        return new SpecialOffersFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_special_offers, container, false);

        recyclerViewSpecialOffers = view.findViewById(R.id.recyclerView_special_offers);
        dataBaseHelper = new DataBaseHelper(getContext());

        // Retrieve special offers from the database
        List<SpecialOffer> specialOffers = dataBaseHelper.getAllSpecialOffers();
        Log.d("SpecialOffersFragment", "Retrieved " + specialOffers.size() + " special offers");

        specialOfferAdapter = new SpecialOfferAdapter(getContext(), specialOffers);
        recyclerViewSpecialOffers.setAdapter(specialOfferAdapter);
        recyclerViewSpecialOffers.setLayoutManager(new LinearLayoutManager(getContext()));

        return view;
    }
}
