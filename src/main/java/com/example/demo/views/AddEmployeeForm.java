package com.example.demo.views;

import com.example.demo.views.viewmanagers.ManagerInitialViewManager;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.data.provider.ListDataProvider;
import database_manegment.database_entities.Employee;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;

//клас що описує формочку для додавання робітника
public class AddEmployeeForm extends VerticalLayout {
    ListDataProvider<Employee> dataProvider;
    ArrayList<Employee> data;
    private TextField nameField;
    private TextField surnameField;
    private TextField patronymic;
    private ComboBox<String> rolechooser;
    private TextField salaryField;
    private DatePicker dateofBirth;
    private DatePicker dateofStart;
    private TextField phoneField;
    private TextField cityField;
    private TextField streetField;
    private TextField zipcode;
    private Button saveButton;

    public AddEmployeeForm(ListDataProvider<Employee> dataProvider, ArrayList<Employee> data, Dialog addEmployeeDialog) {
        this.dataProvider = dataProvider;
        this.data = data;
        setJustifyContentMode(JustifyContentMode.CENTER);
        FormLayout formLayout = new FormLayout();
        nameField = new TextField("Name");
        nameField.setMaxLength(50);
        surnameField = new TextField("Surname");
        surnameField.setMaxLength(50);
        patronymic = new TextField("Patronymic");
        surnameField.setMaxLength(50);
        rolechooser = new ComboBox<>("Role");
        rolechooser.setItems("Cashier", "Manager");
        salaryField = new TextField("Salary");
        dateofBirth = new DatePicker("Date of birth");
        dateofStart = new DatePicker("Date of start");
        phoneField = new TextField("Phone Number");
        phoneField.setMaxLength(13);
        cityField = new TextField("City");
        cityField.setMaxLength(50);
        streetField = new TextField("Street");
        streetField.setMaxLength(50);
        zipcode = new TextField("Zip Code");
        zipcode.setMaxLength(9);
        saveButton = new Button("Save");
        saveButton.setWidth("100%");
        saveButton.addThemeName("primary");
        formLayout.add(nameField, surnameField, patronymic, rolechooser,
                dateofBirth, dateofStart, phoneField, cityField,
                streetField, zipcode, salaryField);
        add(formLayout);
        add(saveButton);
        saveButton.addClickListener(event -> {
            ManagerInitialViewManager.addEmployee(getEmployee(), dataProvider, data);
            addEmployeeDialog.close();
        });

    }

    private Employee getEmployee(){
        return new Employee(
                null,
                surnameField.getValue(),
                nameField.getValue(),
                patronymic.getValue(),
                rolechooser.getValue(),
                BigDecimal.valueOf(Double.parseDouble(salaryField.getValue())),
                Date.valueOf(dateofBirth.getValue()),
                Date.valueOf(dateofStart.getValue()),
                phoneField.getValue(),
                cityField.getValue(),
                streetField.getValue(),
                zipcode.getValue()
        );
    }
}
