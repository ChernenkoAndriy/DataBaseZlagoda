package com.example.demo.repositories.database_entities;

import jakarta.persistence.Entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Category implements IEntity<Integer> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer category_number; // Змінили тип на Integer
    private String category_name;

    private Integer amountOfGoods; // Змінили тип на Integer

    public Category() {

    }

    public Integer getAmountOfGoods() {
        return amountOfGoods;
    }

    public void setAmountOfGoods(Integer amountOfGoods) {
        this.amountOfGoods = amountOfGoods;
    }

    public Category(Integer category_number, String category_name, Integer amountOfGoods) { // Змінили тип на Integer
        this.category_number = category_number;
        this.category_name = category_name;
        this.amountOfGoods = amountOfGoods;
    }

    public Integer getCategory_number() { // Змінили тип на Integer
        return category_number;
    }

    public void setCategory_number(Integer category_number) { // Змінили тип на Integer
        this.category_number = category_number;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    @Override
    public Integer getId() {
        return category_number;
    }

    @Override
    public void setId(Integer id) {
        this.category_number = id;
    }
}
