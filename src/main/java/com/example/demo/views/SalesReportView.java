package com.example.demo.views;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.html.Span;
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

        // Add query description
        Span queryDescription = new Span("The total quantity and total revenue of products sold by product category, including product names and category names, grouped by category, for a month.");
        queryDescription.getStyle()
                .set("font-size", "16px")
                .set("font-weight", "bold")
                .set("margin-bottom", "17px");

        // Configure the grid columns and enable sorting
        grid.addColumn(map -> map.get("category_name"))
                .setHeader("Category Name")
                .setAutoWidth(true)
                .setFlexGrow(1)
                .setSortable(true); // Enable sorting for Category Name

        grid.addColumn(map -> map.get("product_name"))
                .setHeader("Product Name")
                .setAutoWidth(true)
                .setFlexGrow(1)
                .setSortable(true); // Enable sorting for Product Name

        grid.addColumn(map -> map.get("total_quantity_sold"))
                .setHeader("Total Quantity Sold")
                .setAutoWidth(true)
                .setFlexGrow(1)
                .setSortable(true); // Enable sorting for Total Quantity Sold

        grid.addColumn(map -> map.get("total_revenue"))
                .setHeader("Total Revenue")
                .setAutoWidth(true)
                .setFlexGrow(1)
                .setSortable(true); // Enable sorting for Total Revenue

        // Fetch data for the last 30 days as an example
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(30);
        List<Map<String, Object>> reportData = reportService.getSalesByCategory(startDate, endDate);
        grid.setItems(reportData);

        // Add the description and grid to the layout
        add(queryDescription, grid);
        setFlexGrow(1, grid); // Ensure the grid grows to fill the available space
    }
}
