package com.example.demo.views.repositories.mappers;

import com.example.demo.views.repositories.database_entities.Store_Product;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import java.math.BigDecimal;

public class StoreProductRowMapper implements RowMapper<Store_Product> {
    @Override
    public Store_Product mapRow(ResultSet rs, int rowNum) throws SQLException {
        UUID upc = rs.getObject("UPC", UUID.class);
        UUID upcProm = rs.getObject("UPC_prom", UUID.class);
        int idProduct = rs.getInt("id_product");
        BigDecimal sellingPrice = rs.getBigDecimal("selling_price");
        int productNumber = rs.getInt("products_number");
        boolean promotionalProduct = rs.getBoolean("promotional_product");
        String productName = rs.getString("product_name");

        return new Store_Product(upc, upcProm, idProduct, sellingPrice, productNumber, promotionalProduct, productName);
    }
}
