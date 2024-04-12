package org.rest.servlet.mapper;

import org.rest.model.City;
import org.rest.servlet.dto.CityIncomingDto;
import org.rest.servlet.dto.CityOutGoingDto;
import org.rest.servlet.dto.CityUpdateDto;

import java.util.List;

public interface CityDtoMapper {
    City map(CityIncomingDto cityIncomingDto);

    City map(CityUpdateDto roleUpdateDto);

    CityOutGoingDto map(City role);

    List<CityOutGoingDto> map(List<City> CityList);

}
