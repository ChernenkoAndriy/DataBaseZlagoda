package com.example.demo.views.services;

import com.example.demo.views.repositories.database_entities.AuthorizationData;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class AuthorizationService {
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    public AuthorizationService(NamedParameterJdbcTemplate namedParameterJdbcTemplate){
        this.namedParameterJdbcTemplate=namedParameterJdbcTemplate;
    }

    public AuthorizationData getUserByLogin(String login) {
        String sql = "SELECT login, \"password\", \"Employee\".empl_role, \"Employee\".phone_number, " +
                "\"Employee\".empl_name, \"Employee\".empl_surname, \"Employee\".id_employee " +
                "FROM public.\"Authorization_Data\" " +
                "INNER JOIN \"Employee\" ON \"Employee\".id_employee = \"Authorization_Data\".id_employee " +
                "WHERE login = :login";

        Map<String, Object> params = new HashMap<>();
        params.put("login", login);

        try {
            return namedParameterJdbcTemplate.queryForObject(sql, params, (rs, rowNum) -> {
                AuthorizationData data = new AuthorizationData();
                data.setLogin(rs.getString("login"));
                data.setPassword(rs.getString("password"));
                data.setRole(rs.getString("empl_role"));
                data.setId(UUID.fromString(rs.getString("id_employee")));
                data.setPhone(rs.getString("phone_number"));
                data.setName(rs.getString("empl_name"));
                data.setSurname(rs.getString("empl_surname"));
                return data;
            });
        } catch (EmptyResultDataAccessException e) {
            return new AuthorizationData();
        }
    }

    public boolean check(String login) {
        String sql = "SELECT COUNT(*) FROM \"Authorization_Data\" WHERE login = :login";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("login", login);
        Integer count = namedParameterJdbcTemplate.queryForObject(sql, params, Integer.class);
        return count > 0;
    }

    public void addUser(String login, String password, UUID idEmployee) {
        String sql = "INSERT INTO \"Authorization_Data\" (login, \"password\", id_employee) " +
                "VALUES (:login, crypt(:password, gen_salt('bf')), :id_employee)";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("login", login);
        params.addValue("password", password);
        params.addValue("id_employee", idEmployee);
        namedParameterJdbcTemplate.update(sql, params);
    }

}
