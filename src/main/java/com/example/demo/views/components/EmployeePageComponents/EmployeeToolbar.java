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
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.converter.StringToLongConverter;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.example.demo.repositories.database_entities.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
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
public void setService(EmployeeService service){
        checkNumberForm.setService(service);
}
    public static class UpdateEmployeeEvent extends UpdateEvent<EmployeeToolbar> {
        public UpdateEmployeeEvent(EmployeeToolbar employeeToolbar) {
            super(employeeToolbar);
        }
    }
    private class CheckNumberForm extends Dialog {
        private final VerticalLayout layout;
        private final H1 title = new H1("Check Statistics");
        private final ComboBox<Employee> cashierChooser = new ComboBox<>("Select Cashier");
        private final TextField checkCountField = new TextField("Check Count");
        private final TextField soldProductsField = new TextField("Sold Products Amount");
        private final Binder<Employee> binder = new Binder<>(Employee.class);
        private List<Employee> employees = new ArrayList<>();
        private EmployeeService service;

        public CheckNumberForm() {
            setWidth("600px");

            layout = new VerticalLayout();
            layout.setWidth("100%");
            layout.setPadding(true);
            layout.setSpacing(true);

            title.getStyle()
                    .set("text-align", "center")
                    .set("width", "100%");

            cashierChooser.setItemLabelGenerator(e ->
                    e.getPhone_number() + " " + e.getEmpl_surname() + " " + e.getEmpl_name()
            );
            cashierChooser.setWidthFull();
            cashierChooser.addValueChangeListener(event -> {
                Employee selected = event.getValue();
                if (selected != null) {
                    setEmployee(selected);
                }
            });

            checkCountField.setReadOnly(true);
            checkCountField.setWidthFull();

            soldProductsField.setReadOnly(true);
            soldProductsField.setWidthFull();

            binder.forField(checkCountField)
                    .withConverter(new StringToLongConverter("Must be a number"))
                    .bind(
                            emp -> emp.getNumberOfChecks() != null ? emp.getNumberOfChecks() : 0L,
                            (emp, value) -> {} // read-only, setter пустий
                    );

            binder.forField(soldProductsField)
                    .withConverter(new StringToLongConverter("Must be a number"))
                    .bind(
                            emp -> emp.getTotalAmountOfProducts() != null ? emp.getTotalAmountOfProducts() : 0L,
                            (emp, value) -> {} // read-only, setter пустий
                    );

            layout.add(title, cashierChooser, checkCountField, soldProductsField);
            add(layout);
        }

        public void setService(EmployeeService service) {
            this.service = service;
        }

        public void setEmployee(Employee employee) {
            employee = service.getCashiersWithNumberOfChecks(employee.getId()).getFirst();
            binder.setBean(employee);
        }

        public void setItems(List<Employee> cashiers) {
            this.employees = cashiers != null ? cashiers : new ArrayList<>();
            cashierChooser.setItems(this.employees);
            if (!this.employees.isEmpty()) {
                cashierChooser.setValue(this.employees.getFirst());
            }
        }
    }


}
