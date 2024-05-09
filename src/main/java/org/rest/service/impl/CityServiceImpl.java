package org.rest.service.impl;

import org.rest.model.City;
import org.rest.repository.CityCrudRepository;
import org.rest.service.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.StreamSupport;

@Service
public class CityServiceImpl implements CityService {

    private final CityCrudRepository cityCrudRepository;

    @Autowired
    public CityServiceImpl(CityCrudRepository cityCrudRepository){
        this.cityCrudRepository = cityCrudRepository;
    }

    @Override
    public City save(City city) {
        return cityCrudRepository.save(city);
    }

    @Override
    public City findById(Long id){
        return cityCrudRepository.findById(id).orElseThrow(() -> new IllegalStateException("City not found"));
    }

    @Override
    public List<City> findAll(){
        Iterable<City> iterable = cityCrudRepository.findAll();
        return StreamSupport.stream(iterable.spliterator(), false)
                .toList();
    }

    @Override
    public void update(City city) {
        if (cityCrudRepository.existsById(city.getId()))
            cityCrudRepository.save(city);
    }

    @Override
    public void delete(Long id) {
        if (cityCrudRepository.existsById(id)){
        cityCrudRepository.deleteCityFromUsers(id);
        cityCrudRepository.deleteById(id);
        }
    }
}
