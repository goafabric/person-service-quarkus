package org.goafabric.personservice.persistence

import io.quarkus.hibernate.panache.PanacheRepository
import jakarta.data.page.Page
import jakarta.data.page.PageRequest
import jakarta.data.repository.Find
import jakarta.data.repository.Query
import org.goafabric.personservice.persistence.entity.PersonEo

interface PersonRepository : PanacheRepository.Managed<PersonEo, String> { //CrudRepository<PersonEo, String> {
    @Find
    fun findByLastName(
        lastName: String,
        pageable: PageRequest
    ): Page<PersonEo>

    @Query(
        ("SELECT p FROM PersonEo p " +
                "WHERE (:firstName IS NULL OR p.firstName = :firstName) " +
                "AND (:lastName IS NULL OR p.lastName = :lastName)")
    )
    fun search(
        firstName: String?,
        lastName: String?,
        pageable: PageRequest
    ): Page<PersonEo>

    fun save(personEo: PersonEo): PersonEo {
        return session.merge(personEo)
    }
}