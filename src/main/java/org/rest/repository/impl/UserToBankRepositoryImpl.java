package org.rest.repository.impl;

import org.rest.model.Bank;
import org.rest.model.User;
import org.rest.model.UserToBank;
import org.rest.repository.UserToBankRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;

@Repository
public class UserToBankRepositoryImpl implements UserToBankRepository {
    private static final String SAVE_SQL = """
            INSERT INTO users_banks (user_id, bank_id)
            VALUES (?, ?);
            """;
    private static final String UPDATE_SQL = """
            UPDATE users_banks
            SET user_id = ?,
                bank_id = ?
            WHERE users_banks_id = ?;
            """;
    private static final String DELETE_SQL = """
            DELETE FROM users_banks
            WHERE users_banks_id = ? ;
            """;
    private static final String FIND_BY_ID_SQL = """
            SELECT * FROM users_banks
            WHERE users_banks_id = ?
            LIMIT 1;
            """;
    private static final String FIND_ALL_SQL = """
            SELECT * FROM users_banks;
            """;
    private static final String FIND_ALL_BY_USER_ID_SQL = """
            SELECT * FROM users_banks
            WHERE user_id = ?;
            """;
    private static final String FIND_ALL_BY_BANK_ID_SQL = """
            SELECT * FROM users_banks
            WHERE bank_id = ?;
            """;
    private static final String FIND_BY_USERID_AND_BANK_ID_SQL = """
            SELECT * FROM users_banks
            WHERE user_id = ? AND bank_id = ?
            LIMIT 1;
            """;
    private static final String FIND_BANKS_BY_USER_ID_SQL = """
            SELECT * FROM banks
            WHERE bank_id IN
            (SELECT bank_id FROM users_banks WHERE user_id = ?);
            """;
    private static final String FIND_USERS_BY_BANK_ID_SQL = """
    SELECT * FROM users 
    WHERE user_id IN 
    (SELECT user_id FROM users_banks WHERE bank_id = ?);
    """;
    private static final String DELETE_BY_USER_ID_SQL = """
            DELETE FROM users_banks
            WHERE user_id = ?;
            """;
    private static final String DELETE_BY_BANK_ID_SQL = """
            DELETE FROM users_banks
            WHERE bank_id = ?;
            """;
    private static final String EXIST_BY_ID_SQL = """
            SELECT exists (
            SELECT 1
            FROM users_banks
            WHERE users_banks_id = ?);
            """;

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserToBankRepositoryImpl(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public UserToBank save(UserToBank userToBank) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(SAVE_SQL, new String[]{"users_banks_id"});
            ps.setLong(1, userToBank.getUserId());
            ps.setLong(2, userToBank.getBankId());
            return ps;
        }, keyHolder);
        Long id = keyHolder.getKey().longValue();
        userToBank.setId(id);
        return userToBank;
    }

    @Override
    public UserToBank findById(Long id) {
        return jdbcTemplate.queryForObject(FIND_BY_ID_SQL, (rs, rowNum) ->
                new UserToBank(rs.getLong("users_banks_id"), rs.getLong("user_id"), rs.getLong("bank_id")), id);
    }

    @Override
    public List<UserToBank> findAll() {
        return jdbcTemplate.query(FIND_ALL_SQL, (rs, rowNum) ->
                new UserToBank(rs.getLong("users_banks_id"), rs.getLong("user_id"), rs.getLong("bank_id")));
    }

    @Override
    public List<UserToBank> findAllByBankId(Long bankId) {
        return jdbcTemplate.query(FIND_ALL_BY_BANK_ID_SQL, (rs, rowNum) ->
                new UserToBank(rs.getLong("users_banks_id"), rs.getLong("user_id"), rs.getLong("bank_id")), bankId);
    }

    @Override
    public List<Bank> findBanksByUserId(Long userId) {
        return jdbcTemplate.query(FIND_BANKS_BY_USER_ID_SQL, (rs, rowNum) -> {
            Bank bank = new Bank();
            bank.setId(rs.getLong("bank_id"));
            bank.setName(rs.getString("bank_name"));
            return bank;
        }, userId);
    }

    @Override
    public List<User> findUsersByBankId(Long bankId) {
        return jdbcTemplate.query(FIND_USERS_BY_BANK_ID_SQL, (rs, rowNum) -> {
            User user = new User();
            user.setId(rs.getLong("user_id"));
            user.setName(rs.getString("user_name"));
            user.setCity(null);
            return user;
        }, bankId);
    }

    @Override
    public void update(UserToBank userToBank) {
        jdbcTemplate.update(UPDATE_SQL, userToBank.getUserId(), userToBank.getBankId(), userToBank.getId());
    }

    @Override
    public boolean deleteById(Long id) {
        return jdbcTemplate.update(DELETE_SQL, id) > 0;
    }

    @Override
    public boolean existById(Long id) {
        return jdbcTemplate.queryForObject(EXIST_BY_ID_SQL, Integer.class, id) > 0;
    }

    @Override
    public boolean deleteByUserId(Long userId) {
        return jdbcTemplate.update(DELETE_BY_USER_ID_SQL, userId) > 0;
    }

    @Override
    public boolean deleteByBankId(Long bankId) {
        return jdbcTemplate.update(DELETE_BY_BANK_ID_SQL, bankId) > 0;
    }

    @Override
    public List<UserToBank> findAllByUserId(Long userId) {
        return jdbcTemplate.query(FIND_ALL_BY_USER_ID_SQL, (rs, rowNum) ->
                new UserToBank(rs.getLong("users_banks_id"), rs.getLong("user_id"), rs.getLong("bank_id")), userId);
    }

    @Override
    public UserToBank findByUserIdAndBankId(Long userId, Long bankId) {
        return jdbcTemplate.queryForObject(FIND_BY_USERID_AND_BANK_ID_SQL,  (rs, rowNum) ->
                new UserToBank(rs.getLong("users_banks_id"), rs.getLong("user_id"), rs.getLong("bank_id")), userId, bankId);
    }
}
