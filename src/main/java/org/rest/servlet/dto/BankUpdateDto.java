package org.rest.servlet.dto;

public class BankUpdateDto {
    private Long id;
    private String name;

    public BankUpdateDto() {
    }

    public BankUpdateDto(Long id, String name) {
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