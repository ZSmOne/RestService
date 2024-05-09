package org.rest.servlet.city;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.rest.exception.NotFoundException;
import org.rest.model.City;
import org.rest.service.impl.CityServiceImpl;
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

    private final CityServiceImpl cityServiceImpl;
    private final ObjectMapper objectMapper;

    @Autowired
    public CityController(CityServiceImpl cityServiceImpl) {
        this.cityServiceImpl = cityServiceImpl;
        this.objectMapper = new ObjectMapper();
    }

    @GetMapping("{cityId}")
    public ResponseEntity<String> getCity(@PathVariable("cityId") Long cityId) {
        try {
            City city = cityServiceImpl.findById(cityId);
            CityOutGoingDto cityOutGoingDto = CityMapper.INSTANCE.cityToCityOutGoingDto(city);
            String cityJson = objectMapper.writeValueAsString(cityOutGoingDto);
            return ResponseEntity.ok(cityJson);
        } catch (NotFoundException | JsonProcessingException e) {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/all")
    public ResponseEntity<String> getAllCities() {
        try {
            List<City> cities = cityServiceImpl.findAll();
            List<CityOutGoingDto> cityOutGoingDtoList = cities.stream()
                    .map(CityMapper.INSTANCE::cityToCityOutGoingDto)
                    .toList();
            String citiesJson = objectMapper.writeValueAsString(cityOutGoingDtoList);
            return ResponseEntity.ok(citiesJson);
        } catch (JsonProcessingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing JSON");
        }
    }
    @PostMapping
    public ResponseEntity<String> createCity(@RequestBody CityIncomingDto cityIncomingDto){
        try {
            City city = CityMapper.INSTANCE.cityIncomingDtoToCity(cityIncomingDto);
            city = cityServiceImpl.save(city);
            CityOutGoingDto savedCityDto = CityMapper.INSTANCE.cityToCityOutGoingDto(city);
            String cityJson = objectMapper.writeValueAsString(savedCityDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(cityJson);
        } catch (IllegalArgumentException | JsonProcessingException e) {
            return ResponseEntity.badRequest().body("Incorrect city object");
        }
    }

    @PutMapping
    public ResponseEntity<String> updateCity(@RequestBody CityUpdateDto cityUpdateDto) {
        try {
            City city = CityMapper.INSTANCE.cityUpdateDtoToCity(cityUpdateDto);
            cityServiceImpl.update(city);
            return ResponseEntity.ok().build();
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{cityId}")
    public ResponseEntity<Void> deleteCity(@PathVariable("cityId") Long cityId) {
        try {
            cityServiceImpl.delete(cityId);
            return ResponseEntity.noContent().build();
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    @ExceptionHandler(Throwable.class)
    public ResponseEntity<String> handle(Throwable e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

}
