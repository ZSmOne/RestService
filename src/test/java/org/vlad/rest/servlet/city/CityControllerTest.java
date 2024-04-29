package org.vlad.rest.servlet.city;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.rest.exception.NotFoundException;
import org.rest.model.City;
import org.rest.service.CityService;
import org.rest.servlet.city.CityController;
import org.rest.servlet.mapper.CityMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CityControllerTest {

    @Mock
    private CityService cityService;

    @InjectMocks
    private CityController cityController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        objectMapper = new ObjectMapper();
    }
    @Test
    void testGetAllCities() throws JsonProcessingException {
    List<City> cities = new ArrayList<>();
        cities.add(new City(1L, "City 1"));
        cities.add(new City(2L, "City 2"));
    when(cityService.findAll()).thenReturn(cities);
    ResponseEntity<String> response = cityController.getAllCities();
    assertEquals(HttpStatus.OK, response.getStatusCode());
    String expectedJson = objectMapper.writeValueAsString(
            cities.stream().map(CityMapper.INSTANCE::cityToCityOutGoingDto).toList()
    );
    assertEquals(expectedJson, response.getBody());
}

    @Test
    void testGetCity() throws JsonProcessingException, NotFoundException {
        Long cityId = 1L;
        City city = new City(cityId, "City 1");
        when(cityService.findById(cityId)).thenReturn(city);
        ResponseEntity<String> response = cityController.getCity(cityId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        String expectedJson = objectMapper.writeValueAsString(
                CityMapper.INSTANCE.cityToCityOutGoingDto(city)
        );
        assertEquals(expectedJson, response.getBody());
    }
    @Test
    void testCreateCity() throws JsonProcessingException {
        String json = "{\"name\":\"Test City\"}";
        City city = new City(1L, "Test City");
        when(cityService.save(any())).thenReturn(city);
        ResponseEntity<String> response = cityController.createCity(json);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        String expectedJson = objectMapper.writeValueAsString(
                CityMapper.INSTANCE.cityToCityOutGoingDto(city)
        );
        assertEquals(expectedJson, response.getBody());
    }

    @Test
    void testUpdateCity() throws  NotFoundException {
        //Long cityId = 1L;
        String json = "{\"id\":1,\"name\":\"Updated City\"}";
       // String cityName = "Updated City";
        //CityUpdateDto updateDto = new CityUpdateDto(cityId, cityName);
        //City city = new City(cityId, cityName);
        //String json = objectMapper.writeValueAsString(updateDto);
        doNothing().when(cityService).update(any());
        //json = objectMapper.writeValueAsString(updateDto);
        ResponseEntity<String> response = cityController.updateCity(json);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testUpdateCity_NotFound() throws IOException, NotFoundException {
        String json = "{\"id\":1,\"name\":\"Updated City\"}";
        doThrow(new NotFoundException("City not found")).when(cityService).update(any());
        ResponseEntity<String> response = cityController.updateCity(json);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testDeleteCity() {
        Long cityId = 1L;
        cityService.delete(cityId);
        ResponseEntity<Void> response = cityController.deleteCity(cityId);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void testDeleteCity_NotFound() throws NotFoundException {
        Long cityId = 1L;
        doThrow(new NotFoundException("City not found")).when(cityService).delete(cityId);
        ResponseEntity<Void> response = cityController.deleteCity(cityId);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}

