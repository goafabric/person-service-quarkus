package org.goafabric.personservice.persistence;

import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface PersonRepository extends PagingAndSortingRepository<PersonBo, String> {
    //List<PersonBo> findByFirstName(String firstName);
    List<PersonBo> findByLastName(String lastName);
}

