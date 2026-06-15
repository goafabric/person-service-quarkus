package org.goafabric.personservice.controller

import io.quarkus.test.common.QuarkusTestResource
import io.quarkus.test.junit.QuarkusIntegrationTest
import io.restassured.RestAssured
import org.junit.jupiter.api.Test

@QuarkusIntegrationTest
@QuarkusTestResource(PostgreSQLTestResource::class)
class PersonControllerRestIT {

    @Test
    fun findAll() {
        RestAssured.given()
            .`when`().get("/persons?page=1&size=3")
            .then()
            .statusCode(200)
    }

    @Test
    fun findByFirstName() {
        RestAssured.given()
            .`when`().get("/persons?firstName=Homer&page=1&size=3")
            .then()
            .statusCode(200)
    }

    @Test
    fun findByLastName() {
        RestAssured.given()
            .`when`().get("/persons?lastName=Simpson&page=1&size=3")
            .then()
            .statusCode(200)
    }
}
