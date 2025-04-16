package com.example.demo.views.repositories;

import com.example.demo.views.repositories.database_entities.Check;
import com.example.demo.views.repositories.mappers.CheckRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
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
                "check_number, card_number, print_date, sum_total, vat, id_employee" +
                ") VALUES (" +
                ":check_number, :card_number, :print_date, :sum_total, :vat, :id_employee" +
                ");";
    }
    protected String updateQuery() {
        return "UPDATE public.\"Check\" SET " +
                "check_number = :check_number, " +
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
        return "SELECT * \n" +
                "FROM public.\"Check\"\n" +
                "INNER JOIN public.\"Customer_Card\" \n" +
                "    ON public.\"Check\".card_number = public.\"Customer_Card\".card_number\n" +
                "INNER JOIN public.\"Employee\" \n" +
                "    ON public.\"Check\".id_employee = public.\"Employee\".id_employee;\n";
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
                .addValue("check_number", e.getCheck_number())
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
                .addValue("check_number", e.getCheck_number())
                .addValue("id_employee", e.getId_employee())
                .addValue("card_number", e.getCard_number())
                .addValue("print_date", e.getPrint_date())
                .addValue("sum_total", e.getSum_total())
                .addValue("vat", e.getVat());
        namedJdbcTemplate.update(sql, namedParameters);
    }
    public List<Check> findAll() {
        String sql = findAllQuery();
        List<Check> checks = namedJdbcTemplate.query(sql, rowMapper);
        for(Check check : checks){
            check.setGoods(checkEntryRepository.findById(check.getId()));
        }
        return checks;
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
            LocalDate dateFrom,
            LocalDate dateTo
    ) {
        StringBuilder sql = new StringBuilder("""
        SELECT * FROM public."Check"
        INNER JOIN public."Customer_Card" ON public."Check".card_number = public."Customer_Card".card_number
        INNER JOIN public."Employee" ON public."Check".id_employee = public."Employee".id_employee
        WHERE 1=1
    """);

        Map<String, Object> params = new HashMap<>();

        if (employeeSurname != null) {
            sql.append(" AND \"Employee\".empl_surname ILIKE :employeeSurname");
            params.put("employeeSurname", "%" + employeeSurname + "%");
        }
        if (employeePhone != null) {
            sql.append(" AND \"Employee\".phone_number ILIKE :employeePhone");
            params.put("employeePhone", "%" + employeePhone + "%");
        }
        if (customerSurname != null) {
            sql.append(" AND \"Customer_Card\".cust_surname ILIKE :customerSurname");
            params.put("customerSurname", "%" + customerSurname + "%");
        }
        if (customerPhone != null) {
            sql.append(" AND \"Customer_Card\".phone_number ILIKE :customerPhone");
            params.put("customerPhone", "%" + customerPhone + "%");
        }
        if (dateFrom != null && dateTo != null) {
            sql.append(" AND print_date BETWEEN :dateFrom AND :dateTo");
            params.put("dateFrom", dateFrom);
            params.put("dateTo", dateTo);
        } else if (dateFrom != null) {
            sql.append(" AND print_date >= :dateFrom");
            params.put("dateFrom", dateFrom);
        } else if (dateTo != null) {
            sql.append(" AND print_date <= :dateTo");
            params.put("dateTo", dateTo);
        }

        return namedJdbcTemplate.query(sql.toString(), params, rowMapper);

    }


}
