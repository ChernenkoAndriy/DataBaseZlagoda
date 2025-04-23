package com.example.demo.views.repositories.database_entities;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class Check implements IEntity<UUID>{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID check_number;
    private UUID id_employee;
    private UUID card_number;
    private LocalDateTime print_date;
    private BigDecimal sum_total;
    private BigDecimal vat;
    private Employee cashier;
    private CustomerCard customer;
    private List<CheckEntry> goods;
    public Check() {

    }
    public Check(UUID check_number,
                 UUID id_employee,
                 UUID card_number,
                 LocalDateTime print_date,
                 BigDecimal sum_total,
                 BigDecimal vat) {
        this.check_number = check_number;
        this.id_employee = id_employee;
        this.card_number = card_number;
        this.print_date = print_date;
        this.sum_total = sum_total;
        this.vat = vat;

    }
    @Override
    public UUID getId() {
        return check_number;
    }

    @Override
    public void setId(UUID id) {
        this.check_number = id;
    }
    public UUID getCheck_number() {
        return check_number;
    }

    public void setCheck_number(UUID check_number) {
        this.check_number = check_number;
    }

    public UUID getId_employee() {
        return id_employee;
    }

    public void setId_employee(UUID id_employee) {
        this.id_employee = id_employee;
    }

    public UUID getCard_number() {
        return card_number;
    }

    public void setCard_number(UUID card_number) {
        this.card_number = card_number;
    }

    public LocalDateTime getPrint_date() {
        return print_date;
    }

    public void setPrint_date(LocalDateTime print_date) {
        this.print_date = print_date;
    }

    public BigDecimal getSum_total() {
        return sum_total;
    }

    public void setSum_total(BigDecimal sum_total) {
        this.sum_total = sum_total;
    }

    public BigDecimal getVat() {
        return vat;
    }

    public void setVat(BigDecimal vat) {
        this.vat = vat;
    }

    public Employee getCashier() {
        return cashier;
    }
    public CustomerCard getCustomer() {
        return customer;
    }
    public String getCashierSurname(){
        return cashier.getEmpl_surname();
    }
    public String getCashierName(){
        return cashier.getEmpl_name();
    }
    public String getCashierPhone(){
        return cashier.getPhone_number();
    }
    public String getCustomerSurname(){
        return customer.getCustSurname();
    }
    public String getCustomerName(){
        return customer.getCustSurname();
    }
    public String getCustomerPhone(){
        return customer.getCustSurname();
    }
    public void setCashierSurname(String surname) {
        if (cashier != null) {
            cashier.setEmpl_surname(surname);
        }
    }

    public void setCashierName(String name) {
        if (cashier != null) {
            cashier.setEmpl_name(name);
        }
    }

    public void setCashierPhone(String phone) {
        if (cashier != null) {
            cashier.setPhone_number(phone);
        }
    }

    public void setCustomerSurname(String surname) {
        if (customer != null) {
            customer.setCustSurname(surname);
        }
    }

    public void setCustomerName(String name) {
        if (customer != null) {
            customer.setCustName(name);
        }
    }

    public void setCustomerPhone(String phone) {
        if (customer != null) {
            customer.setPhoneNumber(phone);
        }
    }
    public void setCustomer(CustomerCard customer) {
        this.customer = customer;
        if(customer != null) {
            setCard_number(customer.getCardNumber());
            setCustomerPhone(customer.getPhoneNumber());
            setCustomerName(customer.getCustName());
            setCustomerSurname(customer.getCustSurname());
        }
    }
    public void setCashier(Employee cashier) {
        this.cashier = cashier;
        if(cashier != null){
            setCashierName(cashier.getEmpl_name());
            setCashierPhone(cashier.getPhone_number());
            setCustomerSurname(cashier.getEmpl_surname());
            setId_employee(cashier.getId_employee());
        }
    }

    public List<CheckEntry> getGoods() {
        return goods;
    }
    public void setGoods(List<CheckEntry> goods) {
        this.goods = goods;
    }
}
