package com.example.demo.views.components.CheckPageComponents;

import com.example.demo.views.events.UpdateEvent;
import com.example.demo.repositories.database_entities.Check;
import com.example.demo.services.CheckService;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;

import java.time.LocalDateTime;
import java.util.List;

public class CheckToolbar extends VerticalLayout {
    private final Button printButton = new Button("Print");
    private final Button exportButton = new Button("Export");
    private final Button addButton = new Button("Add");

    private final TextField findCashier = new TextField();
    private final TextField findCustomer = new TextField();

    private final DateTimePicker fromPicker = new DateTimePicker();
    private final DateTimePicker toPicker = new DateTimePicker();

    public CheckToolbar() {
        configureComponents();
        HorizontalLayout topLayout = new HorizontalLayout(findCashier, findCustomer, addButton, exportButton, printButton);
        topLayout.setWidthFull();
        topLayout.setSpacing(true);
        topLayout.setAlignItems(Alignment.END);
        topLayout.setJustifyContentMode(JustifyContentMode.BETWEEN);
        HorizontalLayout dateLayout = new HorizontalLayout(fromPicker, toPicker);
        dateLayout.setWidthFull();
        dateLayout.setSpacing(true);
        this.setSpacing(false);

        setWidthFull();
        setSpacing(true);
        setPadding(false);
        setAlignItems(Alignment.START);

        add(topLayout, dateLayout);

        // Додаємо слухачів на зміни
        attachValueChangeListeners();
    }

    protected void configureComponents() {
        addButton.addThemeName("primary");
        addButton.setWidth("10%");

        exportButton.addThemeName("primary");
        exportButton.setWidth("10%");

        printButton.addThemeName("primary");
        printButton.setWidth("10%");

        findCashier.setPlaceholder("Find by cashier’s surname or phone");
        findCashier.setClearButtonVisible(true);
        findCashier.setWidth("30%");

        findCustomer.setPlaceholder("Find by customer’s surname or phone");
        findCustomer.setClearButtonVisible(true);
        findCustomer.setWidth("30%");

        fromPicker.setLabel("From");
        toPicker.setLabel("To");

        fromPicker.setWidth("30%");
        toPicker.setWidth("30%");
        fromPicker.setMax(LocalDateTime.now());
        fromPicker.setMin(LocalDateTime.now().minusYears(100));
        toPicker.setMax(LocalDateTime.now());
        toPicker.setMin(LocalDateTime.now().minusYears(100));
    }

    private void attachValueChangeListeners() {
        findCashier.addValueChangeListener(e -> fireEvent(new UpdateCheckEvent(this)));
        findCustomer.addValueChangeListener(e -> fireEvent(new UpdateCheckEvent(this)));

        fromPicker.addValueChangeListener(e -> {
            LocalDateTime value = fromPicker.getValue();
            if (value != null && value.toLocalTime().equals(LocalDateTime.MIN.toLocalTime())) {
                fromPicker.setValue(value.withHour(0).withMinute(0));
            }
            fireEvent(new UpdateCheckEvent(this));
        });

        toPicker.addValueChangeListener(e -> {
            LocalDateTime value = toPicker.getValue();
            if (value != null && value.toLocalTime().equals(LocalDateTime.MIN.toLocalTime())) {
                // Якщо обрана лише дата (без часу), то встановити 00:00
                toPicker.setValue(value.withHour(0).withMinute(0));
            }
            fireEvent(new UpdateCheckEvent(this));
        });
    }

    public List<Check> getAllByfilters(CheckService service) {
        String filterCustomer = findCustomer.getValue().trim();
        String filterCashier = findCashier.getValue().trim();
        String customerSurname = null;
        String customerPhone = null;
        String employeeSurname = null;
        String employeePhone = null;
        if (!filterCustomer.isEmpty()) {
            if (filterCustomer.matches(".*\\d.*")) {
                customerPhone = filterCustomer;
                if (!customerPhone.startsWith("+"))
                    customerPhone = "+" + customerPhone;
            } else {
                customerSurname = filterCustomer;
            }
        }
        if (!filterCashier.isEmpty()) {
            if (filterCashier.matches(".*\\d.*")) {
                employeePhone = filterCashier;
                if (!employeePhone.startsWith("+"))
                    employeePhone = "+" + employeePhone;
            } else {
                employeeSurname = filterCashier;
            }
        }
        LocalDateTime dateFrom = fromPicker.getValue();
        LocalDateTime dateTo = toPicker.getValue();
        return service.findFilteredChecks(
                employeeSurname,
                employeePhone,
                customerSurname,
                customerPhone,
                dateFrom,
                dateTo
        );
    }

    public void addUpdateListener(ComponentEventListener<UpdateCheckEvent> listener) {
        addListener(UpdateCheckEvent.class, listener);
    }

    public void setAddButton(boolean b) {
        addButton.setEnabled(b);
    }

    public static class UpdateCheckEvent extends UpdateEvent<CheckToolbar> {
        public UpdateCheckEvent(CheckToolbar toolbar) {
            super(toolbar);
        }
    }

    public Button getAddButton() {
        return addButton;
    }
}
