package org.goafabric.personservice.logic

import jakarta.enterprise.context.ApplicationScoped
import jakarta.transaction.Transactional
import org.eclipse.microprofile.rest.client.inject.RestClient
import org.goafabric.personservice.adapter.CalleeServiceAdapter
import org.goafabric.personservice.controller.dto.Person
import org.goafabric.personservice.persistence.PersonRepositoryPanache
import org.goafabric.personservice.persistence.entity.PersonEo

@Transactional
@ApplicationScoped
class PersonLogic2(
    private val personRepository: PersonRepositoryPanache,
    @param:RestClient private val calleeServiceAdapter: CalleeServiceAdapter) {
    fun getById(id: String): Person {
        return Person(firstName = "homer", lastName = "simpson", address = emptyList())
    }

    fun save(person: Person): Person {
        return Person(firstName = "homer", lastName = "simpson", address = emptyList())
    }

    fun delete(id: String) {
    }

    fun sayMyName (name : String) : Person {
        return Person(firstName = calleeServiceAdapter.sayMyName(name).message, lastName = "", address = emptyList())
    }
}