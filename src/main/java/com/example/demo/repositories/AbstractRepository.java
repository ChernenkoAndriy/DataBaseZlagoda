package com.example.demo.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public abstract class AbstractRepository<T, TPK>{
    protected final NamedParameterJdbcTemplate namedJdbcTemplate;
    protected RowMapper<T> rowMapper;

    protected abstract String saveAllQuery();
    protected abstract String updateQuery();
    protected abstract String findAllQuery();
    protected abstract String findByIdQuery();
    protected abstract String deleteQuery();
    protected abstract String countQuery();
    @Autowired
    public AbstractRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.namedJdbcTemplate = jdbcTemplate;
    }
    public int save(T e) {
        String sql = saveAllQuery();
        SqlParameterSource namedParameters = new BeanPropertySqlParameterSource(e);
        return namedJdbcTemplate.update(sql, namedParameters);
    }
    public int update(T e) {
        String sql = updateQuery();
        SqlParameterSource namedParameters = new BeanPropertySqlParameterSource(e);
        return namedJdbcTemplate.update(sql, namedParameters);
    }
    public List<T> findAll() {
        String sql = findAllQuery();
        return namedJdbcTemplate.query(sql, rowMapper);
    }
    public T findById(TPK id) {
        String sql = findByIdQuery();
        return namedJdbcTemplate.getJdbcTemplate().queryForObject(sql, rowMapper, id);
    }
    public int delete(TPK id) {
        String sql = deleteQuery();
        return namedJdbcTemplate.getJdbcTemplate().update(sql, id);
    }
    public int count() {
        return namedJdbcTemplate.getJdbcTemplate().queryForObject(countQuery(), Integer.class);
    }

}
