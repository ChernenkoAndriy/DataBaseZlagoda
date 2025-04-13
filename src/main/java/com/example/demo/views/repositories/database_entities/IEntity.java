package com.example.demo.views.repositories.database_entities;

public interface IEntity<TPK> {
    TPK getId();
    void setId(TPK id);

}
