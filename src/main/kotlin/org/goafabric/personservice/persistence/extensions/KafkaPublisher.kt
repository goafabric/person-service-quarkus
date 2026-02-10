//package org.goafabric.personservice.persistence.extensions
//
//import jakarta.enterprise.context.ApplicationScoped
//import jakarta.persistence.PostPersist
//import jakarta.persistence.PostRemove
//import jakarta.persistence.PostUpdate
//import org.eclipse.microprofile.reactive.messaging.Channel
//import org.eclipse.microprofile.reactive.messaging.Emitter
//import org.eclipse.microprofile.reactive.messaging.Incoming
//import org.goafabric.personservice.controller.dto.Person
//import org.goafabric.personservice.logic.PersonMapper
//import org.goafabric.personservice.persistence.entity.AddressEo
//import org.goafabric.personservice.persistence.entity.PersonEo
//import org.slf4j.Logger
//import org.slf4j.LoggerFactory
//
//@ApplicationScoped
//class KafkaPublisher(
//    @param:Channel("person-out") private val personEmitter: Emitter<Any>,
//    private val personMapper: PersonMapper
//) {
//    private val log: Logger = LoggerFactory.getLogger(this.javaClass)
//
//    private enum class DbOperation {  CREATE, UPDATE, DELETE }
//
//    @PostPersist
//    fun afterCreate(`object`: Any) {
//        publish(DbOperation.CREATE, `object`)
//    }
//
//    @PostUpdate
//    fun afterUpdate(`object`: Any) {
//        publish(DbOperation.UPDATE, `object`)
//    }
//
//    @PostRemove
//    fun afterDelete(`object`: Any) {
//        publish(DbOperation.DELETE, `object`)
//    }
//
//    private fun publish(operation: DbOperation, entity: Any) {
//        when (entity) {
//            is PersonEo  -> publish("person", entity.id!!, operation, personMapper.map(entity))
//            is AddressEo -> publish("person", entity.id!!, operation, personMapper.map(entity))
//            else -> error("Type " + entity::class)
//        }
//    }
//
//    //publish both person and address with the same topic to retain order, put Operation and UserContext to Kafka Headers to prevent EventData Wrapper
//    private fun publish(topic: String, key: String, operation: DbOperation, payload: Any) {
//        log.info("publishing event of type {}", topic)
//        personEmitter.send(payload)
//    }
//
//    /*
//    @Incoming("person-in")
//    fun listen(person: Person) {
//        log.info("loopback: " + person.toString())
//    }
//
//     */
//}
