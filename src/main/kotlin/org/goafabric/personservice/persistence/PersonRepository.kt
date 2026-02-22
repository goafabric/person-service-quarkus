package org.goafabric.personservice.persistence

import io.quarkus.hibernate.panache.PanacheRepository
import jakarta.data.page.Page
import jakarta.data.page.PageRequest
import jakarta.data.repository.Find
import jakarta.data.repository.OrderBy
import jakarta.data.repository.Query
import jakarta.data.repository.Repository
import org.goafabric.personservice.persistence.entity.PersonEo

@Repository
interface PersonRepository : PanacheRepository.Managed<PersonEo, String> { //CrudRepository<PersonEo, String> {
    @Find
    @OrderBy("lastName")
    fun findByLastNameAndOrganizationId(
        lastName: String,
        organizationId: String,
        pageable: PageRequest
    ): Page<PersonEo>

    @Query(
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