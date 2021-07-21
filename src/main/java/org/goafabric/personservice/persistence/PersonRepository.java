package org.goafabric.personservice.persistence;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class PersonRepository implements PanacheRepositoryBase<PersonBo, String> {
    public List<PersonBo> findByFirstName(String firstName) {
        return find("firstName = ?1 and tenantId = ?2", firstName, "0").list();
    }

    public List<PersonBo> findByLastName(String lastName) {
        return find("lastName = ?1 and tenantId = ?2", lastName, "0").list();
    }

    public PersonBo save(PersonBo personBo) {
        persist(personBo);
        return personBo;
    }
}

