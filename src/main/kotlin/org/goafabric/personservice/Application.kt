package org.goafabric.personservice

import com.azure.storage.blob.BlobClientBuilder
import io.quarkus.runtime.Quarkus
import io.quarkus.runtime.annotations.QuarkusMain
import jakarta.enterprise.inject.Produces

@QuarkusMain
class Application

fun main(args: Array<String>) {
    Quarkus.run(*args)
}
