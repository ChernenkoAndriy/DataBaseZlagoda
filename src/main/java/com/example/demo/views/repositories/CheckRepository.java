package com.example.demo.views.repositories;

import com.example.demo.views.repositories.database_entities.Check;
import com.example.demo.views.repositories.mappers.CheckRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class CheckRepository extends AbstractRepository<Check, UUID> {

    @Autowired
    public CheckRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
        this.rowMapper = new CheckRowMapper();
    }

    @Override
    protected String saveAllQuery() {
        return "INSERT INTO \"Check\" (check_number, id_employee, card_number, print_date, sum_total, vat) " +
                "VALUES (:check_number, :id_employee, :card_number, :print_date, :sum_total, :vat)";
    }

    @Override
    protected String updateQuery() {
        return "UPDATE \"Check\" SET id_employee = :id_employee, card_number = :card_number, " +
                "print_date = :print_date, sum_total = :sum_total, vat = :vat " +
                "WHERE check_number = :check_number";
    }

    @Override
    protected String findAllQuery() {
        return "SELECT * FROM \"Check\"";
    }

    @Override
    protected String findByIdQuery() {
        return "SELECT * FROM \"Check\" WHERE check_number = ?";
    }

    @Override
    protected String deleteQuery() {
        return "DELETE FROM \"Check\" WHERE check_number = ?";
    }

    @Override
    protected String countQuery() {
        return "SELECT COUNT(*) FROM \"Check\"";
    }
}
