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
        return findx("firstName = :firstName",
                Parameters.with("firstName", firstName)
                        .map()).list();

        //return findx("firstName = ?1 and tenantId = ?2", firstName, "0").list();
    }

    public List<PersonBo> findByLastName(String lastName) {
        return findx("lastName = :lastName",
                Parameters.with("lastName", lastName)
                        .map()).list();
    }

    public PersonBo save(PersonBo personBo) {
        return savex(personBo);
    }
}

