package com.example.demo.views.services;

public class UnsoldProductInfo {
    private String productName;
    private double sellingPrice;
    private int productsNumber;
    private String categoryName;
    private String upc;

    public UnsoldProductInfo() {}

    public UnsoldProductInfo(String productName, double sellingPrice, int productsNumber, String categoryName, String upc) {
        this.productName = productName;
        this.sellingPrice = sellingPrice;
        this.productsNumber = productsNumber;
        this.categoryName = categoryName;
        this.upc = upc;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(double sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public int getProductsNumber() {
        return productsNumber;
    }

    public void setProductsNumber(int productsNumber) {
        this.productsNumber = productsNumber;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getUpc() {
        return upc;
    }

    public void setUpc(String upc) {
        this.upc = upc;
    }
}
