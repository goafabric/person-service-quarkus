package org.goafabric.personservice.logic;

import org.goafabric.personservice.service.Person;

import javax.enterprise.context.ApplicationScoped;
import java.util.Collections;
import java.util.List;

@ApplicationScoped
public class PersonLogic {

    public Person getById(String id) {
        return Person.builder().build();
    }

    public List<Person> findAll() {
        return Collections.singletonList(Person.builder().build());
    }

    public List<Person> findByFirstName(String firstName) {
        return Collections.singletonList(Person.builder().build());
    }

    public List<Person> findByLastName(String lastName) {
        return Collections.singletonList(Person.builder().build());
    }

    public Person save(Person person) {
        return null;
    }

}
