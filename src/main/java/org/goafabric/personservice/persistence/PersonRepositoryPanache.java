package org.goafabric.personservice.persistence;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import org.goafabric.personservice.controller.dto.PersonSearch;
import org.goafabric.personservice.extensions.UserContext;
import org.goafabric.personservice.persistence.entity.PersonEo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class PersonRepositoryPanache implements PanacheRepositoryBase<PersonEo, String> {


    public List<PersonEo> find(PersonSearch search, Page page) {
        var query = new StringBuilder();
        var params = new HashMap<String, Object>();

        if (search.getFirstName() != null) {
            query.append("firstName = :firstName");
            params.put("firstName", search.getFirstName());
        }

        if (search.getLastName() != null) {
            if (!query.isEmpty()) { query.append(" and "); }
            query.append("lastName = :lastName");
            params.put("lastName", search.getLastName());
        }

        return findWithOrganization(query.toString(), params).page(page).list();
    }

    //we assume here that findById and deleteByWork because the UUID should be unique across all organizations, doesnt work for counts though
    private PanacheQuery<PersonEo> findWithOrganization(String query, Map<String, Object> params) {
        var findQuery = new StringBuilder(query);
        if (!findQuery.isEmpty()) { findQuery.append(" and "); }
        findQuery.append("organizationId = :organizationId");
        params.put("organizationId", UserContext.getOrganizationId());
        return find(findQuery.toString(), params);
    }


    public List<PersonEo> findByStreet(String street, Page page) {
        if (true) { throw new IllegalStateException("NYI"); }
        return find("address.street", street).page(page).list();
    }

    public PersonEo save(PersonEo person) {
        persist(person);
        return person;
    }

}

