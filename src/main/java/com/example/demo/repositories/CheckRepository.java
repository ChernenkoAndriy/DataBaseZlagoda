package com.example.demo.repositories;

import com.example.demo.repositories.mappers.CheckRowMapper;
import com.example.demo.repositories.database_entities.Check;
import com.example.demo.repositories.database_entities.CheckEntry;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@Repository
public class CheckRepository{
    private NamedParameterJdbcTemplate namedJdbcTemplate;
    private CheckEntryRepository checkEntryRepository;
    private CheckRowMapper rowMapper;
    public CheckRepository(NamedParameterJdbcTemplate namedJdbcTemplate,
                           CheckEntryRepository checkEntryRepository
                           ) {
        this.namedJdbcTemplate = namedJdbcTemplate;
        this.rowMapper = new CheckRowMapper();
        this.checkEntryRepository = checkEntryRepository;
    }
    protected String saveAllQuery() {
        return "INSERT INTO public.\"Check\" (" +
                " card_number, print_date, sum_total, vat, id_employee" +
                ") VALUES (" +
                " :card_number, :print_date, :sum_total, :vat, :id_employee" +
                ");";
    }
    protected String updateQuery() {
        return "UPDATE public.\"Check\" SET " +
                "card_number = :card_number, " +
                "print_date = :print_date, " +
                "sum_total = :sum_total, " +
                "vat = :vat, " +
                "id_employee = :id_employee " +
                "WHERE check_number = :check_number;";
    }
    protected String findByIdQuery() {
        return findAllQuery() +  " WHERE check_number = ?;\n";
    }
    protected String findAllQuery() {
        return "SELECT \n" +
                "    ch.check_number AS ch_check_number,\n" +
                "    ch.print_date AS ch_print_date,\n" +
                "    ch.sum_total AS ch_sum_total,\n" +
                "    ch.vat AS ch_vat,\n" +
                "    ch.card_number AS ch_card_number,\n" +
                "    ch.id_employee AS ch_id_employee,\n" +
                "\n" +
                "    cc.card_number AS c_card_number,\n" +
                "    cc.cust_surname AS c_surname,\n" +
                "    cc.cust_name AS c_name,\n" +
                "    cc.cust_patronymic AS c_patronymic,\n" +
                "    cc.phone_number AS c_phone,\n" +
                "    cc.city AS c_city,\n" +
                "    cc.street AS c_street,\n" +
                "    cc.zip_code AS c_zip,\n" +
                "    cc.percent AS c_percent,\n" +
                "\n" +
                "    e.id_employee AS e_id,\n" +
                "    e.empl_surname AS e_surname,\n" +
                "    e.empl_name AS e_name,\n" +
                "    e.empl_patronymic AS e_patronymic,\n" +
                "    e.empl_role AS e_role,\n" +
                "    e.salary AS e_salary,\n" +
                "    e.date_of_birth AS e_birth,\n" +
                "    e.date_of_start AS e_start,\n" +
                "    e.phone_number AS e_phone,\n" +
                "    e.city AS e_city,\n" +
                "    e.street AS e_street,\n" +
                "    e.zip_code AS e_zip\n" +
                "\n" +
                "FROM public.\"Check\" ch\n" +
                "LEFT JOIN public.\"Customer_Card\" cc ON ch.card_number = cc.card_number\n" +
                "INNER JOIN public.\"Employee\" e ON ch.id_employee = e.id_employee \n";
    }
    protected String deleteQuery() {
        return "DELETE FROM public.\"Check\" WHERE check_number = ?;";
    }
    protected String countQuery() {
        return "SELECT COUNT(*) FROM public.\"Check\";";
    }
    public void save(Check e) {
        String sql = saveAllQuery();
        SqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("id_employee", e.getId_employee())
                .addValue("card_number", e.getCard_number())
                .addValue("print_date", e.getPrint_date())
                .addValue("sum_total", e.getSum_total())
                .addValue("vat", e.getVat());
        namedJdbcTemplate.update(sql, namedParameters);
    }
    public void update(Check e) {
        String sql = updateQuery();
        SqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("id_employee", e.getId_employee())
                .addValue("card_number", e.getCard_number())
                .addValue("print_date", e.getPrint_date())
                .addValue("sum_total", e.getSum_total())
                .addValue("vat", e.getVat())
                .addValue("check_number", e.getCheck_number());
        namedJdbcTemplate.update(sql, namedParameters);
    }
    public List<Check> findAll() {
        String sql = findAllQuery();
        List<Check> checks = namedJdbcTemplate.query(sql, rowMapper);
        return checks;
    }
    public void setGoodsFor(Check check){
        List<CheckEntry> goods = checkEntryRepository.findById(check.getId());
        check.setGoods(goods);
    }
    public Check findById(UUID id) {
        String sql = findByIdQuery();
        Check check = namedJdbcTemplate.getJdbcTemplate().queryForObject(sql, rowMapper, id);
            check.setGoods(checkEntryRepository.findById(check.getId()));
        return check;
    }
    public int delete(UUID id) {
        String sql = deleteQuery();
        return namedJdbcTemplate.getJdbcTemplate().update(sql, id);
    }
    public int count() {
        return namedJdbcTemplate.getJdbcTemplate().queryForObject(countQuery(), Integer.class);
    }

