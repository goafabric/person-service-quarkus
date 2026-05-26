package org.goafabric.personservice.logic

import com.azure.storage.blob.BlobClientBuilder
import com.azure.storage.blob.BlobServiceClient
import com.azure.storage.blob.models.BlobHttpHeaders
import jakarta.enterprise.context.ApplicationScoped
import jakarta.enterprise.inject.Produces
import jakarta.transaction.Transactional
import org.eclipse.microprofile.config.inject.ConfigProperty
import org.goafabric.personservice.extensions.UserContext
import java.io.Closeable
import java.io.InputStream


@ApplicationScoped
@Transactional
class ObjectStorageLogic(@param:ConfigProperty(name = "azure.storage.blob.container-name") val container: String,
                         val blobServiceClient: BlobServiceClient,
                         val blobClientBuilder: BlobClientBuilder) {

    fun getByKey(key: String): ObjectEntry {
        val blobClient = blobServiceClient.getBlobContainerClient(container)
            .getBlobClient(getPath(key))

        val content = blobClient.downloadContent()

        return ObjectEntry(
            key = key,
            content.length,
            contentType = blobClient.properties.contentType,
            data = content.toStream()
        )
    }

    fun deleteByKey(key: String) {
        val client = blobServiceClient.getBlobContainerClient(container)
            .getBlobClient(getPath(key))
        client.delete()
    }

    fun put(objectEntry: ObjectEntry) {
        blobServiceClient.createBlobContainerIfNotExists(container)
        val blobClient = blobServiceClient.getBlobContainerClient(container)
            .getBlobClient(getPath(objectEntry.key))

        blobClient.upload(objectEntry.data, objectEntry.sizeBytes)
        blobClient.setHttpHeaders(BlobHttpHeaders().setContentType(objectEntry.contentType))
    }

    fun getByUrl(presignedUrl: String): PresignedObjectEntry {
        val blobClient = blobClientBuilder
            .endpoint(presignedUrl)
            .buildClient()

        val content = blobClient.openInputStream()
        val properties = blobClient.properties

        return PresignedObjectEntry(
            url = presignedUrl,
            contentType = properties.contentType,
            data = content,
            sizeBytes = properties.blobSize,
        )
    }


    fun getPath(key: String): String {
        return "${UserContext.tenantId}/$key"
    }

    class BlobClientBuilderConfig {
        @Produces
        fun blobClientBuilder(): BlobClientBuilder {
            return BlobClientBuilder()
        }
    }

    data class ObjectEntry(
        val key: String,
        val sizeBytes: Long,
        val contentType: String,
        val data: InputStream,
    ): Closeable by data

    data class PresignedObjectEntry (
        val url: String,
        val sizeBytes: Long,
        val contentType: String,
        val data: InputStream,
    ) : Closeable by data
}

