package com.example.demo.views;

public class CustomerInfo {
    private String surname;
    private String name;
    private String city;
    private double totalSpent;

    public CustomerInfo(String surname, String name, String city, double totalSpent) {
        this.surname = surname;
        this.name = name;
        this.city = city;
        this.totalSpent = totalSpent;
    }

    public String getSurname() {
        return surname;
    }

    public String getName() {
        return name;
    }

    public String getCity() {
        return city;
    }

    public double getTotalSpent() {
        return totalSpent;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setTotalSpent(double totalSpent) {
        this.totalSpent = totalSpent;
    }
}
