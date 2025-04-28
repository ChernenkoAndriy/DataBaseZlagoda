package com.example.demo.repositories.database_entities;

public interface IEntity<TPK> {
    TPK getId();
    void setId(TPK id);

}
