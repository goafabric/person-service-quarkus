package org.goafabric.personservice.persistence;

import io.quarkus.panache.common.Parameters;
import org.goafabric.personservice.persistence.multitenancy.MultiTenantRepository;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class PersonRepository extends MultiTenantRepository<PersonBo> {

    public PersonRepository() {
        super(PersonRepositoryDelegate.class);
    }
    
    public List<PersonBo> findByFirstName(String firstName) {
        return find("firstName", firstName).list();
    }

    public List<PersonBo> findByLastName(String lastName) {
        return find("lastName = :lastName",
                Parameters.with("lastName", lastName)
                        .map()).list();
    }

    public long countByLastName(String lastName) {
        return count("lastName = :lastName",
                Parameters.with("lastName", lastName)
                        .map());
    }

}

