package com.example.demo.views;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
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

    public EmployeeReportView(EmployeeReportService reportService) {
        this.reportService = reportService;
        this.grid = new Grid<>();

        // Configure the grid
        grid.addColumn(map -> map.get("id_employee")).setHeader("Employee ID");
        grid.addColumn(map -> map.get("empl_surname")).setHeader("Surname");
        grid.addColumn(map -> map.get("empl_name")).setHeader("Name");
        grid.addColumn(map -> map.get("empl_role")).setHeader("Role");

        // Fetch data for the specified date range and product
        LocalDate startDate = LocalDate.of(2025, 4, 1);
        LocalDate endDate = LocalDate.of(2025, 4, 30);
        String productName = "Milk";
        List<Map<String, Object>> reportData = reportService.getEmployeesWithNoChecksAndNoProductSales(startDate, endDate, productName);
        grid.setItems(reportData);

        // Limit to 5 rows for display
        grid.setPageSize(5);

        add(grid);
    }
}