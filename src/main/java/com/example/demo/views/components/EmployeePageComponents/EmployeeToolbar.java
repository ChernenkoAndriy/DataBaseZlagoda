package com.example.demo.views.components.EmployeePageComponents;

import com.example.demo.views.events.UpdateEvent;
import com.example.demo.services.EmployeeService;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.example.demo.repositories.database_entities.Employee;

import java.util.List;

public class EmployeeToolbar extends HorizontalLayout{
    private TextField filterField = new TextField();;
    private ComboBox<String> rolefilter = new ComboBox<>("");
    private Button printButton = new Button("Print");
    private Button exportButton= new Button("Export");
    public Button getAddButton() {
        return addButton;
    }
    private Button addButton= new Button("Add");

    public Button getCheckStatistic() {
        return checkStatistic;
    }

    private Button checkStatistic = new Button("Check statistics");

    public CheckNumberForm getCheckNumberForm() {
        return checkNumberForm;
    }

    private CheckNumberForm checkNumberForm = new CheckNumberForm();
    public EmployeeToolbar(List<String> roles){
        addButton.addThemeName("primary");
        addButton.setWidth("20%");
        exportButton.addThemeName("primary");
        exportButton.setWidth("15%");
        printButton.setWidth("15%");
        printButton.addThemeName("primary");
        roles.add("All");
        rolefilter.setItems(roles);
        HorizontalLayout rightLayout = new HorizontalLayout(exportButton, printButton);
        rightLayout.setAlignItems(FlexComponent.Alignment.END);
        rightLayout.setSpacing(true);
        configureComponents();
        addComponentAsFirst(addButton);
        checkStatistic.addThemeName("primary");
        add(checkStatistic);
        add(rightLayout);
    }
    protected void configureComponents() {
        filterField.setPlaceholder("Find employee by name, surname or phone number");
        filterField.setValueChangeMode(ValueChangeMode.LAZY);
        filterField.setWidth("55%");
        filterField.addValueChangeListener(e ->
            fireEvent(new UpdateEmployeeEvent(this)));
        rolefilter.setValue("All");
        rolefilter.setAllowCustomValue(false);
        rolefilter.addValueChangeListener(e ->
            fireEvent(new UpdateEmployeeEvent(this)));
        add(filterField);
        add(rolefilter);
    }
    public List<Employee> getAllByfilters(EmployeeService service) {
        String role = rolefilter.getValue();
        String surname = null;
        String phone = null;
        if ("All".equals(role)) {
            role = null;
        }
        String filterText = filterField.getValue().trim();
        if (!filterText.isEmpty()) {
            if (filterText.matches(".*\\d.*")) {
                phone = filterText;
                if(!phone.startsWith("+"))
                    phone = "+" + phone;
            } else {
                surname = filterText;
            }
        }

        return service.getAllBy(surname, role, phone);
    }
    //метод для зовнішнього створення слухача
    public void addUpdateListener(ComponentEventListener<UpdateEmployeeEvent> listener) {
        addListener(UpdateEmployeeEvent.class, listener);
    }
public void openForm(){
        checkNumberForm.open();;
}
public void updateForm(List<Employee> cashiers){
        checkNumberForm.setItems(cashiers);
}
    public static class UpdateEmployeeEvent extends UpdateEvent<EmployeeToolbar> {
        public UpdateEmployeeEvent(EmployeeToolbar employeeToolbar) {
            super(employeeToolbar);
        }
    }
    private class CheckNumberForm extends Dialog {
        private VerticalLayout layout;
        private List<Employee> employees;

        public CheckNumberForm() {
            setWidth("70%");
            layout = new VerticalLayout();

            // Title for the form
            layout.add(new H1("Check statistics"));

            this.add(layout);
        }

        public void setItems(List<Employee> cashiers) {
            this.employees = cashiers;

            // Clear the layout first
            layout.removeAll();

            // Iterate over each employee and create a set of fields for each
            for (Employee employee : employees) {
                FormLayout formLayout = new FormLayout();

                // Create form fields for each employee's data
                TextField nameField = new TextField("Name");
                nameField.setValue(employee.getEmpl_name());
                nameField.setReadOnly(true); // Set as readonly to prevent editing

                TextField surnameField = new TextField("Surname");
                surnameField.setValue(employee.getEmpl_surname());
                surnameField.setReadOnly(true); // Set as readonly to prevent editing

                TextField phoneField = new TextField("Phone");
                phoneField.setValue(employee.getPhone_number());
                phoneField.setReadOnly(true); // Set as readonly to prevent editing

                TextField checkCountField = new TextField("Check Count");
                checkCountField.setValue(String.valueOf(employee.getNumberOfChecks()));
                checkCountField.setReadOnly(true); // Set as readonly to prevent editing

                TextField soldProductsField = new TextField("Sold Products Amount");
                soldProductsField.setValue(String.valueOf(employee.getTotalAmountOfProducts()));
                soldProductsField.setReadOnly(true); // Set as readonly to prevent editing

                // Add fields to the form layout
                formLayout.add(nameField, surnameField, phoneField, checkCountField, soldProductsField);

                // Add the form layout to the main layout
                layout.add(formLayout);
            }
        }
    }


}
