package org.goafabric.personservice.persistence.multitenancy;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.runtime.Startup;
import org.goafabric.personservice.persistence.PersonBo;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
@Startup
public class RepositoryDelegate implements PanacheRepositoryBase<PersonBo, String> {
}
