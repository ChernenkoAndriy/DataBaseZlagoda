package com.example.demo.views.components;

import com.example.demo.views.viewmanagers.EmployeeValidator;
import com.example.demo.views.viewmanagers.MEService;
import com.vaadin.flow.component.dialog.Dialog;
import database_manegment.database_entities.Employee;

import javax.management.openmbean.KeyAlreadyExistsException;
import java.math.BigDecimal;
import java.util.Optional;

public class EditEmployeeForm extends EmployeeForm{
    public EditEmployeeForm(MEService MEService, Dialog EmployeeDialog, Employee employee) {
        super(MEService);
        initializeFields(employee);
        saveButton.addClickListener(event -> {
            if (EmployeeValidator.validateFields(nameField, surnameField, cityField, streetField, zipcode, salaryField, phoneField, rolechooser, dateofBirth, dateofStart)) {
                employee.setEmpl_name(nameField.getValue());
                employee.setEmpl_surname(surnameField.getValue());
                employee.setEmpl_patronymic(patronymic.getValue());
                employee.setEmpl_role(rolechooser.getValue());
                employee.setSalary(new BigDecimal(salaryField.getValue()));  // Перетворення зарплати в BigDecimal
                employee.setDate_of_birth(java.sql.Date.valueOf(dateofBirth.getValue()));  // Перетворення в java.sql.Date
                employee.setDate_of_start(java.sql.Date.valueOf(dateofStart.getValue()));  // Перетворення в java.sql.Date
                employee.setPhone_number(phoneField.getValue());
                employee.setCity(cityField.getValue());
                employee.setStreet(streetField.getValue());
                employee.setZip_code(zipcode.getValue());
                try{
                MEService.updateEmployee(employee);
                EmployeeDialog.close();
                }catch (KeyAlreadyExistsException e){
                    phoneField.setInvalid(true);
                    phoneField.setErrorMessage(e.getMessage());
                }
            }
        });
    }
    private void initializeFields(Employee employee) {
        nameField.setValue(employee.getEmpl_name());
        surnameField.setValue(employee.getEmpl_surname());
        patronymic.setValue(Optional.ofNullable(employee.getEmpl_patronymic()).orElse(""));
        rolechooser.setValue(employee.getEmpl_role());
        salaryField.setValue(String.valueOf(employee.getSalary()));
        dateofBirth.setValue(employee.getDate_of_birth().toLocalDate());
        dateofStart.setValue(employee.getDate_of_start().toLocalDate());
        phoneField.setValue(employee.getPhone_number());
        cityField.setValue(employee.getCity());
        streetField.setValue(employee.getStreet());
        zipcode.setValue(employee.getZip_code());
    }

}
