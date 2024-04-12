package org.rest.model;

import java.util.List;

public class Bank {
    private Long id;
    private String name;
    private List<User> usersList;

    public Bank() {
    }

    public Bank(Long id, String name, List<User> usersList) {
        this.id = id;
        this.name = name;
        this.usersList = usersList;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<User> getUsersList() {
        return usersList;
    }

    public void setUsersList(List<User> usersList) {
        this.usersList = usersList;
    }
}
