package org.goafabric.personservice.persistence.extensions

import io.quarkus.test.junit.QuarkusTest
import jakarta.inject.Inject
import org.assertj.core.api.Assertions
import org.eclipse.microprofile.config.ConfigProvider
import org.goafabric.personservice.consumer.PersonConsumer
import org.goafabric.personservice.controller.dto.Address
import org.goafabric.personservice.controller.dto.Person
import org.goafabric.personservice.logic.PersonLogic
import org.junit.jupiter.api.Assumptions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import java.util.concurrent.TimeUnit

@QuarkusTest
class KafkaPublisherIT {
    @Inject
    lateinit var personLogic: PersonLogic

    @Inject
    lateinit var personConsumer: PersonConsumer

    companion object {
        @JvmStatic
        @BeforeAll
        fun checkDevServices() =
            Assumptions.assumeTrue(ConfigProvider.getConfig().getValue("quarkus.devservices.enabled", Boolean::class.java), "Docker is not running, skipping test")
    }

    @Test
    fun save() {
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