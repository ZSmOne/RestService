package org.rest.servlet.city;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.rest.exception.NotFoundException;
import org.rest.model.City;
import org.rest.service.CityService;
import org.rest.servlet.city.dto.CityIncomingDto;
import org.rest.servlet.city.dto.CityOutGoingDto;
import org.rest.servlet.city.dto.CityUpdateDto;
import org.rest.servlet.mapper.CityMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/city")
public class CityController {

    private final CityService cityService;
    private final ObjectMapper objectMapper;

    @Autowired
    public CityController(CityService cityService) {
        this.cityService = cityService;
        this.objectMapper = new ObjectMapper();
    }

    @GetMapping("/all")
    public ResponseEntity<String> getAllCities() {
        List<City> cities = cityService.findAll();
        List<CityOutGoingDto> cityOutGoingDtos = cities.stream()
                .map(CityMapper.INSTANCE::cityToCityOutGoingDto)
                .toList();
        try {
            String citiesJson = objectMapper.writeValueAsString(cityOutGoingDtos);
            return ResponseEntity.ok(citiesJson);
        } catch (JsonProcessingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing JSON");
        }
    }
    @GetMapping("{cityId}")
    public ResponseEntity<String> getCity(@PathVariable("cityId") Long cityId) {
        try {
            City city = cityService.findById(cityId);
            CityOutGoingDto cityOutGoingDto = CityMapper.INSTANCE.cityToCityOutGoingDto(city);
            String cityJson = objectMapper.writeValueAsString(cityOutGoingDto);
            return ResponseEntity.ok(cityJson);
        } catch (NotFoundException | JsonProcessingException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<String> createCity(@RequestBody String json){
        try {
            CityIncomingDto cityIncomingDto = objectMapper.readValue(json, CityIncomingDto.class);
            City city = CityMapper.INSTANCE.cityIncomingDtoToCity(cityIncomingDto);
            city = cityService.save(city);
            CityOutGoingDto savedCityDto = CityMapper.INSTANCE.cityToCityOutGoingDto(city);
            String cityJson = objectMapper.writeValueAsString(savedCityDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(cityJson);
        } catch (IllegalArgumentException | JsonProcessingException e) {
            return ResponseEntity.badRequest().body("Incorrect city object");
        }
    }

    @PutMapping
    public ResponseEntity<String> updateCity(@RequestBody String json) {
        try {
         CityUpdateDto cityUpdateDto = objectMapper.readValue(json, CityUpdateDto.class);
           City city = CityMapper.INSTANCE.cityUpdateDtoToCity(cityUpdateDto);
           cityService.update(city);
           return ResponseEntity.ok().build();
       } catch (NotFoundException e) {
           return ResponseEntity.notFound().build();
     } catch (IllegalArgumentException | JsonProcessingException e) {
         return ResponseEntity.badRequest().build();
        }
    }

@DeleteMapping("/{cityId}")
public ResponseEntity<Void> deleteCity(@PathVariable("cityId") Long cityId) {
    try {
        cityService.delete(cityId);
        return ResponseEntity.noContent().build();
    } catch (NotFoundException e) {
        return ResponseEntity.notFound().build();
    } catch (IllegalArgumentException e) {
        return ResponseEntity.badRequest().build();
    }
}
}


