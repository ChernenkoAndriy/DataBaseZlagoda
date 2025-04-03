package com.example.demo.views.viewmanagers;

import database_manegment.database_entities.Employee;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;

import java.math.BigDecimal;
import java.sql.Date;

public class EmployeeFactory {
    public static Employee createEmployee(TextField nameField, TextField surnameField, TextField patronymic,
                                          ComboBox<String> rolechooser, TextField salaryField, DatePicker dateofBirth,
                                          DatePicker dateofStart, TextField phoneField, TextField cityField,
                                          TextField streetField, TextField zipcode) {
        return new Employee(
                null,
                surnameField.getValue().trim(),
                nameField.getValue().trim(),
                patronymic.getValue().trim().isEmpty() ? null : patronymic.getValue().trim(),
                rolechooser.getValue(),
                new BigDecimal(salaryField.getValue().trim()),
                Date.valueOf(dateofBirth.getValue()),
                Date.valueOf(dateofStart.getValue()),
                phoneField.getValue().trim(),
                cityField.getValue().trim(),
                streetField.getValue().trim(),
                zipcode.getValue().trim()
        );
    }
}