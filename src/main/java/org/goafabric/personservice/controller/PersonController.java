package org.goafabric.personservice.controller;

import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.goafabric.personservice.controller.dto.Person;
import org.goafabric.personservice.controller.dto.PersonSearch;
import org.goafabric.personservice.logic.PersonLogic;

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
    @Path("/{id}")
    public Person getById(@PathParam("id") String id) {
        return personLogic.getById(id);
    }

    @GET
    @Path("")
    public List<Person> findAll() {
        return personLogic.findAll();
    }

    @GET
    @Path("firstName")
    public List<Person> findByFirstName(@QueryParam("firstName") String firstName) {
        return personLogic.findByFirstName(firstName);
    }

    @GET
    @Path("lastName")
    public List<Person> findByLastName(@QueryParam("lastName") String lastName) {
        return personLogic.findByLastName(lastName);
    }

    @POST
    @Path("")
    @Consumes(MediaType.APPLICATION_JSON)
    public Person save(@Valid Person person) {
        return personLogic.save(person);
    }

    @GET
    @Path("name")
    public Person sayMyName(@QueryParam("name") String name) {
        return personLogic.sayMyName(name);
    }

    @GET
    @Path("/search")
    public String searchUsers(@BeanParam PersonSearch personSearch) {
        System.out.println(personSearch.getFirstName());
        System.out.println(personSearch.getLastName());
        return "ok";
    }
}
