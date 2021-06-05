package org.goafabric.personservice.service;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

@QuarkusTest
public class PersonServiceTest {

    @Test
    public void testFindAll() {
        given()
          .when().get("/persons/findAll")
          .then()
             .statusCode(200);
                //.body(is("true"));
    }

}