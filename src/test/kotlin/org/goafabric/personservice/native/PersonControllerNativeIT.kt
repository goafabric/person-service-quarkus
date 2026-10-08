package org.goafabric.personservice.native

import io.quarkus.test.common.QuarkusTestResource
import io.quarkus.test.junit.QuarkusIntegrationTest
import io.restassured.RestAssured
import org.eclipse.microprofile.config.ConfigProvider
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ConditionEvaluationResult
import org.junit.jupiter.api.extension.ExecutionCondition
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.extension.ExtensionContext

class DevServicesEnabled : ExecutionCondition {
    override fun evaluateExecutionCondition(context: ExtensionContext): ConditionEvaluationResult =
        if (ConfigProvider.getConfig().getValue("quarkus.devservices.enabled", Boolean::class.java))
            ConditionEvaluationResult.enabled("") else ConditionEvaluationResult.disabled("")
}

@QuarkusIntegrationTest
@ExtendWith(DevServicesEnabled::class)
@QuarkusTestResource(value = PostgresNativeTestResource::class, restrictToAnnotatedClass = true)
class PersonControllerNativeIT {

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
