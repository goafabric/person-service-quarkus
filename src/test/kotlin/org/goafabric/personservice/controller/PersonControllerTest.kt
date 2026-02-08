package org.goafabric.personservice.controller

import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured
import org.junit.jupiter.api.Test
import java.util.*

@QuarkusTest
class PersonControllerTest {
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
            .auth()
            .basic(String(Base64.getDecoder().decode("YWRtaW4=")), String(Base64.getDecoder().decode("YWRtaW4=")))
            .`when`().get("/persons?firstName=Homer&page=1&size=3")
            .then()
            .statusCode(200)
    }

    @Test
    fun findByLastName() {
        RestAssured.given()
            .auth()
            .basic(String(Base64.getDecoder().decode("YWRtaW4=")), String(Base64.getDecoder().decode("YWRtaW4=")))
            .`when`().get("/persons?lastName=Simpson&page=1&size=3")
            .then()
            .statusCode(200)
    }
}