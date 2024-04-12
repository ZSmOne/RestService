package org.rest.servlet.dto;

public class CityOutGoingDto {
    private Long id;
    private String name;

    public CityOutGoingDto() {
    }

    public CityOutGoingDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}

