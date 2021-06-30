package org.goafabric.personservice.service;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

@QuarkusTest
public class PersonServiceTest {

    @Test
    public void findAll() {
        given()
          .when().get("/persons/findAll")
          .then()
             .statusCode(200);
    }

    @Test
    public void findByFirstName() {
        given()
                .when().get("/persons/findByFirstName?firstName=Homer")
                .then()
                .statusCode(200);
    }

    @Test
    public void findByLastName() {
        given()
                .when().get("/persons/findByLastName?lastName=Simpson")
                .then()
                .statusCode(200);
    }

}