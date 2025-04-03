package com.example.demo.views.viewmanagers;

import com.vaadin.flow.data.provider.ListDataProvider;
import database_manegment.database_entities.DatabaseConnectionPool;
import database_manegment.database_entities.Employee;
import jakarta.validation.ConstraintViolationException;

import javax.management.openmbean.KeyAlreadyExistsException;
import java.sql.*;
import java.util.ArrayList;

public class MEService {
    private static final String selectAllQuery = "SELECT * FROM public.\"Employee\" ORDER BY empl_surname ASC";
    private static final String addWorkerQuery = "INSERT INTO \"Employee\"(id_employee, empl_surname, empl_name, empl_patronymic, empl_role,\n"
            + "salary, date_of_birth, date_of_start, phone_number, city, street, zip_code) "
            + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String updateWorkerQuery = "UPDATE \"Employee\"\n"
            + "SET empl_name=?, empl_surname=?, empl_patronymic=?, empl_role=?, salary=?, date_of_birth=?, \n"
            + "date_of_start=?, phone_number=?, city=?, street=?, zip_code =?\n"
            + "WHERE id_employee = ?;";
    private static final String deleteWorkerQuery = "DELETE FROM \"Employee\" WHERE id_employee=?";

    private final ArrayList<Employee> data;
    private final ListDataProvider<Employee> dataProvider;

    public MEService(ArrayList<Employee> data, ListDataProvider<Employee> dataProvider) {
        this.data = data;
        this.dataProvider = dataProvider;
    }

    public void fillTheTable() {
        try (Connection conn = DatabaseConnectionPool.getConnection();
             PreparedStatement stmt = conn.prepareStatement(selectAllQuery)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Employee employee = new Employee(
                        rs.getString("id_employee"),
                        rs.getString("empl_surname"),
                        rs.getString("empl_name"),
                        rs.getString("empl_patronymic"),
                        rs.getString("empl_role"),
                        rs.getBigDecimal("salary"),
                        rs.getDate("date_of_birth"),
                        rs.getDate("date_of_start"),
                        rs.getString("phone_number"),
                        rs.getString("city"),
                        rs.getString("street"),
                        rs.getString("zip_code")
                );
                data.add(employee);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addEmployee(Employee employee) {
        try (Connection conn = DatabaseConnectionPool.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(addWorkerQuery)) {
            pstmt.setString(1, generateNewEmployeeId(conn));
            pstmt.setString(2, employee.getEmpl_surname());
            pstmt.setString(3, employee.getEmpl_name());
            pstmt.setString(4, employee.getEmpl_patronymic());
            pstmt.setString(5, employee.getEmpl_role());
            pstmt.setBigDecimal(6, employee.getSalary());
            pstmt.setDate(7, employee.getDate_of_birth());
            pstmt.setDate(8, employee.getDate_of_start());
            pstmt.setString(9, employee.getPhone_number());
            pstmt.setString(10, employee.getCity());
            pstmt.setString(11, employee.getStreet());
            pstmt.setString(12, employee.getZip_code());
            pstmt.executeUpdate();
            data.add(employee);
            dataProvider.refreshAll();
        } catch (SQLException e) {
            throw new KeyAlreadyExistsException("Such telephone number is already used");
        }
    }

    private String generateNewEmployeeId(Connection connection) throws SQLException {
        String sql = "SELECT MAX(id_employee) FROM \"Employee\"";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                String maxId = rs.getString(1);
                if (maxId != null) {
                    int number = Integer.parseInt(maxId.substring(1));
                    return String.format("E%08d", ++number);
                }
            }
        }
        return "E00000001";
    }

    public void deleteEmployee(Employee employee) {
        try (Connection conn = DatabaseConnectionPool.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(deleteWorkerQuery)) {
            pstmt.setString(1, employee.getId_employee());
            pstmt.executeUpdate();
            data.remove(employee);
            dataProvider.refreshAll();
        } catch (SQLException e) {
            throw new ConstraintViolationException("Unable to delete employee: " + employee.getEmpl_name() + " " + employee.getEmpl_surname(), null);
        }
    }

    public void updateEmployee(Employee employee) {
        try (Connection conn = DatabaseConnectionPool.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(updateWorkerQuery)) {
            pstmt.setString(1, employee.getEmpl_name());
            pstmt.setString(2, employee.getEmpl_surname());
            pstmt.setString(3, employee.getEmpl_patronymic());
            pstmt.setString(4, employee.getEmpl_role());
            pstmt.setBigDecimal(5, employee.getSalary());
            pstmt.setDate(6, employee.getDate_of_birth());
            pstmt.setDate(7, employee.getDate_of_start());
            pstmt.setString(8, employee.getPhone_number());
            pstmt.setString(9, employee.getCity());
            pstmt.setString(10, employee.getStreet());
            pstmt.setString(11, employee.getZip_code());
            pstmt.setString(12, employee.getId_employee());
            pstmt.executeUpdate();
            dataProvider.refreshAll();
        } catch (SQLException e) {
            throw new KeyAlreadyExistsException("Such telephone number is already used");
        }
    }

    public void filterTable(String role, String prompt) {
        dataProvider.clearFilters();
        String searchTerm = prompt.toLowerCase();
        dataProvider.addFilter(employee -> {
            boolean matchesRole = "All".equalsIgnoreCase(role.trim()) || (employee.getEmpl_role() != null && employee.getEmpl_role().trim().equalsIgnoreCase(role.trim()));
            boolean matchesSearch = employee.getEmpl_name().toLowerCase().contains(searchTerm) ||
                    employee.getEmpl_surname().toLowerCase().contains(searchTerm) ||
                    employee.getPhone_number().contains(searchTerm);
            return matchesRole && matchesSearch;
        });
        dataProvider.refreshAll();
    }
}
