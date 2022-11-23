package com.example.abm.Products;

public class Product {

private String color_name;
    private int image;

    public Product(String color_name, int image) {
        this.color_name = color_name;
        this.image = image;
    }

    public String getColor_name() {
        return color_name;
    }

    public void setColor_name(String color_name) {
        this.color_name = color_name;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }


}
