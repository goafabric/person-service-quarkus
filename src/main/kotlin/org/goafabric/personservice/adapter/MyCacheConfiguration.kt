package org.goafabric.personservice.adapter

import io.quarkus.cache.CacheKeyGenerator
import io.quarkus.cache.CompositeCacheKey
import jakarta.enterprise.context.ApplicationScoped
import org.goafabric.personservice.extensions.UserContext
import java.lang.reflect.Method

//https://quarkus.io/guides/cache/#multiple-backends-simultaneously
@ApplicationScoped
class MyCacheConfiguration : CacheKeyGenerator {
    override fun generate(method: Method, vararg methodParams: Any): Any {
        val key = CompositeCacheKey(UserContext.tenantId, UserContext.organizationId, method.name, methodParams)
        return key
    }
}