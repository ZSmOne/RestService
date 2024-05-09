package org.rest.repository;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.rest.model.City;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.stream.StreamSupport;


@ExtendWith(SpringExtension.class)
class CityRepositoryImplTest extends TestDatabaseHelper {

    @Autowired
    public  CityCrudRepository cityRepository;

    @Test
    void save() {
        City city = new City(null, "New York");
        City savedCity = cityRepository.save(city);
        Assertions.assertNotNull(savedCity);
    }

    @DisplayName("Find by ID")
    @ParameterizedTest
    @CsvSource(value = {
            "1, true",
            "3, true",
            "100, false"
    })
    void findById(Long expectedId, Boolean expectedValue) {
        City city = cityRepository.findById(expectedId).orElse(null);
        Assertions.assertEquals(expectedValue, city != null);
        if (city != null)
            Assertions.assertEquals(expectedId, city.getId());
    }


    @Test
    void  findAll() {
        int resultSize = StreamSupport.stream(cityRepository.findAll().spliterator(), false)
                .toList().size();
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
        boolean isCityExist = cityRepository.existsById(cityId);
        Assertions.assertEquals(expectedValue, isCityExist);
    }


    @Test
    void deleteById() {
        Boolean expectedValue = false;
        int expectedSize = StreamSupport.stream(cityRepository.findAll().spliterator(), false)
                .toList().size();
        City tempCity = new City(null, "City for delete.");
        tempCity = cityRepository.save(tempCity);
        cityRepository.deleteById(tempCity.getId()) ;
        boolean resultDelete = cityRepository.existsById(tempCity.getId()) ;
        int cityListAfterSize = StreamSupport.stream(cityRepository.findAll().spliterator(), false)
                .toList().size();

        Assertions.assertEquals(expectedValue, resultDelete);
        Assertions.assertEquals(expectedSize, cityListAfterSize);
    }
}
