package org.rest.servlet.user.dto;

import org.rest.model.City;

public class UserIncomingDto {
    private String name;

    private City city;

    public UserIncomingDto() {
    }

    public UserIncomingDto(String name, City city) {
        this.name = name;
        this.city = city;
    }

    public String getName() {
        return name;
    }

    public City getCity() {
        return city;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCity(City city) {
        this.city = city;
    }
}

