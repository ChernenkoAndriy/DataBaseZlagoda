package com.example.demo.views.viewmanagers;

import com.example.demo.views.ManagerInitialView;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.data.provider.ListDataProvider;
import database_manegment.database_entities.DatabaseConnectionPool;
import database_manegment.database_entities.Employee;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;

public class ManagerInitialViewManager {
    private static final String selectAllquery = "SELECT * FROM public.\"Employee\" ORDER BY empl_surname ASC ";
    private static final String addWorkerQuery = "INSERT INTO \"Employee\"(id_employee," +
            " empl_surname, empl_name, empl_patronymic, empl_role,\n" +
            "salary, date_of_birth, date_of_start, phone_number, city, street, zip_code) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    //метод заповнює таблицю працівників у ManagreInitialView
    //повертає колекцію з об'єктами що зберігаються в таблиці
    public static ListDataProvider<Employee> fillTheTable(Grid<Employee> table, ArrayList<Employee> data) {
        try (Connection conn = DatabaseConnectionPool.getConnection();
             PreparedStatement stmt = conn.prepareStatement(selectAllquery)) {
            ResultSet rs = stmt.executeQuery();
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
                data.add(employee);
            }
            ListDataProvider<Employee> dataProvider = new ListDataProvider<>(data);
            table.setItems(dataProvider);
            return dataProvider;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void addEmployeetoBd(Employee employee) {
        try (Connection conn = DatabaseConnectionPool.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(addWorkerQuery)) {
            pstmt.setString(1, generateNewEmployeeId(conn));                            // id_employee
            pstmt.setString(2, employee.getEmpl_surname());                        // empl_surname
            pstmt.setString(3, employee.getEmpl_name());                           // empl_name
            pstmt.setString(4, employee.getEmpl_patronymic());                       // empl_patronymic
            pstmt.setString(5, employee.getEmpl_role());                          // empl_role
            pstmt.setBigDecimal(6, employee.getSalary());  // salary
            pstmt.setDate(7, employee.getDate_of_birth());           // date_of_birth
            pstmt.setDate(8, employee.getDate_of_start());           // date_of_start
            pstmt.setString(9, employee.getPhone_number());                     // phone_number
            pstmt.setString(10, employee.getCity());                            // city
            pstmt.setString(11, employee.getStreet());            // street
            pstmt.setString(12, employee.getZip_code());                           // zip_code
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    public static void addEmployee(Employee employee, ListDataProvider<Employee> dataProvider, ArrayList<Employee> data){
        addEmployeetoBd(employee);
        data.add(employee);
        dataProvider.refreshAll();
    }
    private static String generateNewEmployeeId(Connection connection) throws SQLException {
        String sql = "SELECT MAX(id_employee) FROM \"Employee\"";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                String maxId = rs.getString(1); // Наприклад "E0016"
                if (maxId != null) {
                    int number = Integer.parseInt(maxId.substring(1)); // отримати 16
                    number++; // збільшуємо на 1
                    return String.format("E%08d", number); // формуємо E0017
                }
            }
        }
        // Якщо таблиця пуста
        return "E00000001";
    }
}