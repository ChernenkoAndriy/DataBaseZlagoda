package com.example.demo.views.components.CheckPageComponents;

import com.example.demo.views.repositories.database_entities.Check;
import com.example.demo.views.services.CheckService;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.contextmenu.GridContextMenu;
import com.vaadin.flow.component.grid.contextmenu.GridMenuItem;
import com.vaadin.flow.component.grid.contextmenu.GridSubMenu;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class CheckTable extends Grid<Check> {
    private Map<String, Column<Check>> columnsMap = new HashMap<>();

    public CheckTable(CheckService service) {
        setItems(service.getAllEntities());
        configureColumns();
        configureColumnVisibilityContextMenu();
    }

    protected void configureColumns() {
        columnsMap.put("Cashier surname", addColumn(check -> check.getCashier().getEmpl_surname())
                .setHeader("Cashier surname").setSortable(true));

        columnsMap.put("Cashier name", addColumn(check -> check.getCashier().getEmpl_name())
                .setHeader("Cashier name").setSortable(true));

        columnsMap.put("Cashier phone", addColumn(check -> check.getCashier().getPhone_number())
                .setHeader("Cashier phone").setSortable(true));

         columnsMap.put("Customer surname", addColumn(check -> check.getCustomer().getCustSurname()).setHeader("Customer surname").setSortable(true));
         columnsMap.put("Customer name", addColumn(check -> check.getCustomer().getCustName()).setHeader("Customer name").setSortable(true));
         columnsMap.put("Customer phone", addColumn(check -> check.getCustomer().getPhoneNumber()).setHeader("Customer phone").setSortable(true));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm", new Locale("uk"));
        columnsMap.put("Time printed", addColumn(check ->
                check.getPrint_date() != null ? check.getPrint_date().format(formatter) : ""
        ).setHeader("Time printed").setSortable(true));

        columnsMap.put("Vat", addColumn(Check::getVat)
                .setHeader("Vat").setSortable(true));

        columnsMap.put("Discount", addColumn(check -> check.getCustomer().getPercent())
                .setHeader("Discount").setSortable(true));

        columnsMap.put("Sum final", addColumn(Check::getSum_final)
                .setHeader("Sum final").setSortable(true));

        setColumnReorderingAllowed(true);
        getColumns().forEach(col -> col.setAutoWidth(true));
        setMultiSort(true);
    }

    private void configureColumnVisibilityContextMenu() {
        GridContextMenu<Check> contextMenu = new GridContextMenu<>(this);
        GridMenuItem<Check> toggleColumn = contextMenu.addItem("Set column visibility");
        GridSubMenu<Check> exportSubMenu = toggleColumn.getSubMenu();

        for (String c : this.getColumnNames()) {
            GridMenuItem<Check> item = exportSubMenu.addItem(c, e -> toggleColumnVisibility(c));
            item.setCheckable(true);
            item.setChecked(true);
        }
    }

    public String[] getColumnNames() {
        return columnsMap.keySet().toArray(new String[0]);
    }

    private void toggleColumnVisibility(String columnName) {
        Column<Check> column = columnsMap.get(columnName);
        if (column != null) {
            column.setVisible(!column.isVisible());
        }
    }
}
