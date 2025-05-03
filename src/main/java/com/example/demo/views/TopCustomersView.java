package com.example.demo.views;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.html.H2;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import com.vaadin.flow.router.PageTitle;
import jakarta.annotation.security.RolesAllowed;


@Route(value = "top-customers", layout = MainLayout.class)
@PageTitle("Top Customers")
@RolesAllowed("ROLE_MANAGER")

public class TopCustomersView extends VerticalLayout {

    public TopCustomersView() {
        H2 title = new H2("Топ-5 клієнтів за сумою покупок");

        Grid<CustomerInfo> grid = new Grid<>(CustomerInfo.class);
        grid.setColumns("surname", "name", "city", "totalSpent");

        List<CustomerInfo> data = fetchTopCustomers();
        grid.setItems(data);

        add(title, grid);
    }

    private List<CustomerInfo> fetchTopCustomers() {
        List<CustomerInfo> customers = new ArrayList<>();

        String sql = """
        SELECT
                   c.cust_surname,
                   c.cust_name,
                   c.city,
                   SUM(sp.products_number * sp.selling_price) AS total_spent
               FROM
                   "Customer_Card" c
               JOIN "Check" ch ON c.card_number = ch.card_number
               JOIN "Sale" s ON ch.check_number = s.check_number
               JOIN "Store_Product" sp ON s."UPC" = sp."UPC"
               JOIN "Product" p ON sp.id_product = p.id_product
               GROUP BY
                   c.cust_surname, c.cust_name, c.city
               ORDER BY
                   total_spent DESC
               LIMIT 5;
               
    """;


        try (
                Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Zlagoda", "postgres", "Sand5Man9");
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()
        ) {
            while (rs.next()) {
                String surname = rs.getString("cust_surname");
                String name = rs.getString("cust_name");
                String city = rs.getString("city");
                double totalSpent = rs.getDouble("total_spent");

                customers.add(new CustomerInfo(surname, name, city, totalSpent));
            }
        } catch (SQLException e) {
            Notification.show("Помилка з'єднання з базою даних");
            e.printStackTrace();
        }

        return customers;
    }


    public static class CustomerInfo {
        private String surname;
        private String name;
        private String city;
        private double totalSpent;

        public CustomerInfo(String surname, String name, String city, double totalSpent) {
            this.surname = surname;
            this.name = name;
            this.city = city;
            this.totalSpent = totalSpent;
        }

        // Getters потрібні для Grid
        public String getSurname() { return surname; }
        public String getName() { return name; }
        public String getCity() { return city; }
        public double getTotalSpent() { return totalSpent; }
    }
}
