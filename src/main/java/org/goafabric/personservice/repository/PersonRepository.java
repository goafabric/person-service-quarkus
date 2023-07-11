package org.goafabric.personservice.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Parameters;
import jakarta.enterprise.context.ApplicationScoped;
import org.goafabric.personservice.repository.entity.PersonEo;

import java.util.List;

@ApplicationScoped
public class PersonRepository implements PanacheRepositoryBase<PersonEo, String> {


    public List<PersonEo> findByFirstName(String firstName) {
        return find("firstName", firstName).list();
    }

    public List<PersonEo> findByLastName(String lastName) {
        return find("lastName = :lastName",
                Parameters.with("lastName", lastName)
                        .map()).list();
    }

    public long countByLastName(String lastName) {
        return count("lastName = :lastName",
                Parameters.with("lastName", lastName)
                        .map());
    }

    public PersonEo save(PersonEo person) {
        persist(person);
        return person;
    }

}

