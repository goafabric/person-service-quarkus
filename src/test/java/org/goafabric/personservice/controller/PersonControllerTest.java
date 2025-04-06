package org.goafabric.personservice.controller;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import java.util.Base64;

import static io.restassured.RestAssured.given;

@QuarkusTest
public class PersonControllerTest {

    /*
    @Test
    public void findAll() {
        given()
          .when().get("/persons?page=1&size=3")
          .then()
             .statusCode(200);

        RestAssured.given()
                .get("/persons").as(Person[].class);
    }

     */

    @Test
    public void findByFirstName() {
        given()
                .auth().basic(new String(Base64.getDecoder().decode("YWRtaW4=")), new String(Base64.getDecoder().decode("YWRtaW4=")))
                .when().get("/persons?firstName=Homer&page=1&size=3")
                .then()
                .statusCode(200);
    }

    @Test
    public void findByLastName() {
        given()
                .auth().basic(new String(Base64.getDecoder().decode("YWRtaW4=")), new String(Base64.getDecoder().decode("YWRtaW4=")))
                .when().get("/persons?lastName=Simpson&page=1&size=3")
                .then()
                .statusCode(200);
    }

}