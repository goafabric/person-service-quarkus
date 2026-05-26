package org.goafabric.personservice.logic

import jakarta.data.page.PageRequest
import jakarta.enterprise.context.ApplicationScoped
import jakarta.transaction.Transactional
import org.eclipse.microprofile.rest.client.inject.RestClient
import org.goafabric.personservice.adapter.CalleeServiceAdapter
import org.goafabric.personservice.controller.dto.Person
import org.goafabric.personservice.controller.dto.PersonSearch
import org.goafabric.personservice.extensions.UserContext
import org.goafabric.personservice.logic.mapper.PersonMapper
import org.goafabric.personservice.persistence.PersonRepository

@Transactional
@ApplicationScoped
class PersonLogic(
    private val personMapper: PersonMapper,
    private val personRepository: PersonRepository,
    @param:RestClient private val calleeServiceAdapter: CalleeServiceAdapter) {

    fun getById(id: String): Person {
        return personMapper.map(personRepository.findById(id))
    }

    fun search(personSearch: PersonSearch, page: Int, size: Int): List<Person> {
        val persons = personRepository.search(personSearch.firstName, personSearch.lastName,
            PageRequest.ofPage(page.toLong() + 1, size, true))
        return personMapper.map(persons)
    }

    fun save(person: Person): Person {
        return personMapper.map(
            personRepository.save(
                personMapper.map(person)))
    }

    fun delete(id: String) {
        personRepository.deleteById(id)
    }

    fun sayMyName (name : String) : Person {
        return Person(firstName = calleeServiceAdapter.sayMyName(name).message, lastName = "", address = emptyList())
    }

}