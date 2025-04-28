package com.example.demo.views.repositories;

import com.example.demo.views.repositories.database_entities.Category;
import com.example.demo.views.repositories.mappers.CategoryRowMapper;
import org.checkerframework.checker.units.qual.C;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class CategoryRepository extends AbstractRepository<Category, Integer> {
    public CategoryRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
        this.rowMapper = new CategoryRowMapper();
    }

    @Override
    protected String saveAllQuery() {
        return "INSERT INTO public.\"Category\"(category_name) VALUES (:category_name);";
    }

    @Override
    protected String updateQuery() {
        return "UPDATE public.\"Category\"\n" +
                "SET category_name = :category_name\n" +
                "WHERE category_number = :category_number;";
    }

    @Override
    protected String findAllQuery() {
        return "SELECT " +
                "    c.category_name,\n" +
                "    c.category_number,\n" +
                "    COUNT(p.category_number) AS product_count\n" +
                "FROM public.\"Category\" c\n" +
                "LEFT JOIN public.\"Product\" p ON p.category_number = c.category_number\n" +
                "GROUP BY c.category_name, c.category_number;";
    }

    @Override
    protected String findByIdQuery() {
        return "SELECT " +
                "    c.category_name, " +
                "    c.category_number, " +
                "    COUNT(p.category_number) AS product_count " +
                "FROM public.\"Category\" c " +
                "LEFT JOIN public.\"Product\" p ON p.category_number = c.category_number " +
                "WHERE c.category_number = :category_number " +
                "GROUP BY c.category_name, c.category_number;";
    }

    @Override
    protected String deleteQuery() {
        return "DELETE FROM public.\"Category\"\n" +
                "WHERE category_number = ?;";
    }

    @Override
    protected String countQuery() {
        return "SELECT COUNT(*) FROM public.\"Category\";";
    }

    public List<Category> getAllBy(String name) {
        String sql = "SELECT " +
                "    c.category_name, " +
                "    c.category_number, " +
                "    COUNT(p.category_number) AS product_count " +
                "FROM public.\"Category\" c " +
                "LEFT JOIN public.\"Product\" p ON p.category_number = c.category_number " +
                "WHERE c.category_name LIKE :name " +
                "GROUP BY c.category_name, c.category_number;";

        Map<String, Object> params = new HashMap<>();
        params.put("name", "%" + name + "%"); // додаємо шаблон для пошуку

        return namedJdbcTemplate.query(sql, params, new CategoryRowMapper());
    }

    public List<Category> getAllWithoutSales() {
        String sql = "SELECT c.category_number, c.category_name " +
                "FROM public.\"Category\" c " +
                "WHERE EXISTS ( " +
                "    SELECT 1 " +
                "    FROM public.\"Product\" p " +
                "    WHERE p.category_number = c.category_number " +
                ") " +
                "AND NOT EXISTS ( " +
                "    SELECT 1 " +
                "    FROM public.\"Store_Product\" sp " +
                "    WHERE sp.id_product IN ( " +
                "        SELECT p.id_product " +
                "        FROM public.\"Product\" p " +
                "        WHERE p.category_number = c.category_number " +
                "    ) " +
                "    AND sp.promotional_product = true " +
                ");";

        // Використовуємо BeanPropertyRowMapper для автоматичного мапінгу результатів запиту в об'єкти Category
        return namedJdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Category.class));
    }

}
