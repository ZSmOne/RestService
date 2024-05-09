package org.rest.servlet.mapper;

import org.junit.jupiter.api.Test;
import org.rest.model.City;
import org.rest.servlet.city.dto.CityIncomingDto;
import org.rest.servlet.city.dto.CityOutGoingDto;
import org.rest.servlet.city.dto.CityUpdateDto;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CityMapperTest {

    @Test
    void testCityIncomingDtoToCity() {
        CityIncomingDto dto = new CityIncomingDto();
        dto.setName("Test City");
        City city = CityMapper.INSTANCE.cityIncomingDtoToCity(dto);
        assertEquals(dto.getName(), city.getName());
    }

    @Test
    void testCityUpdateDtoToCity() {
        CityUpdateDto dto = new CityUpdateDto(1L, "Updated City");
        City city = CityMapper.INSTANCE.cityUpdateDtoToCity(dto);
        assertEquals(dto.getId(), city.getId());
        assertEquals(dto.getName(), city.getName());
    }

    @Test
    void testCityToCityOutGoingDto() {
        City city = new City(1L, "Test City");
        CityOutGoingDto dto = CityMapper.INSTANCE.cityToCityOutGoingDto(city);
        assertEquals(city.getId(), dto.getId());
        assertEquals(city.getName(), dto.getName());
    }

    @Test
    void testListCityToCityOutGoingDto() {
        List<City> cities = new ArrayList<>();
        cities.add(new City(1L, "City 1"));
        cities.add(new City(2L, "City 2"));
        List<CityOutGoingDto> dtos = CityMapper.INSTANCE.listCityToCityOutGoingDto(cities);
        assertEquals(cities.size(), dtos.size());
        for (int i = 0; i < cities.size(); i++) {
            assertEquals(cities.get(i).getId(), dtos.get(i).getId());
            assertEquals(cities.get(i).getName(), dtos.get(i).getName());
        }
    }
}
