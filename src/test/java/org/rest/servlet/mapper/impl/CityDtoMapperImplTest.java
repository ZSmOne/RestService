package org.rest.servlet.mapper.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.rest.model.City;
import org.rest.servlet.dto.CityIncomingDto;
import org.rest.servlet.dto.CityOutGoingDto;
import org.rest.servlet.dto.CityUpdateDto;
import org.rest.servlet.mapper.CityDtoMapper;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CityDtoMapperImplTest {

    private CityDtoMapper cityDtoMapper;

    @BeforeEach
    public void setUp() {
        cityDtoMapper = CityDtoMapperImpl.getInstance();
    }

    @Test
    void map_CityIncomingDto_ReturnsCity() {
        CityIncomingDto dto = new CityIncomingDto("TestCity");
        City result = cityDtoMapper.map(dto);

        assertNotNull(result);
        assertEquals("TestCity", result.getName());
        assertNull(result.getId());
    }

    @Test
    void map_CityUpdateDto_ReturnsCity() {
        CityUpdateDto updateDto = new CityUpdateDto(1L, "UpdatedCityName");
        City result = cityDtoMapper.map(updateDto);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("UpdatedCityName", result.getName());
    }

    @Test
    void map_City_ReturnsCityOutGoingDto() {
        City city = new City(1L, "TestCity");
        CityOutGoingDto result = cityDtoMapper.map(city);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("TestCity", result.getName());
    }

    @Test
    void map_ListOfCities_ReturnsListOfCityOutGoingDto() {
        List<City> cityList = Arrays.asList(new City(1L, "City1"), new City(2L, "City2"));
        List<CityOutGoingDto> result = cityDtoMapper.map(cityList);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals("City1", result.get(0).getName());
        assertEquals(2L, result.get(1).getId());
        assertEquals("City2", result.get(1).getName());
    }

}
