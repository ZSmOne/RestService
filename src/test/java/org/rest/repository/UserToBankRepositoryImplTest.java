package org.rest.repository;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.rest.model.UserToBank;
import org.rest.repository.impl.UserToBankRepositoryImpl;
import org.rest.util.PropertiesUtil;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.ext.ScriptUtils;
import org.testcontainers.jdbc.JdbcDatabaseDelegate;
import org.testcontainers.junit.jupiter.Container;

public class UserToBankRepositoryImplTest {
    private static final String INIT_SQL = "sql/schema.sql";
    public static UserToBankRepository userToBankRepository;

    @Container
    public static PostgreSQLContainer<?> container = new PostgreSQLContainer<>("postgres:16.2")
            .withDatabaseName("test")
            .withUsername(PropertiesUtil.getProperties("db.username"))
            .withPassword(PropertiesUtil.getProperties("db.password"))
            .withInitScript(INIT_SQL);
    private static JdbcDatabaseDelegate jdbcDatabaseDelegate;

    @BeforeAll
    static void beforeAll() {
        container.start();
        userToBankRepository = new UserToBankRepositoryImpl(container.getJdbcUrl(), container.getUsername(), container.getPassword());
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
        Long expectedUserId = 1L;
        Long expectedBankId = 4L;
        UserToBank userToBank = new UserToBank(null, expectedUserId, expectedBankId);
        userToBank = userToBankRepository.save(userToBank);
        UserToBank resultUserToBank = userToBankRepository.findById(userToBank.getId());

        Assertions.assertNotNull(resultUserToBank);
        Assertions.assertEquals(expectedUserId, resultUserToBank.getUserId());
        Assertions.assertEquals(expectedBankId, resultUserToBank.getBankId());
    }

    @Test
    void update() {
        Long expectedUserId = 1L;
        Long expectedBankId = 4L;
        UserToBank userToBank = userToBankRepository.findById(2L);
        Long oldUserId = userToBank.getUserId();
        Long oldBankId = userToBank.getBankId();

        Assertions.assertNotEquals(expectedBankId, oldBankId);

        userToBank.setUserId(expectedUserId);
        userToBank.setBankId(expectedBankId);
        userToBankRepository.update(userToBank);
        UserToBank resultuserToBank = userToBankRepository.findById(2L);

        Assertions.assertEquals(userToBank.getId(), resultuserToBank.getId());
        Assertions.assertEquals(expectedUserId, resultuserToBank.getUserId());
        Assertions.assertEquals(expectedBankId, resultuserToBank.getBankId());
    }

    @Test
    void deleteById() {
        Boolean expectedValue = true;
        int expectedSize = userToBankRepository.findAll().size();
        UserToBank userToBank = new UserToBank(null, 1L, 3L);
        userToBank = userToBankRepository.save(userToBank);
        boolean resultDelete = userToBankRepository.deleteById(userToBank.getId());
        int resultSizeAfter = userToBankRepository.findAll().size();

        Assertions.assertEquals(expectedValue, resultDelete);
        Assertions.assertEquals(expectedSize, resultSizeAfter);
    }



    @DisplayName("Delete_by_UserId")
    @ParameterizedTest
    @CsvSource(value = {
            "2, true",
            "1000, false"
    })
    void deleteByUserId(Long expectedUserId, Boolean expectedValue) {
        int beforeSize = userToBankRepository.findAllByUserId(expectedUserId).size();
        Boolean resultDelete = userToBankRepository.deleteByUserId(expectedUserId);
        int afterDelete = userToBankRepository.findAllByUserId(expectedUserId).size();
        Assertions.assertEquals(expectedValue, resultDelete);
        if (beforeSize != 0)
            Assertions.assertNotEquals(beforeSize, afterDelete);
        }


    @DisplayName("Delete by Bank Id")
    @ParameterizedTest
    @CsvSource(value = {
            "2, true",
            "1000, false"
    })
    void deleteByBankId(Long expectedBankId, Boolean expectedValue) {
        int beforeSize = userToBankRepository.findAllByBankId(expectedBankId).size();
        Boolean resultDelete = userToBankRepository.deleteByBankId(expectedBankId);
        int afterDelete = userToBankRepository.findAllByBankId(expectedBankId).size();
        Assertions.assertEquals(expectedValue, resultDelete);
        if (beforeSize != 0) {
            Assertions.assertNotEquals(beforeSize, afterDelete);
        }
    }

    @DisplayName("Find by Id")
    @ParameterizedTest
    @CsvSource(value = {
            "1, true, 1, 1",
            "4, true, 1, 2",
            "1000, false, 0, 0"
    })
    void findById(Long expectedId, Boolean expectedValue, Long expectedUserId, Long expectedBankId) {
        UserToBank userToBank = userToBankRepository.findById(expectedId);
        Assertions.assertEquals(expectedValue, userToBank != null);
        if (userToBank != null) {
            Assertions.assertEquals(expectedId, userToBank.getId());
            Assertions.assertEquals(expectedUserId, userToBank.getUserId());
            Assertions.assertEquals(expectedBankId, userToBank.getBankId());
        }
    }

    @Test
    void findAll() {
        int expectedSize = 5;
        int resultSize = userToBankRepository.findAll().size();

        Assertions.assertEquals(expectedSize, resultSize);
    }

    @DisplayName("Exist by Id")
    @ParameterizedTest
    @CsvSource(value = {
            "1, true",
            "3, true",
            "1000, false"
    })
    void exitsById(Long expectedId, Boolean expectedValue) {
        Boolean resultValue = userToBankRepository.existById(expectedId);

        Assertions.assertEquals(expectedValue, resultValue);
    }

    @DisplayName("Find by user Id")
    @ParameterizedTest
    @CsvSource(value = {
            "1, 2",
            "3, 1",
            "1000, 0"
    })
    void findAllByUserId(Long userId, int expectedSize) {
        int resultSize = userToBankRepository.findAllByUserId(userId).size();

        Assertions.assertEquals(expectedSize, resultSize);
    }

    @DisplayName("Find banks by user Id")
    @ParameterizedTest
    @CsvSource(value = {
            "1, 2",
            "3, 1",
            "1000, 0"
    })
    void findBanksByUserId(Long userId, int expectedSize) {
        int resultSize = userToBankRepository.findBanksByUserId(userId).size();

        Assertions.assertEquals(expectedSize, resultSize);
    }

    @DisplayName("Find Bank by user Id.")
    @ParameterizedTest
    @CsvSource(value = {
            "1, 1",
            "2, 2",
            "3, 1",
            "1000, 0"
    })
    void findAllByBankId(Long bankId, int expectedSize) {
        int resultSize = userToBankRepository.findAllByBankId(bankId).size();

        Assertions.assertEquals(expectedSize, resultSize);
    }

    @DisplayName("Find Users by Bank Id.")
    @ParameterizedTest
    @CsvSource(value = {
            "2, 2",
            "4, 1",
            "1000, 0"
    })
    void findUsersByBankId(Long bankId, int expectedSize) {
        int resultSize = userToBankRepository.findUsersByBankId(bankId).size();

        Assertions.assertEquals(expectedSize, resultSize);
    }

    @DisplayName("Find Users by bank Id.")
    @ParameterizedTest
    @CsvSource(value = {
            "1, 1, true",
            "1, 3, false"
    })
    void findByUserIdAndBankId(Long userId, Long bankId, Boolean expectedValue) {
        UserToBank userToBank = userToBankRepository.findByUserIdAndBankId(userId, bankId);

        Assertions.assertEquals(expectedValue, userToBank != null);
    }
}
