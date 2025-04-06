package org.goafabric.personservice.persistence;

import jakarta.data.page.Page;
import jakarta.data.page.PageRequest;
import jakarta.data.repository.CrudRepository;
import jakarta.data.repository.Find;
import jakarta.data.repository.Repository;
import org.goafabric.personservice.persistence.entity.PersonEo;

@Repository
public interface PersonRepository extends CrudRepository<PersonEo, String>  {
    @Find
    Page<PersonEo> findAllByOrganizationId(String organizationId, PageRequest pageRequest);

    @Find
    Page<PersonEo> findByFirstNameAndOrganizationId(String firstName, String organizationId, PageRequest pageRequest);

    @Find
    Page<PersonEo> findByLastNameAndOrganizationId(String lastName, String organizationId, PageRequest pageRequest);

}
