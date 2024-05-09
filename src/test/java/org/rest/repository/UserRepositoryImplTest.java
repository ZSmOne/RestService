package org.rest.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.rest.model.City;
import org.rest.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.stream.StreamSupport;

@ExtendWith(SpringExtension.class)
class UserRepositoryImplTest extends TestDatabaseHelper {

    @Autowired
    public UserCrudRepository userRepository;

    @Test
    void save() {
        String expectedName = "new name";
        City city = new City(1L, "Moscow");
        User user = new User(null, expectedName, city, List.of());
        user = userRepository.save(user);
        User resultUser = userRepository.findById(user.getId()).orElseThrow();

        Assertions.assertNotNull(resultUser);
        Assertions.assertEquals(expectedName, resultUser.getName());
    }

    @DisplayName("Find by ID")
    @ParameterizedTest
    @CsvSource(value = {
            "1; true",
            "3; true",
            "100; false"
    }, delimiter = ';')
    void findById(Long expectedId, Boolean expectedValue) {
        User user = userRepository.findById(expectedId).orElse(null);
        Assertions.assertEquals(expectedValue, user != null);
        if (user != null)
            Assertions.assertEquals(expectedId, user.getId());

    }

    @Test
    void findAll() {
        int expectedSize = 3;
        int resultSize = StreamSupport.stream(userRepository.findAll().spliterator(), false)
                        .toList().size();

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
        boolean isUserExist = userRepository.existsById(CityId);

        Assertions.assertEquals(expectedValue, isUserExist);
    }

    @Test
    void deleteById() {
        Boolean expectedValue = false;
        int expectedSize = StreamSupport.stream(userRepository.findAll().spliterator(), false)
                .toList().size();
        User User = new User(null, "User for delete", null, null);
        User = userRepository.save(User);
        userRepository.deleteById(User.getId()) ;
        boolean resultDelete = userRepository.existsById(User.getId()) ;
        int userListAfterSize = StreamSupport.stream(userRepository.findAll().spliterator(), false)
                .toList().size();
        Assertions.assertEquals(expectedValue, resultDelete);
        Assertions.assertEquals(expectedSize, userListAfterSize);
    }
}
