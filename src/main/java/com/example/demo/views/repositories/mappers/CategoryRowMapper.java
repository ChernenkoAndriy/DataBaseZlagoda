package com.example.demo.views.repositories.mappers;

import com.example.demo.views.repositories.database_entities.Category;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CategoryRowMapper implements RowMapper<Category> {
    @Override
    public Category mapRow(ResultSet rs, int rowNum) throws SQLException {
        Category category = new Category();
        category.setCategory_number(rs.getInt("category_number"));
        category.setCategory_name(rs.getString("category_name"));
        category.setAmountOfGoods(rs.getInt("product_count"));
        return category;
    }
}
