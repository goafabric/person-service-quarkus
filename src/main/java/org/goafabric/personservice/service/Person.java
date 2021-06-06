package org.goafabric.personservice.service;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;

@Value
@Builder
@AllArgsConstructor
public class Person {
    @Null
    private String id;

    @NotNull
    @Size(min = 3, max = 255)
    private String firstName;

    @NotNull
    @Size(min = 3, max = 255)
    private String lastName;
}
