package com.example.demo.views.services;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
public class ReportService {

    private final JdbcTemplate jdbcTemplate;

    public ReportService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Map<String, Object>> getSalesByCategory(LocalDate startDate, LocalDate endDate) {
        String sql = """
            SELECT 
                c.category_name,
                p.product_name,
                SUM(s.product_number) AS total_quantity_sold,
                SUM(s.selling_price * s.product_number) AS total_revenue
            FROM 
                "Category" c
                JOIN "Product" p ON c.category_number = p.category_number
                JOIN "Store_Product" sp ON p.id_product = sp.id_product
                JOIN "Sale" s ON sp."UPC" = s."UPC"
                JOIN "Check" ch ON s.check_number = ch.check_number
            WHERE 
                ch.print_date BETWEEN ? AND ?
            GROUP BY 
                c.category_name, 
                p.product_name
            ORDER BY 
                c.category_name, 
                p.product_name
            """;
        return jdbcTemplate.queryForList(sql, startDate, endDate);
    }
}
