package org.rest.repository;


import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.rest.model.City;
import org.rest.repository.impl.CityRepositoryImpl;
import org.rest.util.PropertiesUtil;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.jdbc.JdbcDatabaseDelegate;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@Tag("DockerRequired")
public class CityRepositoryImplTest {
    private static final String INIT_SQL = "sql/schema.sql";
    public static CityRepository cityRepository;


    @Container
    public static PostgreSQLContainer<?> container = new PostgreSQLContainer<>("postgres:16.2")
            .withDatabaseName("test")
            .withUsername("db.username")
            .withPassword(PropertiesUtil.getProperties("db.password"))
            .withInitScript(INIT_SQL);

    @BeforeAll
    static void beforeAll() {
        container.start();
        cityRepository = new CityRepositoryImpl(container.getJdbcUrl(), container.getUsername(), container.getPassword());
    }

    @AfterAll
    static void afterAll() {
        container.stop();
    }

    @Test
    void save() {
        String expectedName = "new city";
        City city = new City(null, expectedName);
        city = cityRepository.save(city);
        City resultCity = cityRepository.findById(city.getId());

        Assertions.assertEquals(expectedName, resultCity.getName());
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
            "1; true",
            "3; true",
    }, delimiter = ';')
    void findById(Long expectedId, Boolean expectedValue) {
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
}
