package com.example.demo.views.repositories.database_entities;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

public class Check  implements IEntity<UUID>{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID check_number;
    private UUID id_employee;
    private UUID card_number;
    private LocalDateTime print_date;
    private BigDecimal sum_total;
    private BigDecimal vat;
    ArrayList<Store_Product> products;

    public Check(ArrayList<Store_Product> products, UUID check_number, UUID id_employee, UUID card_number, LocalDateTime print_date, BigDecimal sum_total, BigDecimal vat) {
        this.products = products;
        this.check_number = check_number;
        this.id_employee = id_employee;
        this.card_number = card_number;
        this.print_date = print_date;
        this.sum_total = sum_total;
        this.vat = vat;
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

    @Override
    public UUID getId() {
        return check_number;
    }

    @Override
    public void setId(UUID id) {
        this.check_number = id;
    }
}
