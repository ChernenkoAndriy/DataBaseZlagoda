package com.example.demo.views.repositories;

import com.example.demo.views.repositories.database_entities.Employee;
import com.example.demo.views.repositories.mappers.EmployeeRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Repository
public class EmployeeRepository extends AbstractRepository<Employee, UUID>{
@Autowired
    public EmployeeRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
        this.rowMapper= new EmployeeRowMapper();
    }
    @Override
    protected String saveAllQuery() {
        return "INSERT INTO \"Employee\" (empl_surname, empl_name, empl_patronymic, empl_role, salary, " +
                "date_of_birth, date_of_start, phone_number, city, street, zip_code) " +
                "VALUES (:empl_surname, :empl_name, :empl_patronymic, :empl_role::\"Roles\", :salary, " +
                ":date_of_birth, :date_of_start, :phone_number, :city, :street, :zip_code)";
    }
    @Override
    protected String updateQuery() {
        return "UPDATE \"Employee\" SET empl_surname = :empl_surname, empl_name = :empl_name, " +
                "empl_patronymic = :empl_patronymic, empl_role = :empl_role::\"Roles\", salary = :salary, " +
                "date_of_birth = :date_of_birth, date_of_start = :date_of_start, phone_number = :phone_number, " +
                "city = :city, street = :street, zip_code = :zip_code WHERE id_employee = :id_employee";
    }
    @Override
    protected String findAllQuery() {
        return "SELECT * FROM \"Employee\"";
    }
    @Override
    protected String findByIdQuery() {
        return "SELECT id_employee, empl_surname, empl_name, empl_patronymic, empl_role, salary, date_of_birth, " +
                "date_of_start, phone_number, city, street, zip_code FROM \"Employee\" WHERE id_employee = ?";
    }
    @Override
    protected String deleteQuery() {
        return "DELETE FROM \"Employee\" WHERE id_employee = ?";
    }
    @Override
    protected String countQuery() {
        return "SELECT COUNT(*) FROM \"Employee\"";
    }
    public ArrayList<Employee> getAllBy(String empl_role, String empl_surname, String phone_number) {
        StringBuilder queryBuilder = new StringBuilder("SELECT * FROM \"Employee\" WHERE 1=1");
        MapSqlParameterSource parameters = new MapSqlParameterSource();

        if (empl_role != null) {

            queryBuilder.append(" AND empl_role = CAST(:empl_role AS \"Roles\")");
            parameters.addValue("empl_role", empl_role);
        }

        if (empl_surname != null) {
            queryBuilder.append(" AND empl_surname LIKE :empl_surname");
            parameters.addValue("empl_surname", "%" + empl_surname + "%");
        }

        if (phone_number != null) {
            queryBuilder.append(" AND phone_number LIKE :phone_number");
            parameters.addValue("phone_number", "%" + phone_number + "%");
        }
        queryBuilder.append(";");
        String filterSelect = queryBuilder.toString();


        return (ArrayList<Employee>) namedJdbcTemplate.query(filterSelect, parameters, rowMapper);
    }
    public List<String> getRoles() {
        String sql = "SELECT DISTINCT empl_role FROM \"Employee\"";
        return namedJdbcTemplate.getJdbcTemplate()
                .queryForList(sql, String.class);
    }
    public boolean existsByPhoneNumber(String phoneNumber) {
        String sql = "SELECT COUNT(*) FROM \"Employee\" WHERE phone_number = ?";
        Integer count = namedJdbcTemplate.getJdbcTemplate().queryForObject(sql, Integer.class, phoneNumber);
        return count > 0;
    }
    public boolean existsByPhoneNumber(String phoneNumber, UUID id) {
        String sql = "SELECT COUNT(*) FROM \"Employee\" WHERE phone_number = ? AND id_employee != ?";
        Integer count = namedJdbcTemplate.getJdbcTemplate().queryForObject(sql, Integer.class, new Object[]{phoneNumber, id});
        return count > 0;
    }
    public boolean existsById(UUID id) {
        String sql = "SELECT COUNT(*) FROM \"Employee\" WHERE id_employee = ?";
        int count = namedJdbcTemplate.getJdbcTemplate().queryForObject(sql, Integer.class, id);
        return count > 0;
    }
    public boolean existsChecksLinkedTo(UUID id) {
        String sql = "SELECT COUNT(*) FROM \"Check\" WHERE id_employee = ?";
        int count = namedJdbcTemplate.getJdbcTemplate().queryForObject(sql, Integer.class, id);
        return count > 0;
    }
    public UUID getIdByPhone(String phoneNumber) {
    String sql = "SELECT id_employee FROM \"Employee\" WHERE phone_number = ?";
        return namedJdbcTemplate.getJdbcTemplate().queryForObject(sql, UUID.class, phoneNumber);
    }
    public List<Employee> getCashiersWithNumberOfChecks() {
        String sql = "SELECT " +
                "    e.id_employee, " +
                "    e.empl_surname, " +
                "    e.empl_name, " +
                "    e.phone_number, " +
                "    COUNT(c.check_number) AS number_of_checks " +
                "FROM " +
                "    public.\"Employee\" e " +
                "LEFT JOIN " +
                "    public.\"Check\" c ON e.id_employee = c.id_employee " +
                "WHERE " +
                "    e.empl_role = 'Cashier' " +
                "GROUP BY " +
                "    e.id_employee, e.empl_surname, e.empl_name, e.phone_number " +
                "ORDER BY " +
                "    number_of_checks DESC;";

        List<Map<String, Object>> rows = namedJdbcTemplate.getJdbcTemplate().queryForList(sql);
        List<Employee> employees = new ArrayList<>();
        for (Map<String, Object> row : rows) {
            Employee employee = new Employee();

            employee.setId((UUID) row.get("id_employee"));
            employee.setEmpl_surname((String) row.get("empl_surname"));
            employee.setEmpl_name((String) row.get("empl_name"));
            employee.setPhone_number((String) row.get("phone_number"));
            Long numberOfChecks = (Long) row.get("number_of_checks");
            employee.setNumberOfChecks(numberOfChecks);
            employees.add(employee);
        }

        return employees;
    }

}
