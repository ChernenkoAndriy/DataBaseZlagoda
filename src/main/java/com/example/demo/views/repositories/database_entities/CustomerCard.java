package com.example.demo.views.repositories.database_entities;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
public class CustomerCard implements IEntity<UUID> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID cardNumber;

    private String custSurname;
    private String custName;
    private String custPatronymic;
    private String phoneNumber;
    private String city;
    private String street;
    private String zipCode;
    private int percent;

    public CustomerCard() {}

    @Override
    public UUID getId() { return cardNumber; }

    @Override
    public void setId(UUID id) { this.cardNumber = id; }

    // Геттери/сеттери
    public String getCustSurname() { return custSurname; }
    public void setCustSurname(String custSurname) { this.custSurname = custSurname; }

    public String getCustName() { return custName; }
    public void setCustName(String custName) { this.custName = custName; }

    public String getCustPatronymic() { return custPatronymic; }
    public void setCustPatronymic(String custPatronymic) { this.custPatronymic = custPatronymic; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getStreet() { return street; }
    public void setStreet(String street) { this.street = street; }

    public String getZipCode() { return zipCode; }
    public void setZipCode(String zipCode) { this.zipCode = zipCode; }

    public int getPercent() { return percent; }
    public void setPercent(int percent) { this.percent = percent; }
}
