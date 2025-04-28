package com.example.demo.repositories;

import com.example.demo.repositories.database_entities.CustomerCard;
import com.example.demo.repositories.mappers.CustomerCardRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class CustomerRepository extends AbstractRepository<CustomerCard, UUID> {

    public CustomerRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
        this.rowMapper=new CustomerCardRowMapper();
    }

    @Override
    protected String saveAllQuery() {
        return "INSERT INTO public.\"Customer_Card\"(\n" +
                "   cust_surname, cust_name, cust_patronymic, phone_number, city, street, zip_code, percent)\n" +
                "VALUES (\n" +
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

    public List<CustomerCard> getAllBy(String surname, String phone, Integer percent) {
        StringBuilder sql = new StringBuilder("SELECT * FROM public.\"Customer_Card\" WHERE 1=1");
        List<Object> params = new ArrayList<>();

        if (surname != null) {
            sql.append(" AND cust_surname ILIKE ?");
            params.add("%" + surname + "%");
        }
        if (phone != null) {
            sql.append(" AND phone_number ILIKE ?");
            params.add("%" + phone + "%");
        }
        if (percent != null) {
            sql.append(" AND percent = ?");
            params.add(percent);
        }

        return namedJdbcTemplate
                .getJdbcTemplate()
                .query(sql.toString(), params.toArray(), rowMapper);
    }

    public boolean existsPhoneNumber(String number) {
        String sql = "SELECT COUNT(*) FROM public.\"Customer_Card\" WHERE phone_number = :phoneNumber";
        Map<String, Object> params = new HashMap<>();
        params.put("phoneNumber", number);

        Integer count = namedJdbcTemplate.queryForObject(sql, params, Integer.class);
        return count != null && count > 0;
    }

    public boolean existsPhoneNumber(String number, UUID id) {
        String sql = "SELECT COUNT(*) FROM public.\"Customer_Card\" " +
                "WHERE phone_number = :phoneNumber AND card_number <> :cardNumber";
        Map<String, Object> params = new HashMap<>();
        params.put("phoneNumber", number);
        params.put("cardNumber", id);

        Integer count = namedJdbcTemplate.queryForObject(sql, params, Integer.class);
        return count != null && count > 0;
    }

    public boolean existsChecksLinkedTo(UUID id) {
        String sql = "SELECT COUNT(*) FROM \"Check\" WHERE card_number = ?";
        int count = namedJdbcTemplate.getJdbcTemplate().queryForObject(sql, Integer.class, id);
        return count > 0;
    }

}
