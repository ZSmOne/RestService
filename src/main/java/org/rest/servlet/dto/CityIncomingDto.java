package org.rest.servlet.dto;

public class CityIncomingDto {
    private String name;

    public CityIncomingDto() {
    }

    public CityIncomingDto(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

