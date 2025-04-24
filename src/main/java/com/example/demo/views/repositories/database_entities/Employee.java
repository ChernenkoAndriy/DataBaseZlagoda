package com.example.demo.views.repositories.database_entities;

import com.example.demo.views.repositories.database_entities.IEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Employee implements IEntity<UUID> {
    public UUID getId_employee() {
        return id_employee;
    }

    public void setId_employee(UUID id_employee) {
        this.id_employee = id_employee;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
private UUID id_employee;
private String empl_surname;
private String empl_name;
private String empl_patronymic;
private String empl_role;
private BigDecimal salary;
private LocalDate date_of_birth;
private LocalDate date_of_start;
private String phone_number;
private String city;
private String street;
private String zip_code;
    public Employee() {
    }
    public Employee(UUID id_employee, String empl_surname, String empl_name, String empl_patronymic, String empl_role, BigDecimal salary, LocalDate date_of_birth, LocalDate date_of_start, String phone_number, String city, String street, String zip_code) {
        this.id_employee = id_employee;
        this.empl_surname = empl_surname;
        this.empl_name = empl_name;
        this.empl_patronymic = empl_patronymic;
        this.empl_role = empl_role;
        this.salary = salary;
        this.date_of_birth = date_of_birth;
        this.date_of_start = date_of_start;
        this.phone_number = phone_number;
        this.city = city;
        this.street = street;
        this.zip_code = zip_code;
    }
    public String getEmpl_surname() {
        return empl_surname;
    }

    public void setEmpl_surname(String empl_surname) {
        this.empl_surname = empl_surname;
    }

    public String getEmpl_name() {
        return empl_name;
    }

    public void setEmpl_name(String empl_name) {
        this.empl_name = empl_name;
    }

    public String getEmpl_patronymic() {
        return empl_patronymic;
    }

    public void setEmpl_patronymic(String empl_patronymic) {

            this.empl_patronymic = empl_patronymic;
    }

    public String getEmpl_role() {
        return empl_role;
    }

    public void setEmpl_role(String empl_role) {
        this.empl_role = empl_role;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }

    public LocalDate getDate_of_birth() {
        return date_of_birth;
    }

    public void setDate_of_birth(LocalDate date_of_birth) {
        this.date_of_birth = date_of_birth;
    }

    public LocalDate getDate_of_start() {
        return date_of_start;
    }

    public void setDate_of_start(LocalDate date_of_start) {
        this.date_of_start = date_of_start;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        if(phone_number !=null) {
            if (phone_number.charAt(0) != '+') {
                phone_number = "+" + phone_number;
            }
        }
        this.phone_number = phone_number;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getZip_code() {
        return zip_code;
    }

    public void setZip_code(String zip_code) {
        this.zip_code = zip_code;
    }


    @Override
    public UUID getId() {
        return id_employee;
    }

    @Override
    public void setId(UUID id) {
this.id_employee= id;
    }
}
