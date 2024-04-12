package org.rest.servlet.dto;

import java.util.List;

public class UserOutGoingDto {
    private Long id;
    private String name;

    private CityOutGoingDto city;
    private List<BankSimpleOutGoingDto> bankList;

    public UserOutGoingDto() {
    }

    public UserOutGoingDto(Long id, String name, CityOutGoingDto city, List<BankSimpleOutGoingDto> bankList) {
        this.id = id;
        this.name = name;
        this.city = city;
        this.bankList = bankList;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }


    public CityOutGoingDto getCity() {
        return city;
    }



    public List<BankSimpleOutGoingDto> getBankList() {
        return bankList;
    }

}
