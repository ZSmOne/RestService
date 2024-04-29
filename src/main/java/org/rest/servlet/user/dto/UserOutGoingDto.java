package org.rest.servlet.user.dto;

import org.rest.servlet.bank.dto.BankSimpleOutGoingDto;
import org.rest.servlet.city.dto.CityOutGoingDto;

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

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCity(CityOutGoingDto city) {
        this.city = city;
    }

    public List<BankSimpleOutGoingDto> getBankList() {
        return bankList;
    }

    public void setBankList(List<BankSimpleOutGoingDto> bankList) {
        this.bankList = bankList;
    }

}
