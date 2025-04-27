package com.example.demo.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.example.demo.views.services.EmployeeReportService;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Route(value = "employee-report", layout = MainLayout.class)
@Component
@Scope("prototype")
@RolesAllowed("ROLE_MANAGER")
@PageTitle("Employee Report | ZLAGODA")
public class EmployeeReportView extends VerticalLayout {

    private final EmployeeReportService reportService;
    private final Grid<Map<String, Object>> grid;
    private final DatePicker startDatePicker;
    private final DatePicker endDatePicker;
    private final TextField productNameField;
    private final Button searchButton;

    public EmployeeReportView(EmployeeReportService reportService) {
        this.reportService = reportService;

        setSizeFull();

        startDatePicker = new DatePicker("Start Date");
        startDatePicker.setValue(LocalDate.of(2025, 4, 1)); // Default value

        endDatePicker = new DatePicker("End Date");
        endDatePicker.setValue(LocalDate.of(2025, 4, 30)); // Default value

        productNameField = new TextField("Product Name");
        productNameField.setValue("Milk"); // Default value

        searchButton = new Button("Search", event -> onSearchButtonClick());

        grid = new Grid<>();
        grid.setSizeFull();
        grid.addColumn(map -> map.get("id_employee")).setHeader("Employee ID").setAutoWidth(true).setFlexGrow(1);
        grid.addColumn(map -> map.get("empl_surname")).setHeader("Surname").setAutoWidth(true).setFlexGrow(1);
        grid.addColumn(map -> map.get("empl_name")).setHeader("Name").setAutoWidth(true).setFlexGrow(1);
        grid.addColumn(map -> map.get("empl_role")).setHeader("Role").setAutoWidth(true).setFlexGrow(1);

        HorizontalLayout inputLayout = new HorizontalLayout(startDatePicker, endDatePicker, productNameField, searchButton);
        inputLayout.setAlignItems(Alignment.BASELINE);

        add(inputLayout, grid);
        setFlexGrow(1, grid);

        onSearchButtonClick();
    }

    private void onSearchButtonClick() {
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();
        String productName = productNameField.getValue();

        if (startDate == null || endDate == null || productName == null || productName.trim().isEmpty()) {
            grid.setItems();
            Notification.show("Please fill in all fields correctly.");
            return;
        }

        if (!reportService.isProductNameValid(productName)) {
            grid.setItems();
            Notification.show("No such product found in the database.");
            return;
        }

        List<Map<String, Object>> results = reportService.getEmployeesWithNoChecksAndNoProductSales(
                startDate, endDate, productName);

        grid.setItems(results);
    }
}