package org.rest.repository;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.rest.model.Bank;
import org.rest.model.City;
import org.rest.model.User;
import org.rest.repository.impl.UserRepositoryImpl;
import org.rest.util.PropertiesUtil;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.ext.ScriptUtils;
import org.testcontainers.jdbc.JdbcDatabaseDelegate;
import org.testcontainers.junit.jupiter.Container;

import java.util.List;

public class UserRepositoryImplTest {
    private static final String INIT_SQL = "sql/schema.sql";
    @Container
    public static PostgreSQLContainer<?> container = new PostgreSQLContainer<>("postgres:15-alpine")
            .withDatabaseName("test")
            .withUsername(PropertiesUtil.getProperties("db.username"))
            .withPassword(PropertiesUtil.getProperties("db.password"))

            .withInitScript(INIT_SQL);
    public static UserRepository userRepository;
    private static JdbcDatabaseDelegate jdbcDatabaseDelegate;


    @BeforeAll
    static void beforeAll() {
        container.start();
        userRepository = new UserRepositoryImpl(container.getJdbcUrl(), container.getUsername(), container.getPassword());
        jdbcDatabaseDelegate = new JdbcDatabaseDelegate(container, "");
    }

    @AfterAll
    static void afterAll() {
        container.stop();
    }

    @BeforeEach
    void setUp() {
        ScriptUtils.runInitScript(jdbcDatabaseDelegate, INIT_SQL);
    }

    @Test
    void save() {
        String expectedName = "new name";
        City city = new City(1L, "Moscow");
        User user = new User(null, expectedName, city, List.of());
        user = userRepository.save(user);
        User resultUser = userRepository.findById(user.getId());

        Assertions.assertNotNull(resultUser);
        Assertions.assertEquals(expectedName, resultUser.getName());
    }

    @Test
    void update() {
        String expectedName = "update name";
        Long expectedCityId = 1L;
        User userUpdate = userRepository.findById(3L);

        City oldCity = userUpdate.getCity();
        List<Bank> bankList = userUpdate.getBankList();
        int BankListSize = userUpdate.getBankList().size();

        Assertions.assertNotEquals(expectedCityId, userUpdate.getCity().getId());
        Assertions.assertNotEquals(expectedName, userUpdate.getName());

        userUpdate.setName(expectedName);
        userRepository.update(userUpdate);
        User resultUser = userRepository.findById(3L);

        Assertions.assertEquals(expectedName, resultUser.getName());
        Assertions.assertEquals(BankListSize, resultUser.getBankList().size());
        Assertions.assertEquals(oldCity.getId(), resultUser.getCity().getId());

        userUpdate.setBankList(List.of());
        userUpdate.setCity(new City(expectedCityId, null));
        userRepository.update(userUpdate);
        resultUser = userRepository.findById(3L);

        Assertions.assertEquals(1, resultUser.getBankList().size());
        Assertions.assertEquals(expectedCityId, resultUser.getCity().getId());

        bankList.add(new Bank(3L, null, null));
        bankList.add(new Bank(4L, null, null));
        userUpdate.setBankList(bankList);
        userRepository.update(userUpdate);
        resultUser = userRepository.findById(3L);

        Assertions.assertEquals(1, resultUser.getBankList().size());

        bankList.remove(2);
        userUpdate.setBankList(bankList);
        userRepository.update(userUpdate);
        resultUser = userRepository.findById(3L);

        Assertions.assertEquals(1, resultUser.getBankList().size());

        userUpdate.setBankList(List.of(new Bank(1L, null, null)));

        userRepository.update(userUpdate);
        resultUser = userRepository.findById(3L);

        Assertions.assertEquals(1, resultUser.getBankList().size());
    }

    @Test
    void deleteById() {
        Boolean expectedValue = true;
        int expectedSize = userRepository.findAll().size();

        City city = new City(1L, "Moscow");
        User User = new User(null, "User for delete name", city, null);
        User = userRepository.save(User);
        boolean resultDelete = userRepository.deleteById(User.getId());
        int CityListSize = userRepository.findAll().size();

        Assertions.assertEquals(expectedValue, resultDelete);
        Assertions.assertEquals(expectedSize, CityListSize);
    }

    @DisplayName("Find by ID")
    @ParameterizedTest
    @CsvSource(value = {
            "1; true",
            "3; true",
            "100; false"
    }, delimiter = ';')
    void findById(Long expectedId, Boolean expectedValue) {
        User user = userRepository.findById(expectedId);
        Assertions.assertEquals(expectedValue, user != null);
        if (user != null)
            Assertions.assertEquals(expectedId, user.getId());
    }

    @Test
    void findAll() {
        int expectedSize = 3;
        int resultSize = userRepository.findAll().size();

        Assertions.assertEquals(expectedSize, resultSize);
    }

    @DisplayName("Exist by ID")
    @ParameterizedTest
    @CsvSource(value = {
            "1; true",
            "3; true",
            "100; false"
    }, delimiter = ';')
    void exitsById(Long CityId, Boolean expectedValue) {
        boolean isUserExist = userRepository.existById(CityId);

        Assertions.assertEquals(expectedValue, isUserExist);
    }
}
