package org.goafabric.personservice.persistence.extensions

import io.smallrye.reactive.messaging.kafka.api.OutgoingKafkaRecordMetadata
import jakarta.enterprise.context.ApplicationScoped
import jakarta.enterprise.inject.Instance
import jakarta.persistence.PostPersist
import jakarta.persistence.PostRemove
import jakarta.persistence.PostUpdate
import org.apache.kafka.common.header.internals.RecordHeaders
import org.eclipse.microprofile.config.inject.ConfigProperty
import org.eclipse.microprofile.reactive.messaging.Channel
import org.eclipse.microprofile.reactive.messaging.Emitter
import org.eclipse.microprofile.reactive.messaging.Message
import org.goafabric.personservice.extensions.UserContext
import org.goafabric.personservice.logic.mapper.PersonMapper
import org.goafabric.personservice.persistence.entity.AddressEo
import org.goafabric.personservice.persistence.entity.PersonEo
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.nio.charset.StandardCharsets


@ApplicationScoped
class KafkaPublisher(
    @param:ConfigProperty(name = "mp.messaging.outgoing.general.enabled") private val kafkaEnabled: Boolean,
    @param:Channel("general") private val personEmitter: Instance<Emitter<Any>>,
    private val personMapper: PersonMapper
) {
    private val log: Logger = LoggerFactory.getLogger(this.javaClass)

    private enum class DbOperation {  CREATE, UPDATE, DELETE }

    @PostPersist
    fun afterCreate(`object`: Any) {
        publish(DbOperation.CREATE, `object`)
    }

    @PostUpdate
    fun afterUpdate(`object`: Any) {
        publish(DbOperation.UPDATE, `object`)
    }

    @PostRemove
    fun afterDelete(`object`: Any) {
        publish(DbOperation.DELETE, `object`)
    }

    private fun publish(operation: DbOperation, entity: Any) {
        if (!kafkaEnabled) return

        when (entity) {
            is PersonEo  -> publish("person", entity.id!!, operation, personMapper.map(entity))
            is AddressEo -> publish("address", entity.id!!, operation, personMapper.map(entity))
            else -> error("Type " + entity::class)
        }
    }

    //publish both person and address with the same topic to retain order, put Operation and UserContext to Kafka Headers to prevent EventData Wrapper
    private fun publish(topic: String, key: String, operation: DbOperation, payload: Any) {
        log.info("publishing event of type {}", topic)

        val headers = RecordHeaders()
        headers.add("operation", operation.toString().toByteArray(StandardCharsets.UTF_8))

        UserContext.adapterHeaderMap.forEach { (headerKey, value) ->
            headers.add(headerKey, value.toByteArray(StandardCharsets.UTF_8))
        }

        val metadata = OutgoingKafkaRecordMetadata.builder<String>()
            .withTopic(topic) // optional if already configured
            .withKey(key)
            .withHeaders(headers)
            .build()

        personEmitter.get().send(Message.of(payload).addMetadata(metadata))
    }

}
