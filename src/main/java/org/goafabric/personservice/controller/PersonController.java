package org.goafabric.personservice.controller;

import org.goafabric.personservice.controller.dto.Person;
import org.goafabric.personservice.logic.PersonLogic;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/persons")
@Produces(MediaType.APPLICATION_JSON)
@RolesAllowed("standard_role")
public class PersonController {
    private final PersonLogic personLogic;

    public PersonController(PersonLogic personLogic) {
        this.personLogic = personLogic;
    }

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

    @GET
    @Path("sayMyName")
    public Person sayMyName(@QueryParam("name") String name) {
        return personLogic.sayMyName(name);
    }
}
