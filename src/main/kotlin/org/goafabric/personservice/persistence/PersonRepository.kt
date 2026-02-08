package org.goafabric.personservice.persistence

import jakarta.data.page.Page
import jakarta.data.page.PageRequest
import jakarta.data.repository.CrudRepository
import jakarta.data.repository.Find
import jakarta.data.repository.Query
import jakarta.data.repository.Repository
import org.goafabric.personservice.persistence.entity.PersonEo

@Repository
interface PersonRepository : CrudRepository<PersonEo?, String?> {
    @Find
    fun findByLastNameAndOrganizationId(
        lastName: String?,
        organizationId: String?,
        pageable: PageRequest?
    ): Page<PersonEo?>?

    @Query("SELECT DISTINCT p FROM PersonEo p JOIN p.address a WHERE a.street = :street and p.organizationId = :organizationId")
    fun findByAddressStreetAndOrganizationId(
        street: String?,
        organizationId: String?,
        pageable: PageRequest?
    ): Page<PersonEo?>?

    @Query(
        ("SELECT p FROM PersonEo p WHERE p.organizationId = :organizationId " +
                "AND (:firstName IS NULL OR p.firstName = :firstName) " +
                "AND (:lastName IS NULL OR p.lastName = :lastName)")
    )
    fun search(
        firstName: String?,
        lastName: String?,
        organizationId: String?,
        pageable: PageRequest?
    ): Page<PersonEo?>?
}
