package org.goafabric.personservice.service;

import org.goafabric.personservice.logic.PersonLogic;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/persons")
@Produces(MediaType.APPLICATION_JSON)
public class PersonService {
    @Inject
    PersonLogic personLogic;

    @GET
    @Path("getById/{id}")
    public Person getById(@PathParam("id") String id) {
        return personLogic.getById(id);
    }

    @GET
    @Path("findAll")
    public List<Person> findAll() {
        return personLogic.findAll();
    }

    @GET
    @Path("findByFirstName")
    public List<Person> findByFirstName(@QueryParam("firstName") String firstName) {
        return personLogic.findByFirstName(firstName);
    }

    @GET
    @Path("findByLastName")
    public List<Person> findByLastName(@QueryParam("lastName") String lastName) {
        return personLogic.findByLastName(lastName);
    }


    @POST
    @Path("save")
    @Consumes(MediaType.APPLICATION_JSON)
    public Person save(@Valid Person person) {
        return personLogic.save(person);
    }

}
