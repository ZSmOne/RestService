package org.rest.servlet.dto;

public class UserSimpleOutGoingDto {
    private Long id;
    private String name;


    public UserSimpleOutGoingDto() {
    }

    public UserSimpleOutGoingDto(Long id, String name) {
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
