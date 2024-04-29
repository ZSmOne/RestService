package org.rest.service.impl;

import org.rest.exception.NotFoundException;
import org.rest.model.City;
import org.rest.repository.CityRepository;
import org.rest.service.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CityServiceImpl implements CityService {
    private final CityRepository cityRepository;
    @Autowired
    private CityServiceImpl(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    @Override
    public City save(City city) {
        city = cityRepository.save(city);
        return city;
    }

    @Override
    public City findById(Long cityId) throws NotFoundException {
        City city = cityRepository.findById(cityId);
        if (city == null)
            throw new NotFoundException("City not found.");
        return city;
    }

    @Override
    public List<City> findAll() {
        return cityRepository.findAll();
    }

    @Override
    public void update(City city) throws NotFoundException {
        isCityExists(city.getId());
        cityRepository.update(city);
    }

    @Override
    public boolean delete(Long cityId) throws NotFoundException {
        isCityExists(cityId);
        return cityRepository.deleteById(cityId);
    }
    private void isCityExists(Long cityId) throws NotFoundException {
        if (!cityRepository.existById(cityId)) {
            throw new NotFoundException("City not found.");
        }
    }
}
