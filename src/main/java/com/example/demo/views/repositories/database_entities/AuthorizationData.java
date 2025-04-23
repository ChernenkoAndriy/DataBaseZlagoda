package com.example.demo.views.repositories.database_entities;

import java.util.UUID;

public class AuthorizationData {
    private String login;
    private String password;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    private UUID id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    private String name;
    private String surname;
    private String role;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    private String phone;

    public AuthorizationData() {}

    public AuthorizationData(String login, String password, String role, String name, String surname, String phone) {
        this.login = login;
        this.password = password;
        this.role = role;
        this.name=name;
        this.surname=surname;
        this.phone=phone;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
