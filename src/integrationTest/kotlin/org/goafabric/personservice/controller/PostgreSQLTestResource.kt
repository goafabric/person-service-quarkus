package org.goafabric.personservice.controller

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager
import org.testcontainers.containers.PostgreSQLContainer

class PostgreSQLTestResource : QuarkusTestResourceLifecycleManager {

    private val container = PostgreSQLContainer("postgres:18.3")
        .withDatabaseName("person")
        .withUsername("person-service")
        .withPassword("person-service")

    override fun start(): Map<String, String> {
        container.start()
        return mapOf(
            "quarkus.datasource.jdbc.url" to container.jdbcUrl,
            "quarkus.datasource.username" to container.username,
            "quarkus.datasource.password" to container.password,
            "database.provisioning.goals" to "-migrate -import-demo-data"
        )
    }

    override fun stop() {
        container.stop()
    }
}
