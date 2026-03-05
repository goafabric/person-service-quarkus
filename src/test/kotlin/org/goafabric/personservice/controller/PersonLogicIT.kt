package org.goafabric.personservice.controller

import io.quarkus.test.junit.QuarkusTest
import jakarta.inject.Inject
import org.assertj.core.api.Assertions
import org.goafabric.personservice.controller.dto.Address
import org.goafabric.personservice.controller.dto.Person
import org.goafabric.personservice.controller.dto.PersonSearch
import org.goafabric.personservice.logic.PersonLogic
import org.junit.jupiter.api.Test

@QuarkusTest
class PersonLogicIT {

    @Inject
    lateinit var personLogic: PersonLogic

    @Test
    fun findById() {
        val persons: List<Person> = personLogic.search(PersonSearch(null, null), 1, 3)
        Assertions.assertThat<Person>(persons).isNotNull().hasSize(3)

        val person = personLogic.getById(persons.first().id!!)
        Assertions.assertThat<Person>(person).isNotNull()
        Assertions.assertThat(person.firstName).isEqualTo(persons.first().firstName)
        Assertions.assertThat(person.lastName).isEqualTo(persons.first().lastName)
    }

    @Test
    fun findAll() {
        Assertions.assertThat<Person>(personLogic.search(PersonSearch(null, null), 1, 3)).isNotNull().hasSize(3)
    }

    @Test
    fun findByFirstName() {
        val persons: List<Person> = personLogic.search(PersonSearch("Monty", null), 1, 3)
        Assertions.assertThat<Person>(persons).isNotNull().hasSize(1)
        Assertions.assertThat(persons.first().firstName).isEqualTo("Monty")
        Assertions.assertThat(persons.first().lastName).isEqualTo("Burns")
    }

    @Test
    fun findByLastName() {
        val persons: List<Person> = personLogic.search(PersonSearch(null, "Simpson"), 1, 3)
        Assertions.assertThat<Person>(persons).isNotNull().hasSize(2)
        Assertions.assertThat(persons.first().lastName).isEqualTo("Simpson")
    }

    @Test
    fun save() {
        val person = personLogic.save(
            Person(
                null, null,
                "Homer",
                "Simpson",
                mutableListOf<Address>(createAddress("Evergreen Terrace"))
            )
        )

        Assertions.assertThat<Person>(person).isNotNull()
        personLogic.delete(person.id!!)
    }

    private fun createAddress(street: String): Address {
        return Address(
            null, null,
            street, "Springfield"
        )
    }


    /*
    @Test
    @Transactional
    fun findByStreet() {
        Assertions.assertThat<Person>(personLogic.findByStreet("Monty Mansion", 1, 3)).isNotNull().hasSize(1)
    }

     */
}
