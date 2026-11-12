package org.goafabric.personservice.persistence.extensions

import io.confluent.kafka.schemaregistry.json.JsonSchema
import io.confluent.kafka.schemaregistry.testutil.MockSchemaRegistry
import io.confluent.kafka.serializers.json.KafkaJsonSchemaDeserializer
import org.assertj.core.api.Assertions.assertThat
import org.apache.kafka.common.header.internals.RecordHeaders
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.goafabric.personservice.controller.dto.Address
import org.goafabric.personservice.controller.dto.Person
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.File

/**
 * Uses the schemas that are registered in the real registry (src/deploy/docker/schema-registry/schemas)
 * and the same producer settings as application.properties, but against an in-memory registry
 */
class KafkaJsonSchemaSerdeTest {
    private val scope = "kafka-json-schema-serde-test"
    private val registryConfig = mapOf("schema.registry.url" to "mock://$scope")

    private val serializer = ContractJsonSchemaSerializer<Any>()

    @BeforeEach
    fun setUp() {
        val client = MockSchemaRegistry.getClientForScope(scope)
        client.register("person-value", JsonSchema(schemaFile("person.json")))
        client.register("address-value", JsonSchema(schemaFile("address.json")))
        client.register("person-headers", JsonSchema(schemaFile("headers.json")))
        client.register("address-headers", JsonSchema(schemaFile("headers.json")))

        serializer.configure(producerConfig("fail"), false)
    }

    private fun producerConfig(violation: String) = registryConfig + mapOf(
        "auto.register.schemas" to false,
        "use.latest.version" to true,
        "latest.compatibility.strict" to false,
        "json.fail.invalid.schema" to true,
        "contract.violation" to violation)

    private fun headers(vararg overrides: Pair<String, String?>): RecordHeaders {
        val values = mapOf(
            "operation" to "CREATE", "X-TenantId" to "0", "X-OrganizationId" to "0",
            "X-Auth-Request-Preferred-Username" to "anonymous", "traceparent" to "00-abc-def-01") + overrides
        return RecordHeaders().apply {
            values.forEach { (key, value) -> if (value != null) add(key, value.toByteArray()) }
        }
    }

    private val invalidAddress = Address(null, null, "x", "Springfield")
    private val validAddress = Address(null, null, "Evergreen Terrace", "Springfield")

    @AfterEach
    fun tearDown() = MockSchemaRegistry.dropScope(scope)

    @Test
    fun `person conforms to the registered schema and survives a roundtrip`() {
        val person = Person("1", 0, "Homer", "Simpson", listOf(Address("2", 0, "Evergreen Terrace", "Springfield")))

        val bytes = serializer.serialize("person", person)

        assertThat(bytes[0]).isEqualTo(0.toByte()) //confluent magic byte, followed by the schema id
        assertThat(deserializer(Person::class.java).deserialize("person", bytes)).isEqualTo(person)
    }

    @Test
    fun `address conforms to the registered schema and survives a roundtrip`() {
        val address = Address(null, null, "Evergreen Terrace", "Springfield")

        val bytes = serializer.serialize("address", address)

        assertThat(deserializer(Address::class.java).deserialize("address", bytes)).isEqualTo(address)
    }

    @Test
    fun `payload violating the registered schema is rejected`() {
        assertThatThrownBy { serializer.serialize("address", Address(null, null, "x", "Springfield")) }
            .hasRootCauseInstanceOf(org.everit.json.schema.ValidationException::class.java)
        assertThatThrownBy { serializer.serialize("person", Person(null, null, "Homer", "Simpson", listOf(Address(null, null, "Evergreen !", "Springfield")))) }
            .hasRootCauseInstanceOf(org.everit.json.schema.ValidationException::class.java)
    }

    @Test
    fun `payload violation is only logged when contract violation is set to warn`() {
        serializer.configure(producerConfig("warn"), false)

        val bytes = serializer.serialize("address", invalidAddress)

        assertThat(bytes[0]).isEqualTo(0.toByte())
        assertThat(deserializer(Address::class.java).deserialize("address", bytes)).isEqualTo(invalidAddress)
    }

    @Test
    fun `headers conforming to the header contract are accepted, additional headers are allowed`() {
        assertThat(serializer.serialize("address", headers(), validAddress)).isNotEmpty()
    }

    @Test
    fun `missing or invalid headers are rejected`() {
        assertThatThrownBy { serializer.serialize("address", headers("X-TenantId" to null), validAddress) }
            .isInstanceOf(org.apache.kafka.common.errors.SerializationException::class.java)
            .hasMessageContaining("headers do not match")
        assertThatThrownBy { serializer.serialize("address", headers("operation" to "MERGE"), validAddress) }
            .hasMessageContaining("headers do not match")
    }

    @Test
    fun `header violation is only logged when contract violation is set to warn`() {
        serializer.configure(producerConfig("warn"), false)

        assertThat(serializer.serialize("address", headers("X-TenantId" to null), validAddress)).isNotEmpty()
    }

    @Test
    fun `topic without a header contract is rejected`() {
        val client = MockSchemaRegistry.getClientForScope(scope)
        client.deleteSubject("address-headers")
        client.deleteSubject("address-headers", true)

        assertThatThrownBy { serializer.serialize("address", headers(), validAddress) }
            .hasMessageContaining("no header contract address-headers")
    }

    @Test
    fun `topic without a registered schema is rejected, since auto register is off`() {
        assertThatThrownBy { serializer.serialize("unknown", Address(null, null, "Evergreen Terrace", "Springfield")) }
            .isInstanceOf(Exception::class.java)
    }

    private fun schemaFile(name: String) = File("src/deploy/docker/schema-registry/schemas/$name").readText()

    private fun <T> deserializer(type: Class<T>) = KafkaJsonSchemaDeserializer<T>().apply {
        configure(registryConfig + ("json.value.type" to type.name), false)
    }
}
