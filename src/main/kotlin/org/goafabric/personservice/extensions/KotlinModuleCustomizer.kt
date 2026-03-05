package org.goafabric.personservice.extensions

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import io.quarkus.jackson.ObjectMapperCustomizer
import jakarta.enterprise.context.ApplicationScoped

@ApplicationScoped
class KotlinModuleCustomizer : ObjectMapperCustomizer {
    override fun customize(objectMapper: ObjectMapper) {
        objectMapper.registerModule(KotlinModule.Builder().build())
    }
}