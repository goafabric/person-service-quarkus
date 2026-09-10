package org.goafabric.personservice

import io.quarkus.runtime.Quarkus
import io.quarkus.runtime.StartupEvent
import io.quarkus.runtime.annotations.QuarkusMain
import jakarta.enterprise.context.ApplicationScoped
import jakarta.enterprise.event.Observes
import org.jboss.logging.Logger

@ApplicationScoped
@QuarkusMain
class Application {
    private val log = Logger.getLogger(Application::class.java)

    fun onStart(@Observes event: StartupEvent) {
        log.info("Running on Java ${System.getProperty("java.version")} " + "(${System.getProperty("java.vendor")}, ${System.getProperty("java.vm.name")})")
    }
}

fun main(args: Array<String>) {
    Quarkus.run(*args)
}
