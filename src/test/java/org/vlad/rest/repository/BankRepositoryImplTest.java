package org.vlad.rest.repository;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.rest.model.Bank;
import org.rest.repository.BankRepository;
import org.rest.repository.UserToBankRepository;
import org.rest.repository.impl.BankRepositoryImpl;
import org.rest.repository.impl.UserToBankRepositoryImpl;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.jdbc.JdbcDatabaseDelegate;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.sql.DataSource;
import java.util.List;

@Testcontainers
@Tag("DockerRequired")
public class BankRepositoryImplTest {
    private static final String INIT_SQL = "sql/schema.sql";
    public static BankRepository bankRepository;
    public static UserToBankRepository userToBankRepository;

    @Container
    public static PostgreSQLContainer<?> container = new PostgreSQLContainer<>("postgres:16.2")
            .withDatabaseName("test")
            .withUsername("username")
            .withPassword("db.password")
            .withInitScript(INIT_SQL);

    @BeforeAll
    static void beforeAll() {
        container.start();
        JdbcDatabaseDelegate jdbcDatabaseDelegate = new JdbcDatabaseDelegate(container, "");
        DataSource dataSource = dataSource();
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        userToBankRepository = new UserToBankRepositoryImpl(jdbcTemplate);
        bankRepository = new BankRepositoryImpl(userToBankRepository ,jdbcTemplate);    }

    @AfterAll
    static void afterAll() {
        container.stop();
    }



    @Test
    void save() {
        String expectedName = "new bank name";
        Bank bank = new Bank(null, expectedName, null);
        bank = bankRepository.save(bank);
        Bank resultBank = bankRepository.findById(bank.getId());

        Assertions.assertNotNull(resultBank);
        Assertions.assertEquals(expectedName, resultBank.getName());

    }

    @Test
    void update() {
        String expectedName = "Update bank name";

        Bank bank = bankRepository.findById(2L);
        String oldName = bank.getName();
        bank.setName(expectedName);
        bankRepository.update(bank);

        Bank resultBank = bankRepository.findById(2L);
        Assertions.assertNotEquals(expectedName, oldName);
        Assertions.assertEquals(expectedName, resultBank.getName());
    }

    @DisplayName("Find by ID")
    @ParameterizedTest
    @CsvSource(value = {
            "1, true",
            "3, true"
    })
    void findById(Long expectedId , Boolean expectedValue) {
        Bank bank = bankRepository.findById(expectedId);
        Assertions.assertEquals(expectedValue, bank != null);
        if (bank != null)
            Assertions.assertEquals(expectedId, bank.getId());
    }

    @Test
    void findAll() {
        int expectedSize = 4;
        int resultSize = bankRepository.findAll().size();
        Assertions.assertEquals(expectedSize, resultSize);
    }

    @DisplayName("Exist by ID")
    @ParameterizedTest
    @CsvSource(value = {
            "1; true",
            "3; true",
            "100; false"
    }, delimiter = ';')
    void exitsById(Long bankId, Boolean expectedValue) {
        boolean isCityExist = bankRepository.existById(bankId);
        Assertions.assertEquals(expectedValue, isCityExist);
    }
    @Test
    void deleteById() {
        Boolean expectedValue = true;
        int expectedSize = bankRepository.findAll().size();
        Bank bank = new Bank(null, "New bank", List.of());
        bank = bankRepository.save(bank);
        int resultSizeBefore = bankRepository.findAll().size();
        Assertions.assertNotEquals(expectedSize, resultSizeBefore);
        boolean resultDelete = bankRepository.deleteById(bank.getId());
        int resultSizeAfter = bankRepository.findAll().size();

        Assertions.assertEquals(expectedValue, resultDelete);
        Assertions.assertEquals(expectedSize, resultSizeAfter);
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
