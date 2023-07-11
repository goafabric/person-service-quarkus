package org.goafabric.personservice.controller;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import org.goafabric.personservice.controller.vo.Person;
import org.junit.jupiter.api.Test;

import java.util.Base64;

import static io.restassured.RestAssured.given;

@QuarkusTest
public class PersonControllerTest {

    @Test
    public void findAll() {
        given()
          .auth().basic(new String(Base64.getDecoder().decode("YWRtaW4=")), new String(Base64.getDecoder().decode("YWRtaW4=")))
          .when().get("/persons/findAll")
          .then()
             .statusCode(200);

        Person[] persons = RestAssured.given()
                .auth().basic(new String(Base64.getDecoder().decode("YWRtaW4=")), new String(Base64.getDecoder().decode("YWRtaW4=")))
                .get("/persons/findAll").as(Person[].class);
    }

    @Test
    public void findByFirstName() {
        given()
                .auth().basic(new String(Base64.getDecoder().decode("YWRtaW4=")), new String(Base64.getDecoder().decode("YWRtaW4=")))
                .when().get("/persons/findByFirstName?firstName=Homer")
                .then()
                .statusCode(200);
    }

    @Test
    public void findByLastName() {
        given()
                .auth().basic(new String(Base64.getDecoder().decode("YWRtaW4=")), new String(Base64.getDecoder().decode("YWRtaW4=")))
                .when().get("/persons/findByLastName?lastName=Simpson")
                .then()
                .statusCode(200);
    }

}