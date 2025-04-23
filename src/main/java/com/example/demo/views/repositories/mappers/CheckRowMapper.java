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

        UUID emplId = UUID.fromString(rs.getString("id_employee"));
        check.setId_employee(emplId);
        employee.setId(emplId);
        employee.setEmpl_surname(rs.getString("empl_surname"));
        employee.setEmpl_name(rs.getString("empl_name"));
        employee.setEmpl_patronymic(rs.getString("empl_patronymic"));
        employee.setEmpl_role(rs.getString("empl_role"));
        employee.setSalary(rs.getBigDecimal("salary"));
        employee.setDate_of_birth(rs.getObject("date_of_birth", LocalDate.class));
        employee.setDate_of_start(rs.getObject("date_of_start", LocalDate.class));
        employee.setPhone_number(rs.getString("phone_number"));
        employee.setCity(rs.getString("city"));
        employee.setStreet(rs.getString("street"));
        employee.setZip_code(rs.getString("zip_code"));
        check.setCashier(employee);

        UUID cardNumber = rs.getObject("card_number", UUID.class);
        customerCard.setId(cardNumber);
        check.setCard_number(cardNumber);
        customerCard.setCustSurname(rs.getString("cust_surname"));
        customerCard.setCustName(rs.getString("cust_name"));
        customerCard.setCustPatronymic(rs.getString("cust_patronymic"));
        customerCard.setPhoneNumber(rs.getString("phone_number"));
        customerCard.setCity(rs.getString("city"));
        customerCard.setStreet(rs.getString("street"));
        customerCard.setZipCode(rs.getString("zip_code"));
        customerCard.setPercent(rs.getInt("percent"));
        check.setCustomer(customerCard);

        check.setCheck_number(UUID.fromString(rs.getString("check_number")));
        check.setPrint_date(rs.getTimestamp("print_date").toLocalDateTime());
        check.setVat(rs.getBigDecimal("vat"));
        return check;
    }
}
