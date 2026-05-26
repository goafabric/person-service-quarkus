package org.goafabric.personservice.controller

import io.quarkiverse.mcp.server.Tool
import jakarta.validation.Valid
import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType
import org.goafabric.personservice.controller.dto.Person
import org.goafabric.personservice.controller.dto.PersonSearch
import org.goafabric.personservice.logic.PersonLogic

@Path("/persons")
@Produces(MediaType.APPLICATION_JSON)
class PersonController(private val personLogic: PersonLogic) {
    @GET
    @Path("/{id}")
    @Tool(description = "get person by id")
    fun getById(@PathParam("id") id: String): Person {
        return personLogic.getById(id)
    }


    @GET
    @Path("")
    @Tool(description = "find person")
    fun find(
        @BeanParam personSearch: PersonSearch,
        @QueryParam("page") page: Int,
        @QueryParam("size") size: Int
    ): List<Person> {
        return personLogic.search(personSearch, page, size)
    }

    @POST
    @Path("")
    @Consumes(MediaType.APPLICATION_JSON)
    @Tool(description = "save person")
    fun save(@Valid person: @Valid Person): Person {
        return personLogic.save(person)
    }

    @GET
    @Path("name")
    fun sayMyName(@QueryParam("name") name: String): Person {
        return personLogic.sayMyName(name)
    }
}
