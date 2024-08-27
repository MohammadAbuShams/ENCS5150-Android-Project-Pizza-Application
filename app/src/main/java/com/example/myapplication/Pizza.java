package com.example.myapplication;

public class Pizza {
    private int id;
    private String name;
    private double price;
    private String size;
    private String category;
    private String description;
    private int imgPizza;
    private boolean isFavorite;  // Field to track favorite status

    // Default constructor
    public Pizza() {
    }

    // Parameterized constructor
    public Pizza(int id, String name, double price, String size, String category,
                 String description, int imgPizza, boolean isFavorite) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.size = size;
        this.category = category;
        this.description = description;
        this.imgPizza = imgPizza;
        this.isFavorite = isFavorite; // Initialize with the provided favorite status
    }

    // Getters and Setters
    public int getID() {
        return id;
    }

    public void setID(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getImgPizza() {
        return imgPizza;
    }

    public void setImgPizza(int imgPizza) {
        this.imgPizza = imgPizza;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    // Optionally, override the toString method for easy logging and debugging
    @Override
    public String toString() {
        return "Pizza{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", size='" + size + '\'' +
                ", category='" + category + '\'' +
                ", description='" + description + '\'' +
                ", imgPizza=" + imgPizza +
                ", isFavorite=" + isFavorite +
                '}';
    }
}
