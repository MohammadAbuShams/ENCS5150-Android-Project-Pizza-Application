package com.example.myapplication;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PizzaJsonParser {
    private static final Map<String, String> pizzaSizes = new HashMap<>();
    public static final Map<String, Double> pizzaPrices = new HashMap<>();

    private static final List<String> chickenPizzas = Arrays.asList("Tandoori Chicken Pizza", "BBQ Chicken Pizza", "Buffalo Chicken Pizza");
    private static final List<String> veggiePizzas = Arrays.asList("Margarita", "New York Style", "Vegetarian Pizza", "Mushroom Truffle Pizza", "Pesto Chicken Pizza", "Neapolitan");
    private static final List<String> otherPizzas = Arrays.asList("Hawaiian", "Calzone", "Seafood Pizza");
    private static final List<String> beefPizzas = Arrays.asList("Pepperoni");

    static {
        // Define static sizes for each pizza
        pizzaSizes.put("Tandoori Chicken Pizza", "Large");
        pizzaSizes.put("BBQ Chicken Pizza", "Medium");
        pizzaSizes.put("Buffalo Chicken Pizza", "Small");
        pizzaSizes.put("Margarita", "Medium");
        pizzaSizes.put("Vegetarian Pizza", "Large");
        pizzaSizes.put("Mushroom Truffle Pizza", "Small");
        pizzaSizes.put("Pesto Chicken Pizza", "Medium");
        pizzaSizes.put("Hawaiian", "Large");
        pizzaSizes.put("Calzone", "Small");
        pizzaSizes.put("Seafood Pizza", "Medium");
        pizzaSizes.put("Pepperoni", "Large");
        pizzaSizes.put("New York Style", "Medium");
        pizzaSizes.put("Neapolitan", "Small");

        // Define static prices for each pizza
        pizzaPrices.put("Tandoori Chicken Pizza", 25.99);
        pizzaPrices.put("BBQ Chicken Pizza", 20.99);
        pizzaPrices.put("Buffalo Chicken Pizza", 15.99);
        pizzaPrices.put("Margarita", 18.99);
        pizzaPrices.put("Vegetarian Pizza", 22.99);
        pizzaPrices.put("Mushroom Truffle Pizza", 19.99);
        pizzaPrices.put("Pesto Chicken Pizza", 21.99);
        pizzaPrices.put("Hawaiian", 23.99);
        pizzaPrices.put("Calzone", 17.99);
        pizzaPrices.put("Seafood Pizza", 24.99);
        pizzaPrices.put("Pepperoni", 20.99);
        pizzaPrices.put("New York Style", 19.99);
        pizzaPrices.put("Neapolitan", 16.99);
    }

    public static List<Pizza> getObjectFromJson(String json) {
        List<Pizza> pizzas = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.getJSONArray("types");  // Access the "types" array from the object
            for (int i = 0; i < jsonArray.length(); i++) {
                String name = jsonArray.getString(i);
                Log.d("PizzaJsonParser", "Processing pizza: " + name);

                // Assign static size and price
                String size = pizzaSizes.getOrDefault(name, "Medium");
                double price = pizzaPrices.getOrDefault(name, 19.99);

                // Log the size and price
                Log.d("PizzaJsonParser", "Size: " + size + ", Price: $" + String.format("%.2f", price));

                String category = determineCategory(name);
                int imageResId = getImageResourceId(name);  // Get the resource ID based on the pizza name

                Pizza pizza = new Pizza(
                        pizzas.size(),  // Unique ID for each pizza including size variation
                        name,
                        price,
                        size,
                        category,
                        "Delicious " + category + " pizza",
                        imageResId,
                        false  // Default favorite status
                );
                pizzas.add(pizza);
                Log.d("PizzaJsonParser", "Added pizza: " + pizza.getName() + " with size: " + pizza.getSize() + ", category: " + pizza.getCategory() + ", imageResId: " + imageResId + ", and price: " + pizza.getPrice());
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return pizzas;
    }

    private static int getImageResourceId(String pizzaName) {
        int resourceId;
        switch (pizzaName) {
            case "Tandoori Chicken Pizza":
                resourceId = R.drawable.tandoori;
                break;
            case "BBQ Chicken Pizza":
                resourceId = R.drawable.bbq;
                break;
            case "Buffalo Chicken Pizza":
                resourceId = R.drawable.buffalochickenpizza;
                break;
            case "Margarita":
                resourceId = R.drawable.margarita;
                break;
            case "Vegetarian Pizza":
                resourceId = R.drawable.vegetarianpizza;
                break;
            case "Mushroom Truffle Pizza":
                resourceId = R.drawable.mushroom;
                break;
            case "Pesto Chicken Pizza":
                resourceId = R.drawable.pestochickenpizza;
                break;
            case "Hawaiian":
                resourceId = R.drawable.hawaiian;
                break;
            case "Calzone":
                resourceId = R.drawable.calzone;
                break;
            case "Seafood Pizza":
                resourceId = R.drawable.seafood;
                break;
            case "Pepperoni":
                resourceId = R.drawable.pepperoni;
                break;
            case "New York Style":
                resourceId = R.drawable.newyorkstyle;
                break;
            case "Neapolitan":
                resourceId = R.drawable.neapolitan;
                break;
            default:
                resourceId = R.drawable.defaultpizza;
                break;
        }
        Log.d("PizzaJsonParser", "Assigned resource ID " + resourceId + " for pizza: " + pizzaName);
        return resourceId;
    }

    private static String determineCategory(String pizzaName) {
        if (chickenPizzas.contains(pizzaName)) {
            Log.d("PizzaJsonParser", "Categorized as Chicken: " + pizzaName);
            return "Chicken";
        } else if (veggiePizzas.contains(pizzaName)) {
            Log.d("PizzaJsonParser", "Categorized as Veggies: " + pizzaName);
            return "Veggies";
        } else if (beefPizzas.contains(pizzaName)) {
            Log.d("PizzaJsonParser", "Categorized as Beef: " + pizzaName);
            return "Beef";
        } else if (otherPizzas.contains(pizzaName)) {
            Log.d("PizzaJsonParser", "Categorized as Other: " + pizzaName);
            return "Other";
        } else {
            Log.d("PizzaJsonParser", "Categorized as Special: " + pizzaName);
            return "Special";
        }
    }
}
