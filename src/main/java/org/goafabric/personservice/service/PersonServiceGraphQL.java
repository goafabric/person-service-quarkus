package org.goafabric.personservice.service;

import org.eclipse.microprofile.graphql.GraphQLApi;
import org.eclipse.microprofile.graphql.Mutation;
import org.eclipse.microprofile.graphql.Name;
import org.eclipse.microprofile.graphql.Query;
import org.goafabric.personservice.logic.PersonLogic;
import org.goafabric.personservice.service.dto.Person;

import javax.inject.Inject;
import javax.ws.rs.QueryParam;
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

    @Query("findByFirstName")
    public List<Person> findByFirstName(@Name("firstName") String firstName) {
        return personLogic.findByFirstName(firstName);
    }

    @Query("findByLastName")
    public List<Person> findByLastName(@Name("lastName") String lastName) {
        return personLogic.findByLastName(lastName);
    }

    @Mutation("save")
    public Person save(Person person) {
        return personLogic.save(person);
    }

    @Query("sayMyName")
    public Person sayMyName(@QueryParam("name") String name) {
        return personLogic.sayMyName(name);
    }
}
