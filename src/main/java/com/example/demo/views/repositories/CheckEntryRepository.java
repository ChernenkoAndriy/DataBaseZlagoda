package com.example.demo.views.repositories;

import com.example.demo.views.repositories.AbstractRepository;
import com.example.demo.views.repositories.database_entities.CheckEntry;
import com.example.demo.views.repositories.database_entities.Employee;
import com.example.demo.views.repositories.mappers.CheckEntryRowMapper;
import com.example.demo.views.repositories.mappers.EmployeeRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.List;
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
        String sql = "INSERT INTO public.\"Sale\"(\n" +
                "\tproduct_number, selling_price, \"UPC\", check_number)\n" +
                "\tVALUES (?, ?, ?, ?);";
        SqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("product_number", e.getAmountOfProducts())
                .addValue("selling_price", e.getSelling_price())
                .addValue("\"UPC\"" , e.getStore_product())
                .addValue("check_number", e.getCheck_number());
         namedJdbcTemplate.update(sql, namedParameters);
    }
    public int update(CheckEntry e) {
        String sql = "UPDATE public.\"Sale\"\n" +
                "\tSET product_number=?, selling_price=? \n" +
                "\tWHERE \"UPC\" = ? AND check_number = ?;";
        SqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("product_number", e.getAmountOfProducts())
                .addValue("selling_price", e.getSelling_price())
                .addValue("\"UPC\"" , e.getStore_product())
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
    public int returnGoods(UUID check, UUID storeProduct) {
        String selectSql = """
        SELECT product_number
        FROM "Sale"
        WHERE "UPC" = ? AND check_number = ?
    """;

        Integer returned = namedJdbcTemplate.getJdbcTemplate().queryForObject(
                selectSql, Integer.class, storeProduct, check
        );

        if (returned == null) return 0;

        String updateSql = """
        UPDATE "Store_Product"
        SET products_number = products_number + ?
        WHERE "UPC" = ? 
    """;

        return namedJdbcTemplate.getJdbcTemplate().update(
                updateSql, returned, storeProduct
        );
    }


    public int deleteSale(UUID check, UUID storeProduct) {
        String sql = """
        DELETE FROM "Sale"
        WHERE "UPC" = ? AND check_number = ?
    """;

        return namedJdbcTemplate.getJdbcTemplate().update(
                sql, storeProduct, check
        );
    }
    public boolean isProductAvailable(UUID upc, int productNumberToSell) {
        String selectSql = """
        SELECT products_number 
        FROM "Store_Product" 
        WHERE "UPC" = :upc
    """;

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("upc", upc);


        Integer available = namedJdbcTemplate.queryForObject(selectSql, params, Integer.class);

        // Перевіряємо, чи є достатньо товару
        return available != null && available >= productNumberToSell;
    }
}
