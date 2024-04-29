package org.rest.repository.impl;

import org.rest.model.Bank;
import org.rest.repository.BankRepository;
import org.rest.repository.UserToBankRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.List;

@Repository
public class BankRepositoryImpl implements BankRepository {

    private static final String SAVE_SQL = """
            INSERT INTO banks (bank_name)
            VALUES (?);
            """;
    private static final String FIND_BY_ID_SQL = """
            SELECT * FROM banks
            WHERE bank_id = ?
            LIMIT 1;
            """;
    private static final String FIND_ALL_SQL = """
            SELECT * FROM banks;
            """;
    private static final String UPDATE_SQL = """
            UPDATE banks
            SET bank_name = ?
            WHERE bank_id = ?;
            """;
    private static final String DELETE_SQL = """
            DELETE FROM banks
            WHERE bank_id = ?;
            """;
    private static final String EXIST_BY_ID_SQL = """
                SELECT exists (
                SELECT 1
                    FROM banks
                        WHERE bank_id = ?
                        LIMIT 1);
            """;

    private final UserToBankRepository userToBankRepository;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public BankRepositoryImpl(UserToBankRepository userToBankRepository, JdbcTemplate jdbcTemplate) {
        this.userToBankRepository = userToBankRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Bank save(Bank bank) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(SAVE_SQL, new String[]{"bank_id"});
            ps.setString(1, bank.getName());
            return ps;
        }, keyHolder);
        Long id = keyHolder.getKey().longValue();
        bank.setId(id);
        return bank;
    }

    @Override
    public void update(Bank bank) {
        jdbcTemplate.update(UPDATE_SQL, bank.getName(), bank.getId());
    }

    @Override
    public Bank findById(Long id) {
        return jdbcTemplate.queryForObject(FIND_BY_ID_SQL, (rs, rowNum) ->
                new Bank(rs.getLong("bank_id"), rs.getString("bank_name"),
                        userToBankRepository.findUsersByBankId(rs.getLong("bank_id"))), id);
    }

    @Override
    public boolean deleteById(Long id) {
        userToBankRepository.deleteByBankId(id);
        return jdbcTemplate.update(DELETE_SQL, id) > 0;
    }

    @Override
    public List<Bank> findAll() {
        return jdbcTemplate.query(FIND_ALL_SQL, (rs, rowNum) ->
                new Bank(rs.getLong("bank_id"), rs.getString("bank_name"),
                        userToBankRepository.findUsersByBankId(rs.getLong("bank_id"))));
    }

    @Override
    public boolean existById(Long id) {
        return jdbcTemplate.queryForObject(EXIST_BY_ID_SQL, Boolean.class, id);
    }
}

