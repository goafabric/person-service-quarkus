package org.goafabric.personservice.persistence;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.runtime.Startup;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
@Startup
public class PersonRepositoryDelegate implements PanacheRepositoryBase<PersonBo, String> {
}
