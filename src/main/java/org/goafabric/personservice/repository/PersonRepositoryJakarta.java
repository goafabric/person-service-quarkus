package org.goafabric.personservice.repository;

import jakarta.data.repository.CrudRepository;
import jakarta.data.repository.Repository;
import org.goafabric.personservice.repository.entity.PersonEo;

import java.util.List;

@Repository
public interface PersonRepositoryJakarta extends CrudRepository<PersonEo, String> {
    List<PersonEo> findByFirstName(String firstName);

    List<PersonEo> findByLastName(String lastName);

}
