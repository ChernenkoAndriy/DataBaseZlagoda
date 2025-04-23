package com.example.demo.views.repositories;

import com.example.demo.views.repositories.database_entities.StoreProduct;
import com.example.demo.views.repositories.mappers.StoreProductRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
@Repository
public class StoreProductRepository extends AbstractRepository<StoreProduct, UUID>{
    public StoreProductRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
        this.rowMapper = new StoreProductRowMapper();
    }

    @Override
    protected String saveAllQuery() {
        return "INSERT INTO public.\"Store_Product\"(" +
                " \"UPC_prom\", id_product, selling_price, products_number, promotional_product) " +
                "VALUES (:UPC_prom, :id_product, :selling_price, :products_number, :promotional_product)";
    }


    @Override
    protected String updateQuery() {
        return "UPDATE public.\"Store_Product\"\n" +
                "    SET \"UPC_prom\" = :UPC_prom,\n" +
                "        id_product = :id_product,\n" +
                "        selling_price = :selling_price,\n" +
                "        products_number = :products_number,\n" +
                "        promotional_product = :promotional_product\n" +
                "    WHERE \"UPC\" = :UPC";
    }

    @Override
    protected String findAllQuery() {
        return "SELECT \n" +
                "    p.product_name, \n" +
                "    sp.\"UPC\", \n" +
                "    sp.\"UPC_prom\", \n" +
                "    sp.id_product, \n" +
                "    sp.selling_price, \n" +
                "    sp.products_number, \n" +
                "    sp.promotional_product\n" +
                "FROM public.\"Store_Product\" sp\n" +
                "INNER JOIN public.\"Product\" p ON sp.id_product = p.id_product;\n";
    }

    @Override
    protected String findByIdQuery() {
        return
                "SELECT " +
                        "    p.product_name, " +
                        "    sp.\"UPC\", " +
                        "    sp.\"UPC_prom\", " +
                        "    sp.id_product, " +
                        "    sp.selling_price, " +
                        "    sp.products_number, " +
                        "    sp.promotional_product " +
                        "FROM public.\"Store_Product\" sp " +
                        "INNER JOIN public.\"Product\" p ON sp.id_product = p.id_product " +
                        "WHERE sp.\"UPC\" = ?";
    }


    @Override
    protected String deleteQuery() {
        return "DELETE FROM public.\"Store_Product\"\n" +
                "\tWHERE \"UPC\" = ?;";
    }

    @Override
    protected String countQuery() {
        return  "SELECT COUNT(*) FROM \"Store_Product\";";
    }

    public void changeQuantity(UUID id, int delta) {
        String sql = "UPDATE public.\"Store_Product\" sp\n" +
                "SET products_number = sp.products_number + ?\n" +
                "WHERE sp.\"UPC\" = ?;";
        namedJdbcTemplate.getJdbcTemplate().update(sql, delta, id);
    }

    public List<StoreProduct> getAllBy(String productName, String categoryName, Boolean promotionalProduct, String upc) {
        StringBuilder query = new StringBuilder();
        query.append("SELECT p.product_name, sp.\"UPC\", sp.\"UPC_prom\", ")
                .append("sp.id_product, sp.selling_price, sp.products_number, sp.promotional_product ")
                .append("FROM public.\"Store_Product\" sp ")
                .append("INNER JOIN public.\"Product\" p ON sp.id_product = p.id_product ")
                .append("INNER JOIN public.\"Category\" c ON c.category_number = p.category_number ");

        List<Object> params = new ArrayList<>();
        boolean hasCondition = false;

        if (productName != null) {
            query.append(hasCondition ? " AND " : " WHERE ");
            query.append("p.product_name LIKE ? ");
            params.add("%" + productName + "%");
            hasCondition = true;
        }

        if (promotionalProduct != null) {
            query.append(hasCondition ? " AND " : " WHERE ");
            query.append("sp.promotional_product = ? ");
            params.add(promotionalProduct);
            hasCondition = true;
        }

        if (categoryName != null) {
            query.append(hasCondition ? " AND " : " WHERE ");
            query.append("c.category_name LIKE ? ");
            params.add("%" + categoryName + "%");
        }

        if (upc != null) {
            query.append(hasCondition ? " AND " : " WHERE ");
            query.append("sp.\"UPC\"::text ILIKE ? ");
            params.add("%" + upc + "%");
        }

        return namedJdbcTemplate.getJdbcTemplate().query(
                query.toString(),
                params.toArray(),
                rowMapper
        );
    }


    public List<StoreProduct> getAllWithSale() {
        String sql = """
                    SELECT\s
                                                       p.product_name,\s
                                                       sp."UPC",\s
                                                       sp."UPC_prom",\s
                                                       sp.id_product,\s
                                                       sp.selling_price,\s
                                                       sp.products_number,\s
                                                       sp.promotional_product
                                                   FROM public."Store_Product" sp
                                                   INNER JOIN public."Product" p ON sp.id_product = p.id_product
                                                   WHERE sp."UPC_prom" IS NOT NULL
                                                     AND EXISTS (
                                                         SELECT 1 FROM public."Store_Product" prom
                                                         WHERE prom."UPC" = sp."UPC_prom"
                                                     )
                                                                                          
                """;
        return namedJdbcTemplate.getJdbcTemplate().query(
                sql,
                rowMapper
        );
    }

    public void addTo(UUID id, int i) {
        String sql = """
        UPDATE "Store_Product"
        SET products_number = products_number + ?
        WHERE "UPC" = ?
    """;
        namedJdbcTemplate.getJdbcTemplate().update(sql, i, id);

    }

    public void updatePrice(UUID upcProm, BigDecimal price) {
        String sql = "UPDATE \"Store_Product\"\n" +
                "SET selling_price = ?\n" +
                "WHERE \"UPC\" = ?;";
        namedJdbcTemplate.getJdbcTemplate().update(sql, price, upcProm);
    }

    public StoreProduct getWithUPC_Prom(UUID upc) {
        String sql = "SELECT * FROM \"Store_Product\" WHERE \"UPC_prom\" = ? LIMIT 1";

        List<StoreProduct> result = namedJdbcTemplate.getJdbcTemplate().query(sql, new Object[]{upc}, rowMapper);

        return result.isEmpty() ? null : result.get(0);
    }

}
