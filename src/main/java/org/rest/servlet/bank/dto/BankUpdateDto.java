package org.rest.servlet.bank.dto;

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

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }
}