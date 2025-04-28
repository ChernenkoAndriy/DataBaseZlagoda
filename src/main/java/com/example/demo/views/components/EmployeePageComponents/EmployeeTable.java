package com.example.demo.views.components.EmployeePageComponents;

import com.example.demo.services.EmployeeService;
import com.vaadin.flow.component.grid.Grid;
import com.example.demo.repositories.database_entities.Employee;
import com.vaadin.flow.component.grid.contextmenu.GridContextMenu;
import com.vaadin.flow.component.grid.contextmenu.GridMenuItem;
import com.vaadin.flow.component.grid.contextmenu.GridSubMenu;

import java.util.HashMap;
import java.util.Map;
//табличка що створюється
//приймає колекцію типу employee
public class EmployeeTable extends Grid<Employee> {
    private Map<String, Grid.Column<Employee>> columnsMap = new HashMap<>();
    public EmployeeTable(EmployeeService service) {
        setItems(service.getAllEntities());
        configureColumns();
        configureColumnVisibilityContextMenu();
    }
    protected void configureColumns() {
        columnsMap.put("Surname", addColumn(Employee::getEmpl_surname).setHeader("Surname").setSortable(true));
        columnsMap.put("Name", addColumn(Employee::getEmpl_name).setHeader("Name").setSortable(true));
        columnsMap.put("Patronymic", addColumn(Employee::getEmpl_patronymic).setHeader("Patronymic").setSortable(true));
        columnsMap.put("Role", addColumn(Employee::getEmpl_role).setHeader("Role").setSortable(true));
        columnsMap.put("Birthdate", addColumn(Employee::getDate_of_birth).setHeader("Birthdate").setSortable(true));
        columnsMap.put("Date of start", addColumn(Employee::getDate_of_start).setHeader("Date of start"));
        columnsMap.put("City", addColumn(Employee::getCity).setHeader("City"));
        columnsMap.put("Street", addColumn(Employee::getStreet).setHeader("Street"));
        columnsMap.put("Phone", addColumn(Employee::getPhone_number).setHeader("Phone"));
        columnsMap.put("Salary", addColumn(Employee::getSalary).setHeader("Salary"));
        columnsMap.put("Zip", addColumn(Employee::getZip_code).setHeader("Zip"));

        setColumnReorderingAllowed(true);
        getColumns().forEach(col -> col.setAutoWidth(true));
        setMultiSort(true);
    }
    private void configureColumnVisibilityContextMenu() {
        GridContextMenu<Employee> contextMenu = new GridContextMenu<>(this);
        GridMenuItem<Employee> toggleColumn = contextMenu.addItem("Set column visibility");
        GridSubMenu<Employee> exportSubMenu = toggleColumn.getSubMenu();
        for(String c : this.getColumnNames()){
            GridMenuItem<Employee> item = exportSubMenu.addItem(c, e -> toggleColumnVisibility(c));
            item.setCheckable(true);
            item.setChecked(true);
            ;
        }
    }
    public String[] getColumnNames() {
        return columnsMap.keySet().toArray(new String[0]);
    }
    private void toggleColumnVisibility(String columnName) {
        Grid.Column<Employee> column = columnsMap.get(columnName);
        if (column != null) {
            column.setVisible(!column.isVisible());
        }
    }
}
