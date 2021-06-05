package org.goafabric.personservice.service;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@AllArgsConstructor
public class Person {
    private String id;
    private String firstName;
    private String lastName;
}
