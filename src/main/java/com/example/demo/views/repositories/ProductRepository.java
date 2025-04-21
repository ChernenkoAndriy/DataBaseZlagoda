package com.example.demo.views.repositories;

import com.example.demo.views.repositories.database_entities.Product;
import com.example.demo.views.repositories.mappers.ProductRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProductRepository extends AbstractRepository<Product, Integer>{
    public ProductRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
        this.rowMapper = new ProductRowMapper();
    }

    @Override
    protected String saveAllQuery() {
        return "INSERT INTO public.\"Product\" (\n" +
                "    category_number, product_name, characteristics\n" +
                ") VALUES (\n" +
                "    :category_number, :product_name, :characteristics\n" +
                ")\n";
    }

    @Override
    protected String updateQuery() {
        return "UPDATE public.\"Product\"\n" +
                "SET category_number = :category_number,\n" +
                "    product_name = :product_name,\n" +
                "    characteristics = :characteristics\n" +
                "WHERE id_product = :id_product\n";
    }

    @Override
    protected String findAllQuery() {
        return "SELECT id_product, \"Product\".category_number, product_name, category_name, characteristics\n" +
                "\tFROM public.\"Product\" INNER JOIN \"Category\" ON \"Category\".category_number = \"Product\".category_number;";
    }

    @Override
    protected String findByIdQuery() {
        return "SELECT id_product, \"Product\".category_number, product_name, category_name, characteristics\n" +
                "\tFROM public.\"Product\" INNER JOIN \"Category\" ON \"Category\".category_number = \"Product\".category_number" +
                "WHERE id_product = ?;";
    }

    @Override
    protected String deleteQuery() {
        return "DELETE FROM public.\"Product\"\n" +
                "\tWHERE id_product = ?;";
    }

    @Override
    protected String countQuery() {
        return "SELECT COUNT(*) FROM \"Product\"";
    }

    public List<Product> getAllBy(String name, Integer categoryNumber) {
        StringBuilder sql = new StringBuilder(
                "SELECT id_product, \"Product\".category_number, product_name, category_name, characteristics " +
                        "FROM public.\"Product\" " +
                        "INNER JOIN \"Category\" ON \"Category\".category_number = \"Product\".category_number"
        );

        MapSqlParameterSource params = new MapSqlParameterSource();
        boolean whereAdded = false;

        if (name != null && !name.isEmpty()) {
            sql.append(whereAdded ? " AND " : " WHERE ");
            sql.append("product_name ILIKE :name");
            params.addValue("name", "%" + name + "%");
            whereAdded = true;
        }

        if (categoryNumber != null) {
            sql.append(whereAdded ? " AND " : " WHERE ");
            sql.append("\"Product\".category_number = :categoryNumber");
            params.addValue("categoryNumber", categoryNumber);
        }

        return namedJdbcTemplate.query(sql.toString(), params, rowMapper);
    }

}
