package org.goafabric.personservice.persistence

import io.quarkus.runtime.StartupEvent
import jakarta.enterprise.context.ApplicationScoped
import jakarta.enterprise.event.Observes
import org.eclipse.microprofile.config.inject.ConfigProperty
import org.goafabric.personservice.controller.dto.Address
import org.goafabric.personservice.controller.dto.Person
import org.goafabric.personservice.controller.dto.PersonSearch
import org.goafabric.personservice.extensions.UserContext
import org.goafabric.personservice.logic.ObjectStorageLogic
import org.goafabric.personservice.logic.PersonLogic
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.function.Consumer
import kotlin.system.exitProcess

@ApplicationScoped
class DemoDataImporter(
    @param:ConfigProperty(name = "database.provisioning.goals") private val goals: String,
    @param:ConfigProperty(name = "multi-tenancy.tenants") private val tenants: String,
    @param:ConfigProperty(name = "quarkus.azure.storage.blob.enabled") private val blobEnabled: Boolean,
    private val personLogic: PersonLogic,
    private val objectStorageLogic: ObjectStorageLogic
) {
    private val log: Logger = LoggerFactory.getLogger(this.javaClass)

    fun onStart(@Observes ev: StartupEvent) {
        run()
    }

    fun run() {
        if (goals.contains("-import-demo-data")) {
            log.info("Importing demo data ...")
            importDemoData()
            log.info("Demo data import done ...")
        }

        if (goals.contains("-terminate")) {
            log.info("Terminating app ...")
            exitProcess(0)
        }
    }

    private fun importDemoData() {
        listOf(*tenants.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()).forEach(
            Consumer { tenant: String ->
                UserContext.tenantId = tenant
                if (personLogic.search(PersonSearch(null, null), 1, 10).isEmpty()) {
                    insertData()
                }
            })
        UserContext.tenantId = "0"
    }

    private fun insertData() {
        personLogic.save(
            Person(
                null, null, "Homer", "Simpson",
                listOf(createAddress("Evergreen Terrace"))
            )
        )
        personLogic.save(
            Person(
                null, null, "Bart", "Simpson",
                listOf(createAddress("Everblue Terrace"))
            )
        )
        personLogic.save(
            Person(
                null, null, "Monty", "Burns",
                listOf(createAddress("Mammon Street No. 1000 on the corner of Croesus"))
            )
        )

        if (blobEnabled) {
            objectStorageLogic.put(
                ObjectStorageLogic.ObjectEntry(
                    "hello_world.txt",
                    "hello world".length.toLong(),
                    "text/plain",
                    "hello world".toByteArray().inputStream(),
                )
            )

            log.info("##blob: " + objectStorageLogic.getByKey("hello_world.txt")
                .data.readAllBytes().toString(Charsets.UTF_8))
        }
    }

    private fun createAddress(street: String): Address {
        return Address(null, null, street, "Springfield " + UserContext.tenantId)
    }
}