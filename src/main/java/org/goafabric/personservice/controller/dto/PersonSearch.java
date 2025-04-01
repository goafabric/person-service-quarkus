package org.goafabric.personservice.controller.dto;

import jakarta.ws.rs.QueryParam;

public class PersonSearch {
    @QueryParam("firstName")
    private String firstName;

    @QueryParam("lastName")
    private String lastName;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}