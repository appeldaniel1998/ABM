package com.example.abm.Products;

import com.example.abm.R;

/**
 * Product class
 */

public class Product {

    private String colorName;
    private int image;
    private String price;
    private String quantity;
    private String description;

    public Product(String color_name, int image) {
        this.colorName = color_name;
        this.image = image;
    }

    //constructor with  no image, when a manager add a new product
    public Product(String color_name, String price, String quantity, String description) {
        this.colorName = color_name;
        this.price = price;
        this.quantity = quantity;
        this.description = description;
        this.image = R.drawable.canni7;
    }

    public Product(String color_name, int image, String price, String quantity) {
        this.colorName = color_name;
        this.price = price;
        this.quantity = quantity;
        this.description = description;
        this.image = image;
    }

    public Product(String color_name, String description, int image, String price, String quantity) {
        this.colorName = color_name;
        this.price = price;
        this.quantity = quantity;
        this.description = description;
        this.image = image;
    }


    public Product(String color_name) {
        this.colorName = color_name;
    }
    public Product() {
    }
    public String getPrice() {return price;}
    public void setPrice(String price) {this.price = price;}

    public String getQuantity() {return quantity;}
    public void setQuantity(String quantity) {this.quantity = quantity;}

    public String getDescription() {return description;}
    public void setDescription(String description) {this.description = description;}

    public String getColorName() {
        return colorName;
    }

    public void setColorName(String colorName) {
        this.colorName = colorName;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String toString() {
        return "Product{" +
                "color_name='" + colorName + '\'' +
                ", image=" + image +
                ", price=" + price +
                ", quantity=" + quantity +
                ", description=" + description +
                '}';
    }




}
