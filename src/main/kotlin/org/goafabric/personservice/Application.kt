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
    fun onStart(@Observes event: StartupEvent) =
        Logger.getLogger(Application::class.java).info("Running on Java ${System.getProperty("java.version")} " + "(${System.getProperty("java.vendor")}, ${System.getProperty("java.vm.name")})")

}

fun main(args: Array<String>) {
    Quarkus.run(*args)
}
