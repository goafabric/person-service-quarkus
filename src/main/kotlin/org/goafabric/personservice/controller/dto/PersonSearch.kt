package org.goafabric.personservice.controller.dto

import jakarta.ws.rs.QueryParam

class PersonSearch(@param:QueryParam("firstName") var firstName: String? = null,
                   @param:QueryParam("lastName") var lastName: String? = null)
