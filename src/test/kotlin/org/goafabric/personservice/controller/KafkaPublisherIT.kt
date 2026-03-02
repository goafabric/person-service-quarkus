/*
package org.goafabric.personservice.controller

import io.quarkus.test.common.QuarkusTestResource
import io.quarkus.test.junit.QuarkusTest
import io.quarkus.test.kafka.InjectKafkaCompanion
import io.quarkus.test.kafka.KafkaCompanionResource
import io.smallrye.reactive.messaging.kafka.companion.KafkaCompanion
import jakarta.inject.Inject
import org.assertj.core.api.Assertions
import org.goafabric.personservice.controller.dto.Address
import org.goafabric.personservice.controller.dto.Person
import org.goafabric.personservice.logic.PersonLogic
import org.junit.jupiter.api.Test


//https://quarkus.io/guides/kafka#testing-using-a-kafka-broker
@QuarkusTest
@QuarkusTestResource(KafkaCompanionResource::class)
class KafkaPublisherIT {
    @Inject
    lateinit var personLogic: PersonLogic


    //@InjectKafkaCompanion
    //lateinit var companion: KafkaCompanion

    @Test
    fun save() {
        val person = personLogic.save(
            Person(
                null, null,
                "Homer",
                "Simpson",
                mutableListOf<Address>(createAddress("Evergreen Terrace"))
            )
        )

        Assertions.assertThat<Person>(person).isNotNull()

    }

    private fun createAddress(street: String): Address {
        return Address(
            null, null,
            street, "Springfield"
        )
    }


    */
/*
    @Test
    @Transactional
    fun findByStreet() {
        Assertions.assertThat<Person>(personLogic.findByStreet("Monty Mansion", 1, 3)).isNotNull().hasSize(1)
    }

     *//*

}
*/
