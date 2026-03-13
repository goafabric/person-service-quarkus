package org.goafabric.personservice.logic

import com.azure.storage.blob.BlobServiceClient
import com.azure.storage.blob.models.BlobHttpHeaders
import jakarta.enterprise.context.ApplicationScoped
import org.eclipse.microprofile.config.inject.ConfigProperty
import org.goafabric.personservice.extensions.UserContext
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream


@ApplicationScoped
class ObjectStorageLogic(@param:ConfigProperty(name = "azure.storage.blob.container-name") val container: String,
                         val blobServiceClient: BlobServiceClient) {

    val directory: String = "${UserContext.tenantId}/"

    fun getByKey(key: String): ObjectEntry {
        val outputStream = ByteArrayOutputStream()
        val client = blobServiceClient.getBlobContainerClient(container)
            .getBlobClient(getPath(key))
        client.downloadStream(outputStream)
        return ObjectEntry(
            key,
            client.properties.contentType,
            outputStream.toByteArray().size.toLong(),
            outputStream.toByteArray()
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

        blobClient.setHttpHeaders(BlobHttpHeaders().setContentType(objectEntry.contentType))
        blobClient.upload(ByteArrayInputStream(objectEntry.data), true)
    }

    fun getPath(key: String): String {
        return "$directory$key"
    }

    data class ObjectEntry(
        val key: String,
        val contentType: String,
        val objectSize: Long,
        val data: ByteArray
    )

}

