package org.goafabric.personservice.persistence;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import org.goafabric.personservice.controller.dto.PersonSearch;
import org.goafabric.personservice.persistence.entity.PersonEo;

import java.util.HashMap;
import java.util.List;

@ApplicationScoped
public class PersonRepositoryPanache implements PanacheRepositoryBase<PersonEo, String> {


    public List<PersonEo> findByStreet(String street) {
        return find("street", street).list();
    }


    public List<PersonEo> find(PersonSearch search) {
        var query = new StringBuilder("1=1");
        var params = new HashMap<>();

        if (search.getFirstName() != null) {
            query.append(" and firstName = :firstName");
            params.put("firstName", search.getFirstName());
        }

        if (search.getLastName() != null) {
            query.append(" and lastName = :lastName");
            params.put("lastName", search.getLastName());
        }

        return find(query.toString(), params).list();
    }


    public PersonEo save(PersonEo person) {
        persist(person);
        return person;
    }

}

