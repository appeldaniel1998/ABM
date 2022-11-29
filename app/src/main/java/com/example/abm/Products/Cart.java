package com.example.abm.Products;

public class Cart {
    private String color_name;
    private int image;
    private int quantity;

    public Cart(String color_name, int image, int quantity) {
        this.color_name = color_name;
        this.image = image;
        this.quantity = quantity;
    }

    public Cart() {
    }

    public String getProductColor() {
        return color_name;
    }
    public void setProductColor(String color_name) {
        this.color_name = color_name;
    }
    public int getProductImage() {
        return image;
    }
    public void setProductImage(int image) {
        this.image = image;
    }

    public int getQuantity() {
        return quantity;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String toString() {
        return "Cart{" +
                "color_name='" + color_name + '\'' +
                ", image=" + image +
                ", quantity=" + quantity +
                '}';
    }

}
