package com.example.demo.views.components.CustomerPageComponents;

import com.example.demo.views.repositories.database_entities.CustomerCard;
import com.example.demo.views.services.CustomerService;
import com.vaadin.flow.component.grid.Grid;

public class CustomerTable extends Grid<CustomerCard> {

    public CustomerTable(CustomerService service) {
        super(CustomerCard.class, false);
        addColumn(CustomerCard::getCustSurname).setHeader("Surname");
        addColumn(CustomerCard::getCustName).setHeader("Name");
        addColumn(CustomerCard::getCustPatronymic).setHeader("Patronymic");
        addColumn(CustomerCard::getPhoneNumber).setHeader("Phone");
        addColumn(CustomerCard::getCity).setHeader("City");
        addColumn(CustomerCard::getStreet).setHeader("Street");
        addColumn(CustomerCard::getZipCode).setHeader("Zip code");
        addColumn(CustomerCard::getPercent).setHeader("Percent");

        setItems(service.getAllEntities());
        setSelectionMode(SelectionMode.SINGLE);
    }
}
