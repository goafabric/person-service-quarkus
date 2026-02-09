package org.goafabric.personservice.persistence

import io.quarkus.hibernate.orm.panache.PanacheQuery
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase
import io.quarkus.panache.common.Page
import jakarta.enterprise.context.ApplicationScoped
import org.goafabric.personservice.controller.dto.PersonSearch
import org.goafabric.personservice.extensions.UserContext.organizationId
import org.goafabric.personservice.persistence.entity.PersonEo

@ApplicationScoped
class PersonRepository : PanacheRepositoryBase<PersonEo, String> {
    fun find(search: PersonSearch, page: Page): MutableList<PersonEo> {
        val query = StringBuilder()
        val params = HashMap<String, Any>()

        if (search.firstName != null) {
            query.append("firstName = :firstName")
            params.put("firstName", search.firstName!!)
        }

        if (search.lastName != null) {
            if (!query.isEmpty()) {
                query.append(" and ")
            }
            query.append("lastName = :lastName")
            params.put("lastName", search.lastName!!)
        }

        return findWithOrganization(query.toString(), params)!!.page<PersonEo>(page).list<PersonEo>()
    }

    //we assume here that findById and deleteByWork because the UUID should be unique across all organizations, doesnt work for counts though
    private fun findWithOrganization(query: String, params: MutableMap<String, Any>): PanacheQuery<PersonEo> {
        val findQuery = StringBuilder(query)
        if (!findQuery.isEmpty()) {
            findQuery.append(" and ")
        }
        findQuery.append("organizationId = :organizationId")
        params.put("organizationId", organizationId)
        return find(findQuery.toString(), params)
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

