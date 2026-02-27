package org.goafabric.personservice.controller

import jakarta.enterprise.context.ApplicationScoped
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.eclipse.microprofile.reactive.messaging.Incoming
import org.goafabric.personservice.controller.dto.Person
import org.goafabric.personservice.extensions.KafkaListener
import org.goafabric.personservice.extensions.MyKafkaInterceptor
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@ApplicationScoped
class PersonConsumer {
    private val log: Logger = LoggerFactory.getLogger(this.javaClass)

    @Incoming("person-in")
    @KafkaListener
    fun listen(consumerRecord: ConsumerRecord<String, Person>)  {
        val operation = MyKafkaInterceptor.getOperation(consumerRecord.headers())
        log.info("loopback person: " + consumerRecord.value())
    }

    /*
    @Incoming("person-in")
    fun listen(person: Person)  {
        log.info("loopback person: " + person.toString())
    }
    @Incoming("person-in")
    fun listen(address: Address) {
        log.info("loopback address: " + address.toString())
    }
    */
}