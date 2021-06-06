package org.goafabric.personservice.persistence;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class PersonRepository implements PanacheRepositoryBase<PersonBo, String> {
    public List<PersonBo> findByFirstName(String firstName) {
        return find("firstName", firstName).list();
    }

    public List<PersonBo> findByLastName(String lastName) {
        return find("lastName", lastName).list();
    }

    public PersonBo save(PersonBo personBo) {
        persist(personBo);
        return personBo;
    }
}

