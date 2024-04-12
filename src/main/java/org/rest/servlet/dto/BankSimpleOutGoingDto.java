package org.rest.servlet.dto;

public class BankSimpleOutGoingDto {
    private Long id;
    private String name;

    public BankSimpleOutGoingDto(Long id, String name) {
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
