package com.example.demo.views.components.CustomerPageComponents;

import com.example.demo.views.events.UpdateEvent;
import com.example.demo.views.repositories.database_entities.CustomerCard;
import com.example.demo.views.services.CustomerService;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.example.demo.views.repositories.database_entities.Employee;

import java.util.List;

public class CustomerToolbar extends HorizontalLayout {
    private TextField filterField = new TextField();
    private NumberField percentFilter = new NumberField();
    private Button printButton = new Button("Print");
    private Button exportButton = new Button("Export");
    private Button addButton = new Button("Add");

    public CustomerToolbar() {

        percentFilter.setStepButtonsVisible(true);
        percentFilter.setMin(0);
        percentFilter.setMax(100);
        percentFilter.setStepButtonsVisible(true);
        percentFilter.setStep(1.0);
        percentFilter.setPlaceholder("Percent");

        addButton.addThemeName("primary");
        addButton.setWidth("20%");
        exportButton.addThemeName("primary");
        exportButton.setWidth("15%");
        printButton.setWidth("15%");
        printButton.addThemeName("primary");

        HorizontalLayout rightLayout = new HorizontalLayout(percentFilter, exportButton, printButton);
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

        filterField.addValueChangeListener(e -> fireEvent(new UpdateCustomerEvent(this)));
        percentFilter.addValueChangeListener(e -> fireEvent(new UpdateCustomerEvent(this)));

        add(filterField);
    }

    public List<CustomerCard> getAllByfilters(CustomerService service) {
        String surname = null;
        String phone = null;

        String filterText = filterField.getValue();
        if (filterText != null && !filterText.trim().isEmpty()) {
            filterText = filterText.trim();
            if (filterText.matches(".*\\d.*")) {
                phone = filterText;
                if (!phone.startsWith("+")) {
                    phone = "+" + phone;
                }
            } else {
                surname = filterText;
            }
        }

        Double percentValue = percentFilter.getValue();
        Integer percent = null;
             percent = (percentValue != null) ? percentValue.intValue() : null;
        return service.getAllBy(surname, phone, percent);
    }



    public void addUpdateListener(ComponentEventListener<UpdateCustomerEvent> listener) {
        addListener(UpdateCustomerEvent.class, listener);
    }

    public Button getAddButton() {
        return addButton;
    }

    public static class UpdateCustomerEvent extends UpdateEvent<CustomerToolbar> {
        public UpdateCustomerEvent(CustomerToolbar source) {
            super(source);
        }
    }
}
