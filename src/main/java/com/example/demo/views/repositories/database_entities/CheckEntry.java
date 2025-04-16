package com.example.demo.views.repositories.database_entities;

import java.math.BigDecimal;
import java.util.UUID;

public class CheckEntry{
    private int amountOfProducts;
    private BigDecimal selling_price;
    private UUID store_product;
    private UUID check_number;
    private String productName;
    private BigDecimal product_selling_price;

    public CheckEntry(int amountOfProducts,
                      BigDecimal selling_price,
                      UUID store_product,
                      UUID check_number,
                      BigDecimal product_selling_price,
                      String productName) {
        this.amountOfProducts = amountOfProducts;
        this.selling_price = selling_price;
        this.store_product = store_product;
        this.check_number = check_number;
        this.product_selling_price = product_selling_price;
        this.productName = productName;
    }

    public CheckEntry() {

    }

    public int getAmountOfProducts() {
        return amountOfProducts;
    }

    public void setAmountOfProducts(int amountOfProducts) {
        this.amountOfProducts = amountOfProducts;
    }

    public BigDecimal getSelling_price() {
        return selling_price;
    }

    public void setSelling_price(BigDecimal selling_price) {
        this.selling_price = selling_price;
    }

    public UUID getStore_product() {
        return store_product;
    }

    public void setStore_product(UUID store_product) {
        this.store_product = store_product;
    }

    public UUID getCheck_number() {
        return check_number;
    }

    public void setCheck_number(UUID check_number) {
        this.check_number = check_number;
    }
    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public BigDecimal getProduct_selling_price() {
        return product_selling_price;
    }

    public void setProduct_selling_price(BigDecimal product_selling_price) {
        this.product_selling_price = product_selling_price;
    }


}
