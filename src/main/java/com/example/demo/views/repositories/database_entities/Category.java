package com.example.demo.views.repositories.database_entities;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.checkerframework.common.aliasing.qual.Unique;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.sql.SQLException;

public class Category implements IEntity<Integer>{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int category_number;
    private String category_name;

    public Category(int category_number, String category_name) {
        this.category_number = category_number;
        this.category_name = category_name;
    }
    public int getCategory_number() {
        return category_number;
    }

    public void setCategory_number(int category_number) {
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
