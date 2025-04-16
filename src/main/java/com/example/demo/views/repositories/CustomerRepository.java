package com.example.demo.views.repositories;

import com.example.demo.views.repositories.database_entities.CustomerCard;
import com.example.demo.views.repositories.mappers.CustomerCardRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
@Repository
public class CustomerRepository extends AbstractRepository<CustomerCard, UUID> {

    public CustomerRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
        this.rowMapper=new CustomerCardRowMapper();
    }

    @Override
    protected String saveAllQuery() {
        return "INSERT INTO public.\"Customer_Card\"(\n" +
                "    card_number, cust_surname, cust_name, cust_patronymic, phone_number, city, street, zip_code, percent)\n" +
                "VALUES (\n" +
                "    :cardNumber,\n" +
                "    :custSurname,\n" +
                "    :custName,\n" +
                "    :custPatronymic,\n" +
                "    :phoneNumber,\n" +
                "    :city,\n" +
                "    :street,\n" +
                "    :zipCode,\n" +
                "    :percent\n" +
                ");";
    }

    @Override
    protected String updateQuery() {
        return "UPDATE public.\"Customer_Card\"\n" +
                "SET\n" +
                "    card_number = :cardNumber,\n" +
                "    cust_surname = :custSurname,\n" +
                "    cust_name = :custName,\n" +
                "    cust_patronymic = :custPatronymic,\n" +
                "    phone_number = :phoneNumber,\n" +
                "    city = :city,\n" +
                "    street = :street,\n" +
                "    zip_code = :zipCode,\n" +
                "    percent = :percent\n" +
                "WHERE\n" +
                "    card_number = :cardNumber;";
    }

    @Override
    protected String findAllQuery() {
        return "SELECT * FROM public.\"Customer_Card\";";
    }

    @Override
    protected String findByIdQuery() {
        return "SELECT * FROM public.\"Customer_Card\" WHERE card_number = ?;";
    }

    @Override
    protected String deleteQuery() {
        return "DELETE FROM public.\"Customer_Card\" WHERE card_number = ?;";
    }

    @Override
    protected String countQuery() {
        return "SELECT COUNT(*) FROM public.\"Customer_Card\";";
    }
    public List<String> customersPhones() {
        String sql = "SELECT phone_number FROM public.\"Customer_Card\";";
        return namedJdbcTemplate.query(
                sql,
                (rs, rowNum) -> rs.getString("phone_number")
        );
    }

    public CustomerCard getCustomer(String phoneNumber) {
        String sql = "SELECT * FROM public.\"Customer_Card\" WHERE phone_number = :phoneNumber";

        Map<String, Object> params = new HashMap<>();
        params.put("phoneNumber", phoneNumber);

        return namedJdbcTemplate.queryForObject(
                sql,
                params,
                new CustomerCardRowMapper()
        );
    }


}
