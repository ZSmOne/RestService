package org.rest.service.impl;

import org.rest.exception.NotFoundException;
import org.rest.model.City;
import org.rest.repository.CityRepository;
import org.rest.repository.impl.CityRepositoryImpl;
import org.rest.service.CityService;

import java.util.List;

public class CityServiceImpl implements CityService {
    private CityRepository cityRepository = CityRepositoryImpl.getInstance();
    private static CityService instance;
    private CityServiceImpl() {
    }

    public static synchronized CityService getInstance() {
        if (instance == null) {
            instance = new CityServiceImpl();
        }
        return instance;
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
