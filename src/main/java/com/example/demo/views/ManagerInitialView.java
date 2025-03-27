package com.example.demo.views;

import com.example.demo.views.viewmanagers.ManagerInitialViewManager;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import database_manegment.database_entities.Employee;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

@Route("manager")
//наслідуємось від OverallView а потім просто додаємо необхідні компоненти і перевизначаємо їх
//у методах доступних з батьківського класу
public class ManagerInitialView extends OverallView<Employee> {
    private TextField filterField;
    private Dialog addEmployeeDialog;
    private ComboBox<String> rolefilter;
    private Button searchButton;
    private Button printButton;
    private Button exportButton;
    private Button addButton;

    public ManagerInitialView() {
        super(Employee.class);
        initializeDialogueForm();
    }
    //цей метод потрібен для створення вікна що викликається при натиску add employee
    //він використовує клас AddEmployeeForm і розміщає його у вікні просто
    private void initializeDialogueForm() {
        addEmployeeDialog = new Dialog();
        AddEmployeeForm addEmployeeForm = new AddEmployeeForm(dataProvider, data, addEmployeeDialog);
        addEmployeeForm.setWidthFull();
        addEmployeeDialog.setWidth("70%");
        addEmployeeDialog.setHeight("auto");
        addEmployeeDialog.add(addEmployeeForm);
        addEmployeeDialog.setModal(true);
    }
//тут ми додаємо всі елементи що знаходяться над табличкою
    protected void modifyToolbar() {
        filterField = new TextField();
        filterField.setPlaceholder("Find employee");
        addButton = new Button("Add employee", e -> addEmployeeDialog.open());
        addButton.addThemeName("primary");
        addButton.setWidth("20%");
        searchButton = new Button("Search");
        searchButton.setWidth("15%");
        searchButton.addThemeName("primary");
        rolefilter = new ComboBox<>("", "All", "Managers", "Cashiers");
        rolefilter.setValue("All");
        exportButton = new Button("Export");
        exportButton.addThemeName("primary");
        exportButton.setWidth("15%");
        printButton = new Button("Print");
        printButton.setWidth("15%");
        printButton.addThemeName("primary");
        bar.add(rolefilter, addButton , exportButton, printButton, filterField, searchButton);
    }
//додаємо стовпчики до таблички, об'екти в табличках мусять мати геттери для всіх полів що будуть використані
    @Override
    protected void modifyTable() {
        table.addColumn(Employee::getEmpl_surname).setHeader("Surname");
        table.addColumn(Employee::getEmpl_name).setHeader("Name");
        table.addColumn(Employee::getEmpl_patronymic).setHeader("Patronymic");
        table.addColumn(Employee::getEmpl_role).setHeader("Role");
        table.addColumn(Employee::getDate_of_birth).setHeader("Birthdate");
        table.addColumn(Employee::getDate_of_start).setHeader("Date of start");
        table.addColumn(Employee::getCity).setHeader("City");
        table.addColumn(Employee::getStreet).setHeader("Street");
        table.addColumn(Employee::getSalary).setHeader("Salary");
        table.addColumn(Employee::getZip_code).setHeader("Zip");
        table.addColumn(Employee::getPhone_number).setHeader("Phone");
        dataProvider = ManagerInitialViewManager.fillTheTable(table, data);
    }

}
