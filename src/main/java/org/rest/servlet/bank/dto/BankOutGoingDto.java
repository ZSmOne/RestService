package org.rest.servlet.bank.dto;

import org.rest.servlet.user.dto.UserSimpleOutGoingDto;

import java.util.List;

public class BankOutGoingDto {
    private Long id;
    private String name;
    private List<UserSimpleOutGoingDto> userList;

    public BankOutGoingDto() {
    }

    public BankOutGoingDto(Long id, String name, List<UserSimpleOutGoingDto> userList) {
        this.id = id;
        this.name = name;
        this.userList = userList;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<UserSimpleOutGoingDto> getUserList() {
        return userList;
    }

    public void setUserList(List<UserSimpleOutGoingDto> userList) {
        this.userList = userList;
    }
}
