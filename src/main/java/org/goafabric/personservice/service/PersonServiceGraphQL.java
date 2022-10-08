package org.goafabric.personservice.service;

import org.eclipse.microprofile.graphql.GraphQLApi;
import org.eclipse.microprofile.graphql.Query;
import org.goafabric.personservice.logic.PersonLogic;
import org.goafabric.personservice.service.dto.Person;

import javax.inject.Inject;
import java.util.List;

//@RolesAllowed("standard_role")
@GraphQLApi
public class PersonServiceGraphQL {
    @Inject
    PersonLogic personLogic;

    @Query("findAll")
    public List<Person> findAll() {
        return personLogic.findAll();
    }
}
