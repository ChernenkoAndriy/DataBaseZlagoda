package com.example.demo.views.repositories.database_entities;

import java.math.BigDecimal;
import java.util.UUID;

public class CheckEntry{
    private int amountOfProducts;
    private BigDecimal selling_price;
    private UUID store_product;
    private UUID check_number;
    private String productName;

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    private BigDecimal total;
    public CheckEntry(int amountOfProducts,
                      BigDecimal selling_price,
                      UUID store_product,
                      UUID check_number,
                      String productName) {
        this.amountOfProducts = amountOfProducts;
        this.selling_price = selling_price;
        this.store_product = store_product;
        this.check_number = check_number;
        this.productName = productName;
        this.total = selling_price.multiply(new BigDecimal(amountOfProducts));
    }

    public CheckEntry() {
    }

    public int getAmountOfProducts() {
        return amountOfProducts;
    }

    public void setAmountOfProducts(int amountOfProducts) {
        this.amountOfProducts = amountOfProducts;
        this.total = new BigDecimal(amountOfProducts).multiply(selling_price);
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

}
