package org.rest.servlet.user.dto;

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

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }
}
