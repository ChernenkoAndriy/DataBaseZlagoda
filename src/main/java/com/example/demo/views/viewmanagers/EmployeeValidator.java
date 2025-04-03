package com.example.demo.views.viewmanagers;

import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;

public class EmployeeValidator {
    public static boolean validateFields(TextField nameField, TextField surnameField, TextField cityField,
                                         TextField streetField, TextField zipcode, TextField salaryField,
                                         TextField phoneField, ComboBox<String> rolechooser,
                                         DatePicker dateofBirth, DatePicker dateofStart) {
        boolean isValid = true;

        if (!validateTextField(nameField)) isValid = false;
        if (!validateTextField(surnameField)) isValid = false;
        if (!validateTextField(cityField)) isValid = false;
        if (!validateTextField(streetField)) isValid = false;
        if (!validateTextField(zipcode)) isValid = false;
        if (!validateTextField(salaryField)) isValid = false;

        if (phoneField.getValue().trim().isEmpty() || !phoneField.getValue().matches("\\d{12}")) {
            phoneField.setInvalid(true);
            phoneField.setErrorMessage("Invalid phone number");
            isValid = false;
        } else {
            phoneField.setInvalid(false);
        }

        if (rolechooser.isEmpty()) {
            rolechooser.setInvalid(true);
            rolechooser.setErrorMessage("Role is required");
            isValid = false;
        } else {
            rolechooser.setInvalid(false);
        }

        if (dateofBirth.isEmpty()) {
            dateofBirth.setInvalid(true);
            dateofBirth.setErrorMessage("Date of birth is required");
            isValid = false;
        } else {
            dateofBirth.setInvalid(false);
        }

        if (dateofStart.isEmpty()) {
            dateofStart.setInvalid(true);
            dateofStart.setErrorMessage("Date of start is required");
            isValid = false;
        } else {
            dateofStart.setInvalid(false);
        }

        return isValid;
    }

    private static boolean validateTextField(TextField field) {
        if (field.getValue().trim().isEmpty()) {
            field.setInvalid(true);
            field.setErrorMessage(field.getLabel() + " is required");
            return false;
        }
        field.setInvalid(false);
        return true;
    }
}