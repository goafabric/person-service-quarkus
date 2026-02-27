package org.goafabric.personservice.controller

import io.opentelemetry.api.trace.Span
import io.opentelemetry.context.Context
import jakarta.enterprise.context.ApplicationScoped
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.common.header.Headers
import org.eclipse.microprofile.reactive.messaging.Incoming
import org.goafabric.personservice.controller.dto.Person
import org.goafabric.personservice.extensions.UserContext
import org.goafabric.personservice.extensions.UserContext.setContext
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.slf4j.MDC
import java.nio.charset.StandardCharsets

@ApplicationScoped
class PersonConsumer {
    private val log: Logger = LoggerFactory.getLogger(this.javaClass)

    @Incoming("person-in")
    fun listen(consumerRecord: ConsumerRecord<String, Person>)  {
        setContext(consumerRecord.headers())
        val person = consumerRecord.value()
        log.info("loopback person: $person")
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

    private fun setContext(headers: Headers) {
        setContext(
            getValue(headers, "X-TenantId"), getValue(headers, "X-OrganizationId"),
            getValue(headers, "X-Auth-Request-Preferred-Username"), null
        )
        configureLogsAndTracing()
    }

    private fun configureLogsAndTracing() {
        MDC.put("tenantId", UserContext.tenantId)
        Span.fromContext(Context.current()).setAttribute("tenant.id", UserContext.tenantId)
    }

    private fun getValue(headers: Headers, key: String): String {
        return String(headers.lastHeader(key).value(), StandardCharsets.UTF_8)
    }
}