package org.goafabric.personservice.logic

import jakarta.data.page.PageRequest
import jakarta.enterprise.context.ApplicationScoped
import jakarta.transaction.Transactional
import org.eclipse.microprofile.rest.client.inject.RestClient
import org.goafabric.personservice.adapter.CalleeServiceAdapter
import org.goafabric.personservice.controller.dto.Person
import org.goafabric.personservice.controller.dto.PersonSearch
import org.goafabric.personservice.extensions.UserContext.organizationId
import org.goafabric.personservice.persistence.PersonRepository
import org.goafabric.personservice.persistence.entity.PersonEo

@ApplicationScoped
@Transactional
class PersonLogic(
    private val personMapper: PersonMapper,
    private val personRepository: PersonRepository,
    @param:RestClient private val calleeServiceAdapter: CalleeServiceAdapter
) {
    fun getById(id: String): Person {
        return personMapper.map(
            personRepository.findById(id).get()
        )
    }

    fun save(person: Person): Person {
        return personMapper.map(
            personRepository.save<PersonEo>(personMapper.map(person))
        )
    }

    fun delete(id: String) {
        personRepository.deleteById(id)
    }

    fun sayMyName (name : String) : Person {
        return Person(firstName = calleeServiceAdapter.sayMyName(name).message, lastName = "", address = emptyList())
    }

    fun search(personSearch: PersonSearch, page: Int, size: Int): MutableList<Person> {
        return personMapper.map(
            personRepository.search(
                personSearch.firstName,
                personSearch.lastName,
                organizationId,
                PageRequest.ofPage(page.toLong(), size, true)
            )
        )
    }

    fun findByStreet(street: String, page: Int, size: Int): MutableList<Person> {
        return personMapper.map(
            personRepository.findByAddressStreetAndOrganizationId(
                street,
                organizationId,
                PageRequest.ofPage(page.toLong(), size, true)
            )
        )
    }
}
