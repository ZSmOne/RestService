package org.rest.servlet.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;
import org.rest.model.City;
import org.rest.servlet.city.dto.CityIncomingDto;
import org.rest.servlet.city.dto.CityOutGoingDto;
import org.rest.servlet.city.dto.CityUpdateDto;

import java.util.List;

@Mapper
public interface CityMapper {
    CityMapper INSTANCE = Mappers.getMapper(CityMapper.class);

    @Mappings({
            @Mapping(target= "name", source = "dto.name")
    })
    City cityIncomingDtoToCity(CityIncomingDto dto);
    @Mappings({
            @Mapping(target= "id", source = "dto.id"),
            @Mapping(target= "name", source = "dto.name")
    })
    City cityUpdateDtoToCity(CityUpdateDto dto);
    @Mappings({
            @Mapping(target= "id", source = "city.id"),
            @Mapping(target= "name", source = "city.name")
    })
    CityOutGoingDto cityToCityOutGoingDto(City city);
    List<CityOutGoingDto> listCityToCityOutGoingDto(List<City> cities);
}
