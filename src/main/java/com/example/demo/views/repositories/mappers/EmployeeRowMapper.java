package com.example.demo.views.repositories.mappers;

import com.example.demo.views.repositories.database_entities.Employee;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.UUID;

public class EmployeeRowMapper implements RowMapper<Employee> {
    @Override
    public Employee mapRow(ResultSet rs, int rowNum) throws SQLException {
        Employee employee = new Employee();

        employee.setId(UUID.fromString(rs.getString("id_employee")));
        employee.setEmpl_surname(rs.getString("empl_surname"));
        employee.setEmpl_name(rs.getString("empl_name"));
        employee.setEmpl_patronymic(rs.getString("empl_patronymic"));
        employee.setEmpl_role(rs.getString("empl_role"));
        employee.setSalary(rs.getBigDecimal("salary"));

        // Перетворення з ResultSet на LocalDate для полів типу LocalDate
        employee.setDate_of_birth(rs.getObject("date_of_birth", LocalDate.class));
        employee.setDate_of_start(rs.getObject("date_of_start", LocalDate.class));

        employee.setPhone_number(rs.getString("phone_number"));
        employee.setCity(rs.getString("city"));
        employee.setStreet(rs.getString("street"));
        employee.setZip_code(rs.getString("zip_code"));

        return employee;
    }

    public void setParams(Employee e){

    }
}
