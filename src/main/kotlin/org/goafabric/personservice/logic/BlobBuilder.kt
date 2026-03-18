package org.goafabric.personservice.logic

import com.azure.storage.blob.BlobClientBuilder
import jakarta.enterprise.context.ApplicationScoped
import jakarta.enterprise.inject.Produces

@ApplicationScoped
class BlobBuilder {
    @Produces
    fun blobClientBuilder(): BlobClientBuilder {
        return BlobClientBuilder()
    }

}