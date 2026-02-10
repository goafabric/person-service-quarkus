package org.goafabric.personservice.persistence

import io.quarkus.hibernate.orm.panache.PanacheQuery
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase
import io.quarkus.panache.common.Page
import jakarta.enterprise.context.ApplicationScoped
import org.goafabric.personservice.controller.dto.PersonSearch
import org.goafabric.personservice.extensions.UserContext.organizationId
import org.goafabric.personservice.persistence.entity.PersonEo
import kotlin.collections.mutableListOf

@ApplicationScoped
class PersonRepository : PanacheRepositoryBase<PersonEo, String> {
    fun find(search: PersonSearch, page: Page): MutableList<PersonEo> {
        val conditions = mutableListOf<String>()
        val params = mutableMapOf<String, Any>()

        search.firstName?.let { conditions += "firstName = :firstName"; params["firstName"] = it }
        search.lastName?.let { conditions += "lastName = :lastName"; params["lastName"] = it }

        return findWithOrganization(conditions, params).page<PersonEo>(page).list()
    }

    //we assume here that findById and deleteByWork because the UUID should be unique across all organizations, doesnt work for counts though
    private fun findWithOrganization(conditions: MutableList<String>, params: MutableMap<String, Any>): PanacheQuery<PersonEo> {
        conditions += "organizationId = :organizationId"
        params["organizationId"] = organizationId
        return find(conditions.joinToString(" and "), params)
    }

    fun findByStreet(street: String, page: Page): MutableList<PersonEo> {
        check(!true) { "NYI" }
        return find("address.street", street).page<PersonEo>(page).list<PersonEo>()
    }

    fun save(person: PersonEo): PersonEo {
        persist(person)
        return person
    }
}

