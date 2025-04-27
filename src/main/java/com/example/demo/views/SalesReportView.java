package com.example.demo.views;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.example.demo.views.services.ReportService;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Route(value = "sales-report", layout = MainLayout.class)
@Component
@Scope("prototype")
@RolesAllowed("ROLE_MANAGER")
@PageTitle("Sales Report | ZLAGODA")
public class SalesReportView extends VerticalLayout {

    private final ReportService reportService;
    private final Grid<Map<String, Object>> grid;

    public SalesReportView(ReportService reportService) {
        this.reportService = reportService;
        this.grid = new Grid<>();

        // Make grid take the full width and height of the page
        setSizeFull();
        grid.setSizeFull();

        // Configure the grid
        grid.addColumn(map -> map.get("category_name")).setHeader("Category Name").setAutoWidth(true).setFlexGrow(1);
        grid.addColumn(map -> map.get("product_name")).setHeader("Product Name").setAutoWidth(true).setFlexGrow(1);
        grid.addColumn(map -> map.get("total_quantity_sold")).setHeader("Total Quantity Sold").setAutoWidth(true).setFlexGrow(1);
        grid.addColumn(map -> map.get("total_revenue")).setHeader("Total Revenue").setAutoWidth(true).setFlexGrow(1);

        // Fetch data for the last 30 days as an example
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(30);
        List<Map<String, Object>> reportData = reportService.getSalesByCategory(startDate, endDate);
        grid.setItems(reportData);

        // Add the grid to the layout
        add(grid);
        setFlexGrow(1, grid); // Ensure the grid grows to fill the available space
    }
}
