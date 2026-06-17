package org.goafabric.personservice.native

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager
import org.junit.jupiter.api.Assumptions
import org.testcontainers.DockerClientFactory
import org.testcontainers.postgresql.PostgreSQLContainer

class NativeTestResource : QuarkusTestResourceLifecycleManager {

    private val container = PostgreSQLContainer("postgres:18.3")
    private val qd = "quarkus.datasource"

    override fun start(): Map<String, String> {
        Assumptions.assumeTrue(DockerClientFactory.instance().isDockerAvailable, "Docker not available, skipping")
        container.start()
        return mapOf("$qd.jdbc.url" to container.jdbcUrl, "$qd.username" to container.username, "$qd.password" to container.password,)
    }

    override fun stop() = container.stop()
}
