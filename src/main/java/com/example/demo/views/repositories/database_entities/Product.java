package com.example.demo.views.repositories.database_entities;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.sql.SQLException;

public class Product implements IEntity<Integer>{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_product;
    private int category_number;
    private String categoryName;
    private String product_name;
    private String characteristics;
    public Product(){}

    public Product(int id_product, int category_number, String product_name, String characteristics, String categoryName) {
        this.id_product = id_product;
        this.category_number = category_number;
        this.product_name = product_name;
        this.characteristics = characteristics;
        this.categoryName = categoryName;
    }

    public int getId_product() {
        return id_product;
    }

    public void setId_product(int id_product) {
        this.id_product = id_product;
    }

    public int getCategory_number() {
        return category_number;
    }

    public void setCategory_number(int category_number) {
        this.category_number = category_number;
    }
    public void setCategory(Category category){
        this.categoryName = category.getCategory_name();
        this.category_number = category.getId();
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getCharacteristics() {
        return characteristics;
    }

    public void setCharacteristics(String characteristics) {
        this.characteristics = characteristics;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
    public Category getCategory(){
        return new Category(category_number, categoryName, null);
    }

    @Override
    public Integer getId() {
        if(id_product == 0) return null;
    return id_product;
    }

    @Override
    public void setId(Integer id) {
        id_product = id;
    }
}
