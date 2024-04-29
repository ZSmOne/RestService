package org.rest.repository.impl;

import org.rest.model.City;
import org.rest.repository.CityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;

@Repository
public class CityRepositoryImpl implements CityRepository {
    private static final String SAVE_SQL = """
            INSERT INTO cities (city_name)
            VALUES (?)
            RETURNING city_id;
            """;
    private static final String FIND_BY_ID_SQL = """
            SELECT * FROM cities
            WHERE city_id = ?
            LIMIT 1;
            """;
    private static final String FIND_ALL_SQL = """
            SELECT * FROM cities;
            """;
    private static final String UPDATE_SQL = """
            UPDATE cities
            SET city_name = ?
            WHERE city_id = ?;
            """;
    private static final String DELETE_SQL = """
            DELETE FROM cities
            WHERE city_id = ?;
            """;
    private static final String EXIST_BY_ID_SQL = """
                SELECT exists (
                SELECT 1
                    FROM cities
                        WHERE city_id = ?
                        LIMIT 1);
            """;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public CityRepositoryImpl(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public City save(City city) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(SAVE_SQL, new String[]{"city_id"});
            ps.setString(1, city.getName());
            return ps;
        }, keyHolder);
        Long id = keyHolder.getKey().longValue();
        city.setId(id);
        return city;
    }

    @Override
    public City findById(Long id) {
        return jdbcTemplate.queryForObject(FIND_BY_ID_SQL, (rs, rowNum) ->
                new City(rs.getLong("city_id"), rs.getString("city_name")), id);
    }

    @Override
    public List<City> findAll() {
        return jdbcTemplate.query(FIND_ALL_SQL, (rs, rowNum) ->
                new City(rs.getLong("city_id"), rs.getString("city_name")));
    }

    @Override
    public void update(City city) {
        jdbcTemplate.update(UPDATE_SQL, city.getName(), city.getId());
    }

    @Override
    public boolean deleteById(Long id) {
        return jdbcTemplate.update(DELETE_SQL, id) > 0;
    }

    @Override
    public boolean existById(Long id) {
        return jdbcTemplate.queryForObject(EXIST_BY_ID_SQL, Boolean.class, id);
    }

}
