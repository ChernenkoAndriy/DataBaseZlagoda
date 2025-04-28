package com.example.demo.views.components.CustomerPageComponents;

import com.example.demo.repositories.database_entities.CustomerCard;
import com.example.demo.services.CustomerService;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.contextmenu.GridContextMenu;
import com.vaadin.flow.component.grid.contextmenu.GridMenuItem;
import com.vaadin.flow.component.grid.contextmenu.GridSubMenu;

import java.util.HashMap;
import java.util.Map;

public class CustomerTable extends Grid<CustomerCard> {
    private final Map<String, Column<CustomerCard>> columnsMap = new HashMap<>();

    public CustomerTable(CustomerService service) {
        setItems(service.getAllEntities());
        configureColumns();
        configureColumnVisibilityContextMenu();
        setSelectionMode(SelectionMode.SINGLE);
    }

    protected void configureColumns() {
        columnsMap.put("Surname", addColumn(CustomerCard::getCustSurname).setHeader("Surname").setSortable(true));
        columnsMap.put("Name", addColumn(CustomerCard::getCustName).setHeader("Name").setSortable(true));
        columnsMap.put("Patronymic", addColumn(CustomerCard::getCustPatronymic).setHeader("Patronymic").setSortable(true));
        columnsMap.put("Phone", addColumn(CustomerCard::getPhoneNumber).setHeader("Phone").setSortable(true));
        columnsMap.put("City", addColumn(CustomerCard::getCity).setHeader("City").setSortable(true));
        columnsMap.put("Street", addColumn(CustomerCard::getStreet).setHeader("Street").setSortable(true));
        columnsMap.put("Zip code", addColumn(CustomerCard::getZipCode).setHeader("Zip code").setSortable(true));
        columnsMap.put("Percent", addColumn(CustomerCard::getPercent).setHeader("Percent").setSortable(true));

        setColumnReorderingAllowed(true);
        getColumns().forEach(col -> col.setAutoWidth(true));
        setMultiSort(true);
    }

    private void configureColumnVisibilityContextMenu() {
        GridContextMenu<CustomerCard> contextMenu = new GridContextMenu<>(this);
        GridMenuItem<CustomerCard> toggleColumn = contextMenu.addItem("Set column visibility");
        GridSubMenu<CustomerCard> exportSubMenu = toggleColumn.getSubMenu();

        for (String c : getColumnNames()) {
            GridMenuItem<CustomerCard> item = exportSubMenu.addItem(c, e -> toggleColumnVisibility(c));
            item.setCheckable(true);
            item.setChecked(true);
        }
    }

    private String[] getColumnNames() {
        return columnsMap.keySet().toArray(new String[0]);
    }

    private void toggleColumnVisibility(String columnName) {
        Column<CustomerCard> column = columnsMap.get(columnName);
        if (column != null) {
            column.setVisible(!column.isVisible());
        }
    }
}
