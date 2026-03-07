package org.goafabric.personservice.persistence.extensions

import jakarta.enterprise.context.ApplicationScoped
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.eclipse.microprofile.reactive.messaging.Incoming
import org.goafabric.personservice.controller.dto.Address
import org.goafabric.personservice.controller.dto.Person
import org.goafabric.personservice.extensions.KafkaListener
import org.goafabric.personservice.extensions.operation
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.concurrent.CountDownLatch

@ApplicationScoped
class PersonConsumer {
    private val log: Logger = LoggerFactory.getLogger(this.javaClass)
    val personLatch: CountDownLatch = CountDownLatch(1)
    val addressLatch: CountDownLatch = CountDownLatch(1)

    @Incoming("person")
    @KafkaListener
    fun consumePerson(consumerRecord: ConsumerRecord<String, Person>)  {
        log.info("loopback event for person {} {}",
            consumerRecord.value(), consumerRecord.operation())
       personLatch.countDown()
    }

    //For Quarkus we can only have a 1:1 relation between Topic:Entity, Custom KafkaListener for Intercepting the JWT
    @Incoming("address")
    @KafkaListener
    fun consumeAddress(consumerRecord: ConsumerRecord<String, Address>)  {
        log.info("loopback event for address {} {}",
            consumerRecord.value(), consumerRecord.operation())
        addressLatch.countDown()
    }

}