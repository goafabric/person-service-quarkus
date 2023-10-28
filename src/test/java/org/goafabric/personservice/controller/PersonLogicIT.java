package org.goafabric.personservice.controller;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.goafabric.personservice.controller.dto.Address;
import org.goafabric.personservice.controller.dto.Person;
import org.goafabric.personservice.logic.PersonLogic;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@QuarkusTest
public class PersonLogicIT {
    @Inject
    PersonLogic personLogic;
    

    @Test
    public void findById() {
        List<Person> persons = personLogic.findAll();
        assertThat(persons).isNotNull().hasSize(3);

        final Person person
                = personLogic.getById(persons.get(0).id());
        assertThat(person).isNotNull();
        assertThat(person.firstName()).isEqualTo(persons.get(0).firstName());
        assertThat(person.lastName()).isEqualTo(persons.get(0).lastName());
    }

    @Test
    public void findAll() {
        assertThat(personLogic.findAll()).isNotNull().hasSize(3);
    }

    @Test
    public void findByFirstName() {
        List<Person> persons = personLogic.findByFirstName("Monty");
        assertThat(persons).isNotNull().hasSize(1);
        assertThat(persons.get(0).firstName()).isEqualTo("Monty");
        assertThat(persons.get(0).lastName()).isEqualTo("Burns");
    }

    @Test
    public void findByLastName() {
        List<Person> persons = personLogic.findByLastName("Simpson");
        assertThat(persons).isNotNull().hasSize(2);
        assertThat(persons.get(0).lastName()).isEqualTo("Simpson");
    }

    @Test
    void save() {
        final Person person = personLogic.save(
                new Person(null,
                        "Homer",
                        "Simpson",
                        createAddress("Evergreen Terrace")
                ));

        assertThat(person).isNotNull();
        personLogic.delete(person.id());
    }

    private Address createAddress(String street) {
        return new Address(null,
                street, "Springfield");
    }
}
