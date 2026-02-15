package org.goafabric.personservice.persistence

import io.quarkus.hibernate.panache.PanacheRepository
import jakarta.data.page.Page
import jakarta.data.page.PageRequest
import jakarta.data.repository.OrderBy
import org.goafabric.personservice.persistence.entity.PersonEo
import org.hibernate.annotations.processing.Find
import org.hibernate.annotations.processing.HQL

interface PersonRepositoryPanache : PanacheRepository.Managed<PersonEo, String> {
    @Find
    @OrderBy("lastName")
    fun findByLastNameAndOrganizationId(
        lastName: String,
        organizationId: String,
        pageable: PageRequest
    ): Page<PersonEo>

    @HQL(
        ("SELECT p FROM PersonEo p WHERE p.organizationId = :organizationId " +
                "AND (:firstName IS NULL OR p.firstName = :firstName) " +
                "AND (:lastName IS NULL OR p.lastName = :lastName)")
    )
    fun search(
        firstName: String?,
        lastName: String?,
        organizationId: String,
        pageable: PageRequest
    ): Page<PersonEo>

    fun save(personEo: PersonEo): PersonEo {
        persist(personEo)
        return personEo
    }
}