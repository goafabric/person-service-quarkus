package org.goafabric.personservice.controller;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.goafabric.personservice.controller.dto.Address;
import org.goafabric.personservice.controller.dto.Person;
import org.goafabric.personservice.controller.dto.PersonSearch;
import org.goafabric.personservice.extensions.TenantContext;
import org.goafabric.personservice.logic.PersonLogic;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@QuarkusTest
public class PersonLogicIT {
    @Inject
    PersonLogic personLogic;

    @BeforeAll
    public static void init() {
        TenantContext.setOrganizationId("0");
    }


    @Test
    public void findById() {
        List<Person> persons = personLogic.search(new PersonSearch(null, null));
        assertThat(persons).isNotNull().hasSize(3);

        final Person person
                = personLogic.getById(persons.getFirst().id());
        assertThat(person).isNotNull();
        assertThat(person.firstName()).isEqualTo(persons.getFirst().firstName());
        assertThat(person.lastName()).isEqualTo(persons.getFirst().lastName());
    }

    @Test
    public void findAll() {
        assertThat(personLogic.search(new PersonSearch(null, null))).isNotNull().hasSize(3);
    }

    @Test
    public void findByFirstName() {
        List<Person> persons =  personLogic.search(new PersonSearch("Monty", null));
        assertThat(persons).isNotNull().hasSize(1);
        assertThat(persons.getFirst().firstName()).isEqualTo("Monty");
        assertThat(persons.getFirst().lastName()).isEqualTo("Burns");
    }

    @Test
    public void findByLastName() {
        List<Person> persons =  personLogic.search(new PersonSearch(null, "Simpson"));
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

}
