package org.rest.repository.impl;

import org.rest.exception.RepositoryException;
import org.rest.model.User;
import org.rest.repository.CityRepository;
import org.rest.repository.UserRepository;
import org.rest.repository.UserToBankRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private static final String SAVE_SQL = """
            INSERT INTO users (user_name, city_id)
            VALUES (?, ?) 
            RETURNING user_id;
            """;
    private static final String FIND_BY_ID_SQL = """
            SELECT * FROM users
            WHERE user_id = ?
            LIMIT 1;
            """;
    private static final String UPDATE_SQL = """
            UPDATE users
            SET user_name = ?,
                city_id =?
            WHERE user_id = ?  ;
            """;
    private static final String DELETE_SQL = """
            DELETE FROM users
            WHERE user_id = ? ;
            """;
    private static final String FIND_ALL_SQL = """
            SELECT * FROM users;
            """;
    private static final String EXIST_BY_ID_SQL = """
                SELECT exists (
                SELECT 1
                    FROM users
                        WHERE user_id = ?
                        LIMIT 1);
            """;
    private CityRepository cityRepository;
    private UserToBankRepository userToBankRepository;
    private JdbcTemplate jdbcTemplate;


    @Autowired
    public UserRepositoryImpl(CityRepositoryImpl cityRepository, UserToBankRepository userToBankRepository,JdbcTemplate jdbcTemplate) {
        this.cityRepository = cityRepository;
        this.userToBankRepository = userToBankRepository;
        this.jdbcTemplate = jdbcTemplate;
    }
    public UserRepositoryImpl() {
    }

    @Override
    public User save(User user) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(SAVE_SQL, new String[]{"user_id"});
            ps.setString(1, user.getName());
            ps.setLong(2, user.getCity().getId());
            return ps;
        }, keyHolder);

        Long id = keyHolder.getKey().longValue();
        user.setId(id);

        return user;
    }

    @Override
    public User findById(Long id) {
        return jdbcTemplate.queryForObject(FIND_BY_ID_SQL, (rs, rowNum) ->
                new User(rs.getLong("user_id"), rs.getString("user_name"),
                        cityRepository.findById(rs.getLong("city_id")),
                        userToBankRepository.findBanksByUserId(rs.getLong("user_id"))), id);
    }

    @Override
    public List<User> findAll() {
        return jdbcTemplate.query(FIND_ALL_SQL,(rs, rowNum) ->
                new User(rs.getLong("user_id"), rs.getString("user_name"),
                        cityRepository.findById(rs.getLong("city_id")),
                        userToBankRepository.findBanksByUserId(rs.getLong("user_id"))));
    }

    @Override
    public void update(User user) {
        jdbcTemplate.update(UPDATE_SQL, user.getName(), user.getCity().getId(), user.getId());
    }

    @Override
    public boolean deleteById(Long id) {
        userToBankRepository.deleteByUserId(id);
        return jdbcTemplate.update(DELETE_SQL, id) > 0;
    }

    @Override
    public boolean existById(Long id) {
        try {
            return jdbcTemplate.queryForObject(EXIST_BY_ID_SQL, Boolean.class, id);
        } catch (Exception e) {
            throw new RepositoryException(String.valueOf(e));
        }
    }
}
