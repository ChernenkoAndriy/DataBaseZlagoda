package com.example.demo.views.components;

import com.example.demo.views.viewmanagers.EmployeeFactory;
import com.example.demo.views.viewmanagers.EmployeeValidator;
import com.example.demo.views.viewmanagers.MEService;
import com.vaadin.flow.component.dialog.Dialog;

import javax.management.openmbean.KeyAlreadyExistsException;

public class AddEmployeeForm extends EmployeeForm{
    public AddEmployeeForm(MEService MEService, Dialog EmployeeDialog) {
        super(MEService);
        saveButton.addClickListener(event -> {
            if (EmployeeValidator.validateFields(nameField, surnameField, cityField, streetField, zipcode, salaryField, phoneField, rolechooser, dateofBirth, dateofStart)) {
                try {
                    MEService.addEmployee(EmployeeFactory.createEmployee(nameField, surnameField, patronymic, rolechooser, salaryField, dateofBirth, dateofStart, phoneField, cityField, streetField, zipcode));
                    EmployeeDialog.close();
                }catch (KeyAlreadyExistsException e){
                    phoneField.setInvalid(true);
                    phoneField.setErrorMessage(e.getMessage());
                }

            }
        });
    }
}
