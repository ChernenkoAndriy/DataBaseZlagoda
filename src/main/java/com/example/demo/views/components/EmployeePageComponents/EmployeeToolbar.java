package com.example.demo.views.components.EmployeePageComponents;

import com.example.demo.views.events.UpdateEvent;
import com.example.demo.views.services.EmployeeService;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.example.demo.views.repositories.database_entities.Employee;
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
    //фільтрувати будемо все по всім параметрам завжди, тобто все робиться одним методом
    //СЕРВІСАМИ ЗАЙМАТИСЬ НЕ ПОТРІБНО, ЇХ СТВОРЮВАТИ БУДУ Я
    //в методі буде запит який буде приєднувати умови в залежності від того чи є параметр, чи він null
    //дивитись приклад EmployeeRepository getAllBy
    //цей метод по суті збирає всі дані з фільтраційних полів і вибирає все що треба
    //викликається подією update
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
    //подія оновлення таблиці
    public static class UpdateEmployeeEvent extends UpdateEvent<EmployeeToolbar> {
        public UpdateEmployeeEvent(EmployeeToolbar employeeToolbar) {
            super(employeeToolbar);
        }
    }

}
