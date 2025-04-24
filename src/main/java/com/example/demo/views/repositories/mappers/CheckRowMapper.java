package com.example.demo.views.repositories.mappers;

import com.example.demo.views.repositories.database_entities.Check;
import com.example.demo.views.repositories.database_entities.CustomerCard;
import com.example.demo.views.repositories.database_entities.Employee;
import org.springframework.jdbc.core.RowMapper;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public class CheckRowMapper implements RowMapper<Check> {
    @Override
    public Check mapRow(ResultSet rs, int rowNum) throws SQLException {
        Check check = new Check();
        Employee employee = new Employee();
        CustomerCard customerCard = new CustomerCard();

        // Employee
        UUID emplId = UUID.fromString(rs.getString("e_id"));
        employee.setId(emplId);
        employee.setEmpl_surname(rs.getString("e_surname"));
        employee.setEmpl_name(rs.getString("e_name"));
        employee.setEmpl_patronymic(rs.getString("e_patronymic"));
        employee.setEmpl_role(rs.getString("e_role"));
        employee.setSalary(rs.getBigDecimal("e_salary"));
        employee.setDate_of_birth(rs.getObject("e_birth", LocalDate.class));
        employee.setDate_of_start(rs.getObject("e_start", LocalDate.class));
        employee.setPhone_number(rs.getString("e_phone"));
        employee.setCity(rs.getString("e_city"));
        employee.setStreet(rs.getString("e_street"));
        employee.setZip_code(rs.getString("e_zip"));

        // CustomerCard
        UUID cardNumber = rs.getObject("c_card_number", UUID.class);
        customerCard.setId(cardNumber);
        customerCard.setCustSurname(rs.getString("c_surname"));
        customerCard.setCustName(rs.getString("c_name"));
        customerCard.setCustPatronymic(rs.getString("c_patronymic"));
        customerCard.setPhoneNumber(rs.getString("c_phone"));
        customerCard.setCity(rs.getString("c_city"));
        customerCard.setStreet(rs.getString("c_street"));
        customerCard.setZipCode(rs.getString("c_zip"));
        customerCard.setPercent(rs.getInt("c_percent"));

        // Check
        check.setCheck_number(UUID.fromString(rs.getString("ch_check_number")));
        check.setPrint_date(rs.getObject("ch_print_date", LocalDateTime.class));
        check.setSum_total(rs.getBigDecimal("ch_sum_total"));
        check.setVat(rs.getBigDecimal("ch_vat"));
        check.setId_employee(emplId);
        check.setCard_number(cardNumber);
        check.setCashier(employee);
        check.setCustomer(customerCard);

        return check;
    }
}
