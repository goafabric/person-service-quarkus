package org.goafabric.personservice.logic

import com.azure.storage.blob.BlobServiceClient
import com.azure.storage.blob.models.BlobHttpHeaders
import jakarta.enterprise.context.ApplicationScoped
import org.eclipse.microprofile.config.inject.ConfigProperty
import org.goafabric.personservice.extensions.UserContext
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.Closeable
import java.io.InputStream


@ApplicationScoped
class ObjectStorageLogic(@param:ConfigProperty(name = "azure.storage.blob.container-name") val container: String,
                         val blobServiceClient: BlobServiceClient) {

    val directory: String = "${UserContext.tenantId}/"

    fun getByKey(key: String): ObjectEntry {
        val blobClient = blobServiceClient.getBlobContainerClient(container)
            .getBlobClient(getPath(key))

        val content = blobClient.downloadContent()

        return ObjectEntry(
            key,
            content.length,
            blobClient.properties.contentType,
            content.toStream()
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


    /*
    fun getByUrl(presignedUrl: String): PresignedObjectEntry {
        val blobClient = blobServiceClient.getBlobContainerClient(container)
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

     */

    fun getPath(key: String): String {
        return "$directory$key"
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

