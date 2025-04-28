package com.example.demo.repositories;

import com.example.demo.repositories.mappers.CheckEntryRowMapper;
import com.example.demo.repositories.database_entities.CheckEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
@Repository
public class CheckEntryRepository{
    protected final NamedParameterJdbcTemplate namedJdbcTemplate;
    protected RowMapper<CheckEntry> rowMapper;
    @Autowired
    public CheckEntryRepository(NamedParameterJdbcTemplate jdbcTemplate) {

        this.namedJdbcTemplate = jdbcTemplate;
        this.rowMapper = new CheckEntryRowMapper();
    }
    public void save(CheckEntry e) {
        String sql = "INSERT INTO public.\"Sale\"(" +
                "product_number, selling_price, \"UPC\", check_number) " +
                "VALUES (:product_number, :selling_price, :UPC, :check_number)";
        SqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("product_number", e.getAmountOfProducts())
                .addValue("selling_price", e.getSelling_price())
                .addValue("UPC", e.getStore_product())  // без лапок у ключі
                .addValue("check_number", e.getCheck_number());
        namedJdbcTemplate.update(sql, namedParameters);
    }

    public int update(CheckEntry e) {
        String sql = "UPDATE public.\"Sale\" " +
                "SET product_number = :product_number, " +
                "selling_price = :selling_price " +
                "WHERE \"UPC\" = :upc AND check_number = :check_number";

        SqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("product_number", e.getAmountOfProducts())
                .addValue("selling_price", e.getSelling_price())
                .addValue("upc", e.getStore_product())
                .addValue("check_number", e.getCheck_number());

        return namedJdbcTemplate.update(sql, namedParameters);
    }

    public int count() {
        String sql = "SELECT COUNT (*) FROM \"Sale\"";
        return namedJdbcTemplate.getJdbcTemplate().queryForObject(sql, Integer.class);
    }
    public List<CheckEntry> findAll() {
        String sql = "SELECT s.*, (sp.selling_price) AS psellingprice, p.product_name\n" +
                "FROM \"Sale\" s\n" +
                "JOIN \"Store_Product\" sp ON s.\"UPC\" = sp.\"UPC\"\n" +
                "JOIN \"Product\" p ON sp.id_product = p.id_product;\n";
        return namedJdbcTemplate.query(sql, rowMapper);
    }
    public CheckEntry findById(UUID check, UUID store_product) {
        String sql = "SELECT s.*, (sp.selling_price) AS psellingprice, p.product_name " +
                "FROM \"Sale\" s " +
                "JOIN \"Store_Product\" sp ON s.\"UPC\" = sp.\"UPC\" " +
                "JOIN \"Product\" p ON sp.id_product = p.id_product " +
                "WHERE s.\"UPC\" = ? AND s.check_number = ?;";

        return namedJdbcTemplate.getJdbcTemplate().queryForObject(sql, rowMapper, store_product, check);
    }
    public List<CheckEntry> findById(UUID check) {
        String sql = "SELECT s.*, (sp.selling_price) AS psellingprice, p.product_name " +
                "FROM \"Sale\" s " +
                "JOIN \"Store_Product\" sp ON s.\"UPC\" = sp.\"UPC\" " +
                "JOIN \"Product\" p ON sp.id_product = p.id_product " +
                "WHERE s.check_number = ?;";
        return namedJdbcTemplate.getJdbcTemplate().query(sql, rowMapper, check);
    }
    public Integer getMaxCount(CheckEntry checkEntry) {
        UUID product = checkEntry.getStore_product();
        String sql = "SELECT products_number FROM \"Store_Product\" WHERE \"UPC\" = :product";

        Map<String, Object> params = new HashMap<>();
        params.put("product", product);

        return namedJdbcTemplate.queryForObject(sql, params, Integer.class);
    }
    public void delete(CheckEntry c) {
        UUID upc = c.getStore_product();
        UUID checkNumber = c.getCheck_number();
        String sql = "DELETE FROM \"Sale\" WHERE check_number = :checkNumber AND \"UPC\" = :upc";
        Map<String, Object> params = new HashMap<>();
        params.put("checkNumber", checkNumber);
        params.put("upc", upc);
        namedJdbcTemplate.update(sql, params);
    }
    public void returnGoods(int delta, UUID storeProduct) {
        String sql = "UPDATE \"Store_Product\" SET products_number = products_number + :delta WHERE \"UPC\" = :storeProduct";

        Map<String, Object> params = new HashMap<>();
        params.put("delta", delta);
        params.put("storeProduct", storeProduct);

        namedJdbcTemplate.update(sql, params);
    }

}
