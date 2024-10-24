package org.goafabric.personservice.persistence;

import jakarta.data.repository.CrudRepository;
import jakarta.data.repository.Find;
import jakarta.data.repository.Repository;
import org.goafabric.personservice.persistence.entity.PersonEo;

import java.util.List;

@Repository
public interface PersonRepository extends CrudRepository<PersonEo, String> {
    @Find
    List<PersonEo> findByFirstName(String firstName);

    @Find
    List<PersonEo> findByLastName(String lastName);
}
