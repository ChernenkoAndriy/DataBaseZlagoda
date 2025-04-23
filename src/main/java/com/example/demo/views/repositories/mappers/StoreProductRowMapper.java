package com.example.demo.views.repositories.mappers;

import com.example.demo.views.repositories.database_entities.StoreProduct;
import org.postgresql.util.PSQLException;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import java.math.BigDecimal;

public class StoreProductRowMapper implements RowMapper<StoreProduct> {
    @Override
    public StoreProduct mapRow(ResultSet rs, int rowNum) throws SQLException {
        UUID upc = rs.getObject("UPC", UUID.class);
        UUID upcProm = rs.getObject("UPC_prom", UUID.class);
        int idProduct = rs.getInt("id_product");
        BigDecimal sellingPrice = rs.getBigDecimal("selling_price");
        int productNumber = rs.getInt("products_number");
        boolean promotionalProduct = rs.getBoolean("promotional_product");
        String productName = null;
        try {
            productName = rs.getString("product_name");
        }catch (PSQLException e){

        }
        return new StoreProduct(upc, upcProm, idProduct, sellingPrice, productNumber, promotionalProduct, productName);
    }
}
