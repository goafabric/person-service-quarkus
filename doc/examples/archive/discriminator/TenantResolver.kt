package org.goafabric.personservice.persistence.extensions

import io.quarkus.hibernate.orm.PersistenceUnitExtension
import io.quarkus.runtime.StartupEvent
import jakarta.annotation.Priority
import jakarta.enterprise.context.RequestScoped
import jakarta.enterprise.event.Observes
import jakarta.enterprise.inject.spi.CDI
import jakarta.inject.Inject
import org.eclipse.microprofile.config.ConfigProvider
import org.eclipse.microprofile.config.inject.ConfigProperty
import org.flywaydb.core.Flyway
import org.goafabric.personservice.extensions.UserContext.tenantId
import org.goafabric.personservice.persistence.DemoDataImporter

@PersistenceUnitExtension
@RequestScoped
class TenantResolver: io.quarkus.hibernate.orm.runtime.tenant.TenantResolver {

    @ConfigProperty(name = "multi-tenancy.schema-prefix")
    var schemaPrefix: String? = null

    @Inject
    lateinit var demoDataImporter: DemoDataImporter

    override fun getDefaultTenantId(): String {
        return tenantId
    }

    override fun resolveTenantId(): String {
        return tenantId
    }

    fun onStart(@Observes @Priority(1) ev: StartupEvent) {
        if (ConfigProvider.getConfig().getValue("database.provisioning.goals", String::class.java).contains("-migrate")) {
            val flyway = CDI.current().select(Flyway::class.java).get()
            Flyway.configure()
                .configuration(flyway.configuration)
                .load()
                .migrate()
        }
    }
}
