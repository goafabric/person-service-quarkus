package org.goafabric.personservice.native

import io.quarkus.test.common.QuarkusTestResource
import io.quarkus.test.junit.QuarkusIntegrationTest
import io.restassured.RestAssured
import org.junit.jupiter.api.Assumptions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.testcontainers.DockerClientFactory

@QuarkusIntegrationTest
@QuarkusTestResource(value = NativeTestResource::class, restrictToAnnotatedClass = true)
class PersonControllerNativeIT {
    companion object {
        @JvmStatic
        @BeforeAll
        fun checkDocker() {
            Assumptions.assumeTrue(
                DockerClientFactory.instance().isDockerAvailable,
                "Docker is not running, skipping test"
            )
        }
    }


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
