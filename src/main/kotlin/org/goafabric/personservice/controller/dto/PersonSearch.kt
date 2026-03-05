package org.goafabric.personservice.controller.dto

import jakarta.ws.rs.QueryParam

class PersonSearch {
    @QueryParam("firstName")
    var firstName: String? = null

    @QueryParam("lastName")
    var lastName: String? = null


    constructor()
    constructor(firstName: String?, lastName: String?) {
        this.firstName = firstName
        this.lastName = lastName
    }
}