package org.goafabric.personservice.persistence;

import jakarta.data.page.Page;
import jakarta.data.page.PageRequest;
import jakarta.data.repository.CrudRepository;
import jakarta.data.repository.Find;
import jakarta.data.repository.Query;
import jakarta.data.repository.Repository;
import org.goafabric.personservice.persistence.entity.PersonEo;


@Repository
public interface PersonRepository extends CrudRepository<PersonEo, String>  {
    @Find
    Page<PersonEo> findByFirstNameAndOrganizationId(String firstName, String organizationId, PageRequest pageRequest);

    @Query("SELECT p FROM PersonEo p WHERE p.organizationId = :organizationId " +
            "AND (:firstName IS NULL OR p.firstName = :firstName) " +
            "AND (:lastName IS NULL OR p.lastName = :lastName)")
    Page<PersonEo> search(
            String firstName,
            String lastName,
            String organizationId,
            PageRequest pageable
    );
}
