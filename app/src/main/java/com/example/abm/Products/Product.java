package com.example.abm.Products;

import com.example.abm.R;

public class Product {

    private String color_name;
    private int image;
    private String price;
    private String quantity;
    private String description;

    public Product(String color_name, int image, String price, String quantity) {
        this.color_name = color_name;
        this.image = image;
        this.price = price;
        this.quantity = quantity;
    }

    //constructor with  no image, when a manager add a new product
    public Product(String color_name, String price, String quantity, String description) {
        this.color_name = color_name;
        this.price = price;
        this.quantity = quantity;
        this.description = description;
        this.image = R.drawable.canni7; //defualt image
    }

    public Product(String color_name, String price, String quantity, String description, int image) {
        this.color_name = color_name;
        this.price = price;
        this.quantity = quantity;
        this.description = description;
        this.image = image;
    }

    public Product(String color_name) {
        this.color_name = color_name;
    }
    public Product() {}

    public String getPrice() {return price; }
    public String setPrice(String price) {return this.price = price; }

    public String getQuantity() {return quantity; }
    public String setQuantity(String quantity) {return this.quantity = quantity; }

    public String getDescription() {return description; }
    public String setDescription(String description) {return this.description = description; }

    public String getColorName() {
        return color_name;
    }
    public void setColorName(String color_name) {
        this.color_name = color_name;
    }

    public int getImage() {
        return image;
    }
    public void setImage(int image) {
        this.image = image;
    }

    //to string method
    @Override
    public String toString() {
        return "Product{" +
                "color_name='" + color_name + '\'' +
                ", image=" + image +
                ", price='" + price + '\'' +
                ", quantity='" + quantity + '\'' +
                ", description='" + description + '\'' +
                '}';
    }




}
