package com.example.demo.repositories.mappers;

import com.example.demo.repositories.database_entities.Product;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductRowMapper implements RowMapper<Product> {

    @Override
    public Product mapRow(ResultSet rs, int rowNum) throws SQLException {
        Product product = new Product();
        product.setId_product(rs.getInt("id_product"));
        product.setCategory_number(rs.getInt("category_number"));
        product.setProduct_name(rs.getString("product_name"));
        product.setCharacteristics(rs.getString("characteristics"));
        product.setCategoryName(rs.getString("category_name"));
        return product;
    }
}
