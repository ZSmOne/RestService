package org.rest.servlet.mapper.impl;

import org.rest.model.City;
import org.rest.servlet.dto.CityIncomingDto;
import org.rest.servlet.dto.CityOutGoingDto;
import org.rest.servlet.dto.CityUpdateDto;
import org.rest.servlet.mapper.CityDtoMapper;

import java.util.List;

public class CityDtoMapperImpl implements CityDtoMapper {
    private static CityDtoMapper instance;

    private CityDtoMapperImpl() {
    }

    public static CityDtoMapper getInstance() {
        if (instance == null) {
            instance = new CityDtoMapperImpl();
        }
        return instance;
    }

    @Override
    public City map(CityIncomingDto cityIncomingDto) {
        return new City(
                null,
                cityIncomingDto.getName()
        );
    }

    @Override
    public City map(CityUpdateDto cityUpdateDto) {
        return new City(
                cityUpdateDto.getId(),
                cityUpdateDto.getName());
    }

    @Override
    public CityOutGoingDto map(City city) {
        return new CityOutGoingDto(
                city.getId(),
                city.getName()
        );
    }

    @Override
    public List<CityOutGoingDto> map(List<City> cityList) {
        return cityList.stream().map(this::map).toList();
    }
}