    public List<Check> findFilteredChecks(
            String employeeSurname,
            String employeePhone,
            String customerSurname,
            String customerPhone,
            LocalDateTime dateFrom,
            LocalDateTime dateTo
    ) {
        StringBuilder sql = new StringBuilder("""
        SELECT 
            ch.check_number AS ch_check_number,
            ch.print_date AS ch_print_date,
            ch.sum_total AS ch_sum_total,
            ch.vat AS ch_vat,
            ch.card_number AS ch_card_number,
            ch.id_employee AS ch_id_employee,

            cc.card_number AS c_card_number,
            cc.cust_surname AS c_surname,
            cc.cust_name AS c_name,
            cc.cust_patronymic AS c_patronymic,
            cc.phone_number AS c_phone,
            cc.city AS c_city,
            cc.street AS c_street,
            cc.zip_code AS c_zip,
            cc.percent AS c_percent,

            e.id_employee AS e_id,
            e.empl_surname AS e_surname,
            e.empl_name AS e_name,
            e.empl_patronymic AS e_patronymic,
            e.empl_role AS e_role,
            e.salary AS e_salary,
            e.date_of_birth AS e_birth,
            e.date_of_start AS e_start,
            e.phone_number AS e_phone,
            e.city AS e_city,
            e.street AS e_street,
            e.zip_code AS e_zip

        FROM public."Check" ch
        LEFT JOIN public."Customer_Card" cc ON ch.card_number = cc.card_number
        INNER JOIN public."Employee" e ON ch.id_employee = e.id_employee
        WHERE 1=1
    """);

        Map<String, Object> params = new HashMap<>();

        if (employeeSurname != null) {
            sql.append(" AND e.empl_surname ILIKE :employeeSurname");
            params.put("employeeSurname", "%" + employeeSurname + "%");
        }
        if (employeePhone != null) {
            sql.append(" AND e.phone_number ILIKE :employeePhone");
            params.put("employeePhone", "%" + employeePhone + "%");
        }
        if (customerSurname != null) {
            sql.append(" AND cc.cust_surname ILIKE :customerSurname");
            params.put("customerSurname", "%" + customerSurname + "%");
        }
        if (customerPhone != null) {
            sql.append(" AND cc.phone_number ILIKE :customerPhone");
            params.put("customerPhone", "%" + customerPhone + "%");
        }

        // Add date filtering logic
        appendDateFilter(sql, params, dateFrom, dateTo);

        return namedJdbcTemplate.query(sql.toString(), params, rowMapper);
    }

    private void appendDateFilter(StringBuilder sql, Map<String, Object> params, LocalDateTime dateFrom, LocalDateTime dateTo) {
        if (dateFrom != null && dateTo != null) {
            boolean isDateOnly = dateFrom.toLocalTime().equals(LocalTime.MIDNIGHT) && dateTo.toLocalTime().equals(LocalTime.MIDNIGHT);

            if (isDateOnly) {
                sql.append(" AND DATE(ch.print_date) BETWEEN :dateFromDate AND :dateToDate");
                params.put("dateFromDate", dateFrom.toLocalDate());
                params.put("dateToDate", dateTo.toLocalDate());
            } else {
                sql.append(" AND ch.print_date BETWEEN :dateFrom AND :dateTo");
                params.put("dateFrom", dateFrom);
                params.put("dateTo", dateTo);
            }
        } else if (dateFrom != null) {
            boolean isDateOnly = dateFrom.toLocalTime().equals(LocalTime.MIDNIGHT);

            if (isDateOnly) {
                sql.append(" AND DATE(ch.print_date) >= :dateFromDate");
                params.put("dateFromDate", dateFrom.toLocalDate());
            } else {
                sql.append(" AND ch.print_date >= :dateFrom");
                params.put("dateFrom", dateFrom);
            }
        } else if (dateTo != null) {
            boolean isDateOnly = dateTo.toLocalTime().equals(LocalTime.MIDNIGHT);

            if (isDateOnly) {
                sql.append(" AND DATE(ch.print_date) <= :dateToDate");
                params.put("dateToDate", dateTo.toLocalDate());
            } else {
                sql.append(" AND ch.print_date <= :dateTo");
                params.put("dateTo", dateTo);
            }
        }
    }


    public UUID saveAndReturnCheckNumber(Check check) {
        String sql = """
        INSERT INTO "Check" (card_number, print_date, sum_total, vat, id_employee)
        VALUES (:card_number, :print_date, :sum_total, :vat, :id_employee)
        RETURNING check_number;
        """;

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("card_number", check.getCard_number())
                .addValue("print_date", check.getPrint_date())
                .addValue("sum_total", check.getSum_total())
                .addValue("vat", check.getVat())
                .addValue("id_employee", check.getId_employee());

        KeyHolder keyHolder = new GeneratedKeyHolder();

        // Ось тут важливо вказати назву повертаємого стовпця
        namedJdbcTemplate.update(sql, params, keyHolder, new String[]{"check_number"});

        Object key = keyHolder.getKeys().get("check_number"); // <- прямо витягуємо ключ

        if (key instanceof UUID uuid) {
            return uuid;
        }
        if (key instanceof String str) {
            return UUID.fromString(str);
        }

        throw new IllegalStateException("Unexpected check_number type returned: " +
                (key != null ? key.getClass().getName() : "null"));
    }


}
