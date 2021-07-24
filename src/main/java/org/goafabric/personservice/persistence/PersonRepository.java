package org.goafabric.personservice.persistence;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Parameters;
import io.quarkus.runtime.Startup;
import org.goafabric.personservice.persistence.multitenancy.MultiTenantRepository;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class PersonRepository extends MultiTenantRepository<PersonBo> {

    @ApplicationScoped @Startup
    private static class RepositoryDelegate implements PanacheRepositoryBase<PersonBo, String> {}

    public PersonRepository() {
        super(RepositoryDelegate.class);
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

