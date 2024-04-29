package org.rest.servlet.user.dto;

import org.rest.servlet.city.dto.CityUpdateDto;

public class UserUpdateDto {
    private Long id;
    private String name;
    private CityUpdateDto city;

    public UserUpdateDto() {
    }

    public UserUpdateDto(Long id, String name, CityUpdateDto city) {
        this.id = id;
        this.name = name;
        this.city = city;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }


    public CityUpdateDto getCity() {
        return city;
    }


}

