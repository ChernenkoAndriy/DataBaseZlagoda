package com.example.demo.repositories.mappers;

import com.example.demo.repositories.database_entities.CustomerCard;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class CustomerCardRowMapper implements RowMapper<CustomerCard> {

    @Override
    public CustomerCard mapRow(ResultSet rs, int rowNum) throws SQLException {
        CustomerCard customerCard = new CustomerCard();
        customerCard.setId(rs.getObject("card_number", UUID.class));
        customerCard.setCustSurname(rs.getString("cust_surname"));
        customerCard.setCustName(rs.getString("cust_name"));
        customerCard.setCustPatronymic(rs.getString("cust_patronymic"));
        customerCard.setPhoneNumber(rs.getString("phone_number"));
        customerCard.setCity(rs.getString("city"));
        customerCard.setStreet(rs.getString("street"));
        customerCard.setZipCode(rs.getString("zip_code"));
        customerCard.setPercent(rs.getInt("percent"));
        return customerCard;
    }
}
