package org.goafabric.personservice

import io.quarkus.runtime.Quarkus
import io.quarkus.runtime.annotations.QuarkusMain

@QuarkusMain
class Application

fun main(args: Array<String>) {
    Quarkus.run(*args)
}
