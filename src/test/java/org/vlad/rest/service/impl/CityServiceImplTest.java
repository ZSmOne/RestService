package org.vlad.rest.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.rest.exception.NotFoundException;
import org.rest.model.City;
import org.rest.repository.CityRepository;
import org.rest.service.impl.CityServiceImpl;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CityServiceImplTest {
    @Mock
    private CityRepository cityRepository;

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
        when(cityRepository.findById(1L)).thenReturn(city);
        City foundCity = cityService.findById(1L);

        assertNotNull(foundCity);
        assertEquals(city.getId(), foundCity.getId());
        assertEquals(city.getName(), foundCity.getName());
    }

    @Test
    void testFindNonExistingCityById() {
        when(cityRepository.findById(1L)).thenReturn(null);

        assertThrows(NotFoundException.class, () -> cityService.findById(1L));
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
        City city = new City(1L, "TestCity");
        when(cityRepository.existById(1L)).thenReturn(true);
        cityService.update(city);

        verify(cityRepository, times(1)).update(city);
    }

    @Test
    void testUpdateNonExistingCity() {
        City city = new City(1L, "TestCity");
        when(cityRepository.existById(1L)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> cityService.update(city));
        verify(cityRepository, never()).update(city);
    }

    @Test
    void testDeleteCity() throws NotFoundException {
        when(cityRepository.existById(1L)).thenReturn(true);
        when(cityRepository.deleteById(1L)).thenReturn(true);

        boolean result = cityService.delete(1L);

        assertTrue(result);
        verify(cityRepository, times(1)).deleteById(1L);
    }


    @Test
    void testDeleteNonExistentCity() {
        when(cityRepository.existById(1L)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> cityService.delete(1L));
        verify(cityRepository, never()).deleteById(1L);
    }
}
