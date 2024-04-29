package org.vlad.rest.repository;


import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.rest.model.City;
import org.rest.repository.CityRepository;
import org.rest.repository.impl.CityRepositoryImpl;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.sql.DataSource;

import static org.junit.Assert.assertNotNull;

@Testcontainers
@Tag("DockerRequired")
public class CityRepositoryImplTest {
    private static final String INIT_SQL = "sql/schema.sql";
    public static CityRepository cityRepository;


    @Container
    public static PostgreSQLContainer<?> container = new PostgreSQLContainer<>("postgres:16.2")
            .withDatabaseName("test")
            .withUsername("username")
            .withPassword("db.password")
            .withInitScript(INIT_SQL);

    @BeforeAll
    static void beforeAll() {
        container.start();
        DataSource dataSource = dataSource();
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        cityRepository = new CityRepositoryImpl(jdbcTemplate);
    }

    @AfterAll
    static void afterAll() {
        container.stop();
    }

    @Test
    void save() {

        City city = new City(null, "New York");
        City svedCity = cityRepository.save(city);
        Assertions.assertNotNull(svedCity);
    }

    @Test
    void update() {
        String expectedName = "update city";
        City cityForUpdate = cityRepository.findById(3L);
        String oldCityName = cityForUpdate.getName();
        cityForUpdate.setName(expectedName);
        cityRepository.update(cityForUpdate);
        City city = cityRepository.findById(3L);

        Assertions.assertNotEquals(expectedName, oldCityName);
        Assertions.assertEquals(expectedName, city.getName());
    }

    @Test
    void deleteById() {
        Boolean expectedValue = true;
        int expectedSize = cityRepository.findAll().size();
        City tempCity = new City(null, "City for delete.");
        tempCity = cityRepository.save(tempCity);
        boolean resultDelete = cityRepository.deleteById(tempCity.getId());
        int cityListAfterSize = cityRepository.findAll().size();

        Assertions.assertEquals(expectedValue, resultDelete);
        Assertions.assertEquals(expectedSize, cityListAfterSize);
    }


    @DisplayName("Find by ID")
    @ParameterizedTest
    @CsvSource(value = {
            "1",
            "3",
    })
    void findById(Long expectedId) {
        City city = cityRepository.findById(expectedId);
        Assertions.assertEquals(expectedId, city.getId());
    }


    @Test
    void  findAll() {
        int resultSize = cityRepository.findAll().size();
        int expectedSize = 3;

        Assertions.assertEquals(expectedSize, resultSize);
    }

    @DisplayName("Exist by ID")
    @ParameterizedTest
    @CsvSource(value = {
            "1; true",
            "3; true",
            "100; false"
    }, delimiter = ';')
    void exitsById(Long cityId, Boolean expectedValue) {
        boolean isCityExist = cityRepository.existById(cityId);
        Assertions.assertEquals(expectedValue, isCityExist);
    }

    public static DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl(container.getJdbcUrl());
        dataSource.setUsername(container.getUsername());
        dataSource.setPassword(container.getPassword());
        return dataSource;
    }
}
