package org.goafabric.personservice.persistence.extensions

import io.quarkus.hibernate.orm.PersistenceUnitExtension
import jakarta.enterprise.context.ApplicationScoped
import jakarta.enterprise.context.RequestScoped
import jakarta.enterprise.inject.spi.CDI
import jakarta.inject.Inject
import org.eclipse.microprofile.config.ConfigProvider
import org.eclipse.microprofile.config.inject.ConfigProperty
import org.flywaydb.core.Flyway
import org.goafabric.personservice.extensions.UserContext.tenantId
import java.util.Map
import java.util.function.Consumer

@PersistenceUnitExtension
@RequestScoped
class TenantResolver: io.quarkus.hibernate.orm.runtime.tenant.TenantResolver {
    @Inject
    internal var flywayConfig: FlywayConfig? = null

    @ConfigProperty(name = "multi-tenancy.schema-prefix")
    var schemaPrefix: String? = null

    override fun getDefaultTenantId(): String {
        return "PUBLIC"
    }

    override fun resolveTenantId(): String {
        return schemaPrefix + tenantId
    }

    @ApplicationScoped
    internal class FlywayConfig() {
        init {
            if (ConfigProvider.getConfig().getValue("database.provisioning.goals", String::class.java).contains("-migrate")) {
                val flyway = CDI.current().select(Flyway::class.java).get()
                val schemas = ConfigProvider.getConfig().getValue("multi-tenancy.tenants", String::class.java)
                val schemaPrefix =
                    ConfigProvider.getConfig().getValue("multi-tenancy.schema-prefix", String::class.java)
                listOf(*schemas.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray())
                    .forEach(
                        Consumer { schema: String? ->
                            Flyway.configure()
                                .configuration(flyway.configuration)
                                .schemas(schemaPrefix + schema)
                                .defaultSchema(schemaPrefix + schema)
                                .placeholders(Map.of<String?, String?>("tenantId", schema))
                                .load()
                                .migrate()
                        }
                    )
            }
        }
    }
}
