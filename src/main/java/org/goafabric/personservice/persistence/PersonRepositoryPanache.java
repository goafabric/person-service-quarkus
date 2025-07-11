package org.goafabric.personservice.persistence;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import org.goafabric.personservice.controller.dto.PersonSearch;
import org.goafabric.personservice.persistence.entity.PersonEo;

import java.util.HashMap;
import java.util.List;

@ApplicationScoped
public class PersonRepositoryPanache implements PanacheRepositoryBase<PersonEo, String> {

    //todo missing filters with organizationid

    public List<PersonEo> findByStreet(String street, Page page) {
        return find("address.street", street).list(); //page(page).list();
    }


    public List<PersonEo> find(PersonSearch search, Page page) {
        var query = new StringBuilder();
        var params = new HashMap<String, Object>();

        if (search.getFirstName() != null) {
            query.append("firstName = :firstName");
            params.put("firstName", search.getFirstName());
        }

        if (search.getLastName() != null) {
            if (!query.isEmpty()) query.append(" and ");
            query.append("lastName = :lastName");
            params.put("lastName", search.getLastName());
        }

        return find(query.toString(), params).list(); //.page(page).list();
    }


    public PersonEo save(PersonEo person) {
        persist(person);
        return person;
    }

}

