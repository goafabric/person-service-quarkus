package org.goafabric.personservice.controller.dto;

import jakarta.ws.rs.QueryParam;

public record PersonSearch(
        @QueryParam("firstName") String firstName,
        @QueryParam("lastName") String lastName) { }
