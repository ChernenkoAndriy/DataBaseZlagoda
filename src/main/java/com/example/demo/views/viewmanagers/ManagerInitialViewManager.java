package com.example.demo.views.viewmanagers;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.data.provider.ListDataProvider;
import database_manegment.database_entities.DatabaseConnectionPool;
import database_manegment.database_entities.Employee;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class ManagerInitialViewManager {
    private static final String selectAllquery = "SELECT * FROM public.\"Employee\"";
//метод заповнює таблицю працівників у ManagreInitialView
    //повертає колекцію з об'єктами що зберігаються в таблиці
    public static ListDataProvider<Employee> fillTheTable(Grid<Employee> table){
        try (Connection conn = DatabaseConnectionPool.getConnection();
             PreparedStatement stmt = conn.prepareStatement(selectAllquery)) {
            ResultSet rs = stmt.executeQuery();
            ArrayList<Employee> employees = new ArrayList<>();
            while (rs.next()) {
                String id = rs.getString("id_employee");
                String name = rs.getString("empl_name");
                String surname = rs.getString("empl_surname");
                String patronymic = rs.getString("empl_patronymic");
                String role = rs.getString("empl_role");
                BigDecimal salary = rs.getBigDecimal("salary");
                Date birth = rs.getDate("date_of_birth");
                Date start = rs.getDate("date_of_start");
                String phone = rs.getString("phone_number");
                String city = rs.getString("city");
                String street = rs.getString("street");
                String zip = rs.getString("zip_code");
                Employee employee = new Employee(id, name, surname, patronymic, role, salary,
                        birth, start, phone, city, street, zip);
                employees.add(employee);
            }
            ListDataProvider<Employee> dataProvider = new ListDataProvider<>(employees);
            table.setItems(dataProvider);
            return dataProvider;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public static void addEmployee(){}
}
