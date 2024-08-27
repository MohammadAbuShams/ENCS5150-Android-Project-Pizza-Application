package com.example.myapplication;


import java.util.List;
import java.util.ArrayList;

public class SpecialOffer {
    private int id;
    private String offerName;
    private String pizzaType;
    private String pizzaSize;
    private String offerPeriod;
    private double totalPrice;
    private List<Pizza> pizzas;

    public SpecialOffer(int id, String offerName, String pizzaType, String pizzaSize, String offerPeriod, double totalPrice) {
        this.id = id;
        this.offerName = offerName;
        this.pizzaType = pizzaType;
        this.pizzaSize = pizzaSize;
        this.offerPeriod = offerPeriod;
        this.totalPrice = totalPrice;
        this.pizzas = new ArrayList<>();
    }

    // Getters and setters for each field
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOfferName() {
        return offerName;
    }

    public void setOfferName(String offerName) {
        this.offerName = offerName;
    }

    public String getPizzaType() {
        return pizzaType;
    }

    public void setPizzaType(String pizzaType) {
        this.pizzaType = pizzaType;
    }

    public String getPizzaSize() {
        return pizzaSize;
    }

    public void setPizzaSize(String pizzaSize) {
        this.pizzaSize = pizzaSize;
    }

    public String getOfferPeriod() {
        return offerPeriod;
    }

    public void setOfferPeriod(String offerPeriod) {
        this.offerPeriod = offerPeriod;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public List<Pizza> getPizzas() {
        return pizzas;
    }

    public void setPizzas(List<Pizza> pizzas) {
        this.pizzas = pizzas;
    }
}
