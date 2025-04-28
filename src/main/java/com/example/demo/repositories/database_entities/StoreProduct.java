package com.example.demo.repositories.database_entities;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.math.BigDecimal;
import java.util.UUID;

public class StoreProduct implements IEntity<UUID>{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID UPC;
    private UUID UPC_prom;
    private int id_product;
    private BigDecimal selling_price;
    private int products_number;
    private boolean promotional_product;
    private String product;
    public StoreProduct(UUID UPC, UUID UPC_prom, int id_product, BigDecimal selling_price, int products_number, boolean promotional_product, String product) {
        this.UPC = UPC;
        this.UPC_prom = UPC_prom;
        this.id_product = id_product;
        this.selling_price = selling_price;
        this.products_number = products_number;
        this.promotional_product = promotional_product;
        this.product=product;
    }

    public StoreProduct() {

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

    public int getProducts_number() {
        return products_number;
    }

    public void setProducts_number(int products_number) {
        this.products_number = products_number;
    }

    public boolean isPromotional_product() {
        return promotional_product;
    }

    public void setPromotional_product(boolean promotional_product) {
        this.promotional_product = promotional_product;
    }

    public UUID getUPC() {
        return UPC;
    }

    public void setUPC(UUID UPC) {
        this.UPC = UPC;
    }
    @Override
    public UUID getId() {
        return UPC;
    }
    @Override
    public void setId(UUID id) {
        this.UPC = id;
    }
    public String getProduct() {
        if(isPromotional_product())
                return product + " prom";
        return product;
    }
    public void setProduct(String product) {
        if(product.endsWith(" prom"))
            product = product.substring(0, product.length()-6);
        this.product = product;
    }

}
