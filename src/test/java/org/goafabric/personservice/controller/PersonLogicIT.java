package org.goafabric.personservice.controller;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.goafabric.personservice.controller.dto.Address;
import org.goafabric.personservice.controller.dto.Person;
import org.goafabric.personservice.controller.dto.PersonSearch;
import org.goafabric.personservice.extensions.UserContext;
import org.goafabric.personservice.logic.PersonLogic;
import org.goafabric.personservice.persistence.PersonRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@QuarkusTest
public class PersonLogicIT {
    @Inject
    PersonLogic personLogic;

    @Inject
    PersonRepository personRepository;

    @BeforeAll
    public static void init() {
        UserContext.setOrganizationId("0");
    }


    @Test
    public void findById() {
        List<Person> persons = personLogic.search(new PersonSearch(null, null), 1, 3);
        assertThat(persons).isNotNull().hasSize(3);

        final Person person
                = personLogic.getById(persons.getFirst().id());
        assertThat(person).isNotNull();
        assertThat(person.firstName()).isEqualTo(persons.getFirst().firstName());
        assertThat(person.lastName()).isEqualTo(persons.getFirst().lastName());
    }

    @Test
    public void findAll() {
        assertThat(personLogic.search(new PersonSearch(null, null), 1, 3)).isNotNull().hasSize(3);
    }

    @Test
    public void findByFirstName() {
        List<Person> persons =  personLogic.search(new PersonSearch("Monty", null), 1, 3);
        assertThat(persons).isNotNull().hasSize(1);
        assertThat(persons.getFirst().firstName()).isEqualTo("Monty");
        assertThat(persons.getFirst().lastName()).isEqualTo("Burns");
    }

    @Test
    public void findByLastName() {
        List<Person> persons =  personLogic.search(new PersonSearch(null, "Simpson"), 1, 3);
        assertThat(persons).isNotNull().hasSize(2);
        assertThat(persons.getFirst().lastName()).isEqualTo("Simpson");
    }

    @Test
    void save() {
        final Person person = personLogic.save(
                new Person(null, null,
                        "Homer",
                        "Simpson",
                        Collections.singletonList(createAddress("Evergreen Terrace"))
                ));

        assertThat(person).isNotNull();
        personLogic.delete(person.id());
    }

    private Address createAddress(String street) {
        return new Address(null, null,
                street, "Springfield");
    }


    @Test
    @Transactional
    void findByStreet() {
        assertThat(personLogic.findByStreet("Monty Mansion", 1,3 )).isNotNull().hasSize(1);
    }
}
