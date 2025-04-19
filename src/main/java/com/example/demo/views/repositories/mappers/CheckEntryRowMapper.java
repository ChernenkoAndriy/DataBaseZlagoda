package com.example.demo.views.repositories.mappers;

import com.example.demo.views.repositories.database_entities.CheckEntry;
import org.springframework.jdbc.core.RowMapper;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class CheckEntryRowMapper implements RowMapper<CheckEntry> {
    @Override
    public CheckEntry mapRow(ResultSet rs, int rowNum) throws SQLException {
        CheckEntry check = new CheckEntry();
        check.setCheck_number(UUID.fromString(rs.getString("check_number")));
        check.setAmountOfProducts(rs.getInt("product_number"));
        check.setSelling_price(rs.getBigDecimal("selling_price"));
        check.setStore_product(UUID.fromString(rs.getString("UPC")));
        check.setProductName(rs.getString("product_name"));
        check.setProduct_selling_price(rs.getBigDecimal("psellingprice"));
        return check;
    }
}
