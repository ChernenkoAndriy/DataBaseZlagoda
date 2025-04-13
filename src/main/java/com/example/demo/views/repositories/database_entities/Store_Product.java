package com.example.demo.views.repositories.database_entities;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.UUID;

public class Store_Product implements IEntity<UUID>{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID UPC;
    private UUID UPC_prom;
    private int id_product;
    private BigDecimal selling_price;
    private int product_number;
    private boolean promotional_product;

    public Store_Product(UUID UPC, UUID UPC_prom, int id_product, BigDecimal selling_price, int product_number, boolean promotional_product) {
        this.UPC = UPC;
        this.UPC_prom = UPC_prom;
        this.id_product = id_product;
        this.selling_price = selling_price;
        this.product_number = product_number;
        this.promotional_product = promotional_product;
    }
    public UUID getUPC_prom() {
        return UPC_prom;
    }

    public void setUPC_prom(UUID UPC_prom) {
        this.UPC_prom = UPC_prom;
    }

    public int getId_product() {
        return id_product;
    }

    public void setId_product(int id_product) {
        this.id_product = id_product;
    }

    public BigDecimal getSelling_price() {
        return selling_price;
    }

    public void setSelling_price(BigDecimal selling_price) {
        this.selling_price = selling_price;
    }

    public int getProduct_number() {
        return product_number;
    }

    public void setProduct_number(int product_number) {
        this.product_number = product_number;
    }

    public boolean isPromotional_product() {
        return promotional_product;
    }

    public void setPromotional_product(boolean promotional_product) {
        this.promotional_product = promotional_product;
    }

    @Override
    public UUID getId() {
        return UPC;
    }

    @Override
    public void setId(UUID id) {
        this.UPC = id;
    }
}
