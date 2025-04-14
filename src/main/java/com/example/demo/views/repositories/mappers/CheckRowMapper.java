package com.example.demo.views.repositories.mappers;

import com.example.demo.views.repositories.database_entities.Check;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.UUID;
import java.math.BigDecimal;

public class CheckRowMapper implements RowMapper<Check> {

    @Override
    public Check mapRow(ResultSet rs, int rowNum) throws SQLException {
        Check check = new Check();

        check.setCheck_number(UUID.fromString(rs.getString("check_number")));
        check.setId_employee(UUID.fromString(rs.getString("id_employee")));

        String cardStr = rs.getString("card_number");
        if (cardStr != null) {
            check.setCard_number(UUID.fromString(cardStr));
        }

        check.setPrint_date(rs.getObject("print_date", LocalDateTime.class));
        check.setSum_total(rs.getBigDecimal("sum_total"));
        check.setVat(rs.getBigDecimal("vat"));

        return check;
    }
}
