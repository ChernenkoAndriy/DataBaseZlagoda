package com.example.demo.views;

import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.button.Button;
//клас що описує формочку для додавання робітника
public class AddEmployeeForm extends VerticalLayout {
    private TextField nameField;
    private TextField surnameField;
    private TextField patronymic;
    private ComboBox<String> rolechooser;
    private DatePicker dateofBirth;
    private DatePicker dateofStart;
    private TextField phoneField;
    private TextField cityField;
    private TextField streetField;
    private TextField zipcode;
    private Button saveButton;

    public AddEmployeeForm() {
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
                streetField, zipcode);
        add(formLayout);
        add(saveButton);
    }
}
