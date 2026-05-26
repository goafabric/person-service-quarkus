package org.goafabric.personservice.persistence.extensions

import com.fasterxml.jackson.databind.ObjectMapper
import io.quarkus.test.junit.QuarkusTest
import jakarta.inject.Inject
import org.assertj.core.api.Assertions
import org.goafabric.personservice.consumer.PersonConsumer
import org.goafabric.personservice.controller.dto.Address
import org.goafabric.personservice.controller.dto.Person
import org.goafabric.personservice.logic.PersonLogic
import org.junit.jupiter.api.Assumptions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.testcontainers.DockerClientFactory
import java.util.concurrent.TimeUnit


// When Docker is running, Kafka Broker will be automatically started for test, if not running, test will be skipped for now
@QuarkusTest
class KafkaPublisherIT {
    @Inject
    lateinit var personLogic: PersonLogic

    @Inject
    lateinit var personConsumer: PersonConsumer

    @Inject
    lateinit var objectMapper: ObjectMapper

    companion object {
        @JvmStatic
        @BeforeAll
        fun checkDocker(): Unit {
            Assumptions.assumeTrue(
                DockerClientFactory.instance().isDockerAvailable,
                "Docker is not running, skipping test"
            )
        }
    }

    @Test
    fun save() {
        println(objectMapper.registeredModuleIds)

        val person = personLogic.save(
            Person(
                null, null,
                "Homer",
                "Simpson",
                mutableListOf(createAddress("Evergreen Terrace"))
            )
        )

        Assertions.assertThat(person).isNotNull()
        Assertions.assertThat(personConsumer.personLatch.await(5, TimeUnit.SECONDS)).isTrue
        Assertions.assertThat(personConsumer.addressLatch.await(5, TimeUnit.SECONDS)).isTrue
    }

    private fun createAddress(street: String): Address {
        return Address(
            null, null,
            street, "Springfield"
        )
    }


}