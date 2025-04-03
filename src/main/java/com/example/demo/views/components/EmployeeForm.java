package com.example.demo.views.components;

import com.example.demo.views.viewmanagers.MEService;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.button.Button;

public class EmployeeForm extends VerticalLayout {
    protected MEService MEService;
    protected final TextField nameField = new TextField("Name");
    protected final TextField surnameField = new TextField("Surname");
    protected final TextField patronymic = new TextField("Patronymic");
    protected final ComboBox<String> rolechooser = new ComboBox<>("Role", "Cashier", "Manager");
    protected final TextField salaryField = new TextField("Salary");
    protected final DatePicker dateofBirth = new DatePicker("Date of birth");
    protected final DatePicker dateofStart = new DatePicker("Date of start");
    protected final TextField phoneField = new TextField("Phone Number");
    protected final TextField cityField = new TextField("City");
    protected final TextField streetField = new TextField("Street");
    protected final TextField zipcode = new TextField("Zip Code");
    protected final Button saveButton = new Button("Save");

    public EmployeeForm(MEService MEService) {
        this.MEService = MEService;
        setJustifyContentMode(JustifyContentMode.CENTER);
        customizeElements();

        FormLayout formLayout = new FormLayout(nameField, surnameField, patronymic, rolechooser, dateofBirth, dateofStart, phoneField, cityField, streetField, zipcode, salaryField);
        add(formLayout, saveButton);


    }

    private void customizeElements() {
        dateofBirth.setRequired(true);
        dateofStart.setRequired(true);
        configureTextField(nameField, "Name", 1, 50, true);
        configureTextField(surnameField, "Surname", 1, 50, true);
        configureTextField(patronymic, "Patronymic", 0, 50, false);
        configureTextField(cityField, "City", 1, 50, true);
        configureTextField(streetField, "Street", 1, 50, true);
        configureTextField(zipcode, "Zip Code", 5, 9, true);
        configureTextField(salaryField, "Salary", 1, 15, true);
        salaryField.setPattern("^\\d{1,13}(\\.\\d{1,4})?$");
        salaryField.setErrorMessage("Salary must be a number with up to 15 digits and up to 4 decimal places.");
        salaryField.setAllowedCharPattern("[0-9.]");
        phoneField.setRequiredIndicatorVisible(true);
        phoneField.setPattern("\\d{12}");
        phoneField.setAllowedCharPattern("[0-9+]");
        phoneField.setMaxLength(13);
        phoneField.setI18n(new TextField.TextFieldI18n()
                .setRequiredErrorMessage("Phone number is required")
                .setPatternErrorMessage("Invalid phone number format"));
        saveButton.setWidth("100%");
        saveButton.addThemeName("primary");
        zipcode.setAllowedCharPattern("[0-9]");
    }

    private void configureTextField(TextField field, String label, int minLength, int maxLength, boolean required) {
        field.setLabel(label);
        field.setMaxLength(maxLength);
        if (required) {
            field.setRequiredIndicatorVisible(true);
        }
        field.setI18n(new TextField.TextFieldI18n()
                .setRequiredErrorMessage(label + " is required")
                .setMaxLengthErrorMessage("Maximum length is " + maxLength + " characters"));
    }
}