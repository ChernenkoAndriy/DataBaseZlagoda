package com.example.demo.views.services;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
public class EmployeeReportService {

    private final JdbcTemplate jdbcTemplate;

    public EmployeeReportService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public boolean isProductNameValid(String productName) {
        String sql = """
            SELECT COUNT(*) 
            FROM "Product" 
            WHERE product_name = ?
            """;
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, productName);
        return count != null && count > 0;
    }

    public List<Map<String, Object>> getEmployeesWithNoChecksAndNoProductSales(
            LocalDate startDate, LocalDate endDate, String productName) {
        String sql = """
            SELECT e.id_employee,
                   e.empl_surname,
                   e.empl_name,
                   e.empl_role
            FROM "Employee" e
            WHERE e.id_employee NOT IN (
                SELECT ch.id_employee 
                FROM "Check" ch 
                WHERE ch.print_date BETWEEN ? AND ?
            )
            AND e.id_employee NOT IN (
                SELECT ch2.id_employee 
                FROM "Check" ch2
                    JOIN "Sale" s ON ch2.check_number = s.check_number
                    JOIN "Store_Product" sp ON s."UPC" = sp."UPC"
                    JOIN "Product" p ON sp.id_product = p.id_product
                WHERE p.product_name = ?
                )
            """;

        return jdbcTemplate.queryForList(sql, startDate, endDate, productName);
    }
}
