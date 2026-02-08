package org.goafabric.personservice.logic

import io.quarkus.panache.common.Page
import jakarta.data.page.PageRequest
import jakarta.enterprise.context.ApplicationScoped
import jakarta.transaction.Transactional
import org.eclipse.microprofile.rest.client.inject.RestClient
import org.goafabric.personservice.adapter.CalleeServiceAdapter
import org.goafabric.personservice.controller.dto.Person
import org.goafabric.personservice.controller.dto.PersonSearch
import org.goafabric.personservice.extensions.UserContext
import org.goafabric.personservice.persistence.PersonRepositoryPanache
import org.goafabric.personservice.persistence.entity.PersonEo

@Transactional
@ApplicationScoped
class PersonLogic2(
    private val personRepository: PersonRepositoryPanache,
    @param:RestClient private val calleeServiceAdapter: CalleeServiceAdapter) {
    fun getById(id: String): Person {
        return map(personRepository.findById(id))
        //return Person(firstName = "homer", lastName = "simpson", address = emptyList())
    }

    fun search(personSearch: PersonSearch, page: Int, size: Int): List<Person> {
        val persons = personRepository.find(personSearch, Page.of(page, size))

        return persons.map { person -> map(person) }

        /*
        return personMapper.map(
            personRepository.search(
                personSearch.firstName,
                personSearch.lastName,
                organizationId,
                PageRequest.ofPage(page.toLong(), size, true)
            )
        )

         */
    }

    fun save(person: Person): Person {
        return map(personRepository.save(map(person)))
        //return Person(firstName = "homer", lastName = "simpson", address = emptyList())
    }

    fun delete(id: String) {
        personRepository.deleteById(id)
    }

    fun sayMyName (name : String) : Person {
        return Person(firstName = calleeServiceAdapter.sayMyName(name).message, lastName = "", address = emptyList())
    }

    fun map(person: PersonEo): Person {
        return Person(id = person.id, version = person.version,
            firstName = person.firstName!!, lastName = person.lastName!!, address = emptyList())
    }

    fun map(person: Person): PersonEo {
        return PersonEo(id = person.id, version = person.version,
            organizationId = UserContext.organizationId,
            firstName = person.firstName, lastName = person.lastName, address = emptyList())
    }

}