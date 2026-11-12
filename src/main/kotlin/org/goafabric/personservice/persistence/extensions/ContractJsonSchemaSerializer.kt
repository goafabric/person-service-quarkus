package org.goafabric.personservice.persistence.extensions

import com.fasterxml.jackson.databind.node.JsonNodeFactory
import io.confluent.kafka.schemaregistry.json.JsonSchema
import io.confluent.kafka.serializers.json.KafkaJsonSchemaSerializer
import org.apache.kafka.common.errors.SerializationException
import org.apache.kafka.common.header.Headers
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.nio.charset.StandardCharsets

/**
 * Validates the payload against <topic>-value and the Kafka headers against <topic>-headers (both from the schema registry).
 * contract.violation=fail (default) rejects the message, contract.violation=warn only logs and sends the message anyway.
 * requires json.fail.invalid.schema=true, as the validation of the payload is hooked into it
 */
class ContractJsonSchemaSerializer<T> : KafkaJsonSchemaSerializer<T>() {
    private val log: Logger = LoggerFactory.getLogger(this.javaClass)

    private var failOnViolation = true

    //the latest version cache is keyed by schema, so this is only a stable placeholder
    private val headersCacheKey = JsonSchema("{}")

    override fun configure(config: Map<String, *>, isKey: Boolean) {
        super.configure(config, isKey)
        failOnViolation = config[CONTRACT_VIOLATION]?.toString()?.lowercase() != "warn"
    }

    override fun serialize(topic: String, headers: Headers?, record: T): ByteArray? {
        if (record != null && headers != null) {
            validateHeaders(topic, headers)
        }
        return super.serialize(topic, headers, record)
    }

    override fun validateJson(`object`: T, schema: JsonSchema): T {
        try {
            return super.validateJson(`object`, schema)
        } catch (e: SerializationException) {
            violation("payload does not match the registered schema: ${e.message}", e)
            return `object`
        }
    }

    private fun validateHeaders(topic: String, headers: Headers) {
        val subject = "$topic-headers"
        val schema = try {
            lookupLatestVersion(subject, headersCacheKey, false).schema as JsonSchema
        } catch (e: Exception) {
            violation("no header contract $subject found in the schema registry: ${e.message}", e)
            return
        }

        val node = JsonNodeFactory.instance.objectNode()
        headers.forEach { node.put(it.key(), String(it.value(), StandardCharsets.UTF_8)) }
        try {
            schema.validate(node)
        } catch (e: RuntimeException) {
            violation("headers do not match the registered schema $subject: ${e.message}", e)
        }
    }

    private fun violation(message: String, cause: Exception) {
        if (failOnViolation) throw SerializationException(message, cause)
        log.warn("contract violation, sending message anyway: {}", message)
    }

    companion object {
        const val CONTRACT_VIOLATION = "contract.violation"
    }
}
