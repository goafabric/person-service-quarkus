package org.goafabric.personservice.controller

import jakarta.enterprise.context.ApplicationScoped
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.eclipse.microprofile.reactive.messaging.Incoming
import org.goafabric.personservice.controller.dto.Address
import org.goafabric.personservice.controller.dto.Person
import org.goafabric.personservice.extensions.KafkaInterceptor.Companion.getOperation
import org.goafabric.personservice.extensions.KafkaListener
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@ApplicationScoped
class PersonConsumer {
    private val log: Logger = LoggerFactory.getLogger(this.javaClass)

    @Incoming("person")
    @KafkaListener
    fun consumePerson(consumerRecord: ConsumerRecord<String, Person>)  {
        val person = consumerRecord.value()
        val operation = getOperation(consumerRecord)
        log.info("loopback event for person {} {}", person, operation)

    }

    @Incoming("address")
    @KafkaListener
    fun consumeAddress(consumerRecord: ConsumerRecord<String, Address>)  {
        val operation = getOperation(consumerRecord)
        log.info("loopback person: " + consumerRecord.value())
    }

}