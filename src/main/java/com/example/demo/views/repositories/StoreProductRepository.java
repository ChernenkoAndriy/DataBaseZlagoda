package com.example.demo.views.repositories;

import com.example.demo.views.repositories.database_entities.Store_Product;
import com.example.demo.views.repositories.mappers.StoreProductRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public class StoreProductRepository extends AbstractRepository<Store_Product, UUID>{
    public StoreProductRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
        this.rowMapper = new StoreProductRowMapper();
    }

    @Override
    protected String saveAllQuery() {
        return "INSERT INTO public.\"Store_Product\"(" +
                "\"UPC\", \"UPC_prom\", id_product, selling_price, products_number, promotional_product) " +
                "VALUES (:UPC, :UPC_prom, :id_product, :selling_price, :products_number, :promotional_product)";
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
        return findAllQuery() + "  WHERE \"UPC\" = ?";
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

}
