package com.example.demo.views.events;
import com.example.demo.views.components.EmployeePageComponents.EmployeeForm;
import com.example.demo.repositories.database_entities.Employee;
public class EmployeeEvent extends AbstractEvent<EmployeeForm, Employee> {
    protected EmployeeEvent(EmployeeForm source, Employee e) {
        super(source, e, false);
    }
}
