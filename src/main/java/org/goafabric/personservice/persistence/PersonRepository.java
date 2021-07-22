package org.goafabric.personservice.persistence;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Parameters;
import org.goafabric.personservice.persistence.multitenancy.MultiTenantRepository;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class PersonRepository extends MultiTenantRepository<PersonBo, String> {

    public PanacheQuery<PersonBo> findAll() {
        return findAllx();
    }

    public List<PersonBo> findByFirstName(String firstName) {
        return findx("firstName", firstName).list();
    }

    public List<PersonBo> findByLastName(String lastName) {
        return findx("lastName = :lastName",
                Parameters.with("lastName", lastName)
                        .map()).list();
    }

    public long countByLastName(String lastName) {
        return countx("lastName = :lastName",
                Parameters.with("lastName", lastName)
                        .map());
    }

    public PersonBo save(PersonBo personBo) {
        return savex(personBo);
    }
}

