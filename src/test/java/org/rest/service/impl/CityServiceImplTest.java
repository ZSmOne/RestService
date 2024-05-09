package org.rest.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.rest.exception.NotFoundException;
import org.rest.model.City;
import org.rest.repository.CityCrudRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CityServiceImplTest {
    @Mock
    private CityCrudRepository cityRepository;

    @InjectMocks
    private CityServiceImpl cityService;



    @Test
    void testSaveCity() {
        City city = new City(1L, "TestCity");
        when(cityRepository.save(city)).thenReturn(city);
        City savedCity = cityService.save(city);

        assertNotNull(savedCity);
        assertEquals(city.getId(), savedCity.getId());
        assertEquals(city.getName(), savedCity.getName());
    }

    @Test
    void testFindCityById() throws NotFoundException {
        City city = new City(1L, "TestCity");
        when(cityRepository.findById(1L)).thenReturn(Optional.of(city));
        City foundCity = cityService.findById(1L);

        assertNotNull(foundCity);
        assertEquals(city.getId(), foundCity.getId());
        assertEquals(city.getName(), foundCity.getName());
    }

    @Test
    void testFindNonExistingCityById() {
        when(cityRepository.findById(1L)).thenReturn(null);
        assertThrows(NullPointerException.class, () -> cityService.findById(1L));
    }

    @Test
    void testFindAllCities() {
        List<City> cities = new ArrayList<>();
        cities.add(new City(1L, "TestCity1"));
        cities.add(new City(2L, "TestCity2"));
        when(cityRepository.findAll()).thenReturn(cities);

        List<City> foundCities = cityService.findAll();

        assertNotNull(foundCities);
        assertEquals(2, foundCities.size());
    }

    @Test
    void testFindAllCitiesWhenRepositoryIsEmpty() {
        when(cityRepository.findAll()).thenReturn(new ArrayList<>());
        List<City> foundCities = cityService.findAll();

        assertNotNull(foundCities);
        assertTrue(foundCities.isEmpty());
    }

    @Test
    void testUpdateCity() throws NotFoundException {
        Long cityId = 1L;
        City city = new City(cityId, "TestCity");
        when(cityRepository.existsById(cityId)).thenReturn(true);
        cityService.update(city);

        verify(cityRepository, times(1)).existsById(cityId);
        verify(cityRepository, times(1)).save(city);
    }

    @Test
    void testDeleteCity() throws NotFoundException {
        when(cityRepository.existsById(1L)).thenReturn(true);
        //when(cityRepository.deleteById(1L)).thenReturn(true);
        cityService.delete(1L);
        verify(cityRepository, times(1)).deleteById(1L);
    }

}
