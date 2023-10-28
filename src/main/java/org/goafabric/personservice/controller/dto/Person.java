package org.goafabric.personservice.controller.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Size;

public record Person (
    @Null String id,
    @NotNull @Size(min = 3, max = 255) String firstName,
    @NotNull @Size(min = 3, max = 255) String lastName,
    Address address
) {}
