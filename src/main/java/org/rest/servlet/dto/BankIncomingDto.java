package org.rest.servlet.dto;

public class BankIncomingDto {

    private String name;

    public BankIncomingDto() {
    }

    public BankIncomingDto(String name) {
            this.name = name;
        }

    public String getName() {
            return name;
        }

    public void setName(String name) {
        this.name = name;
    }
}
