package org.goafabric.personservice.logic

import com.azure.storage.blob.BlobServiceClient
import jakarta.enterprise.context.ApplicationScoped
import org.eclipse.microprofile.config.inject.ConfigProperty
import org.goafabric.personservice.extensions.UserContext
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream


@ApplicationScoped
class ObjectStorageLogic(@param:ConfigProperty(name = "multi-tenancy.schema-prefix") var schemaPrefix: String,
                               val blobServiceClient: BlobServiceClient) {

    fun getByKey(key: String): ObjectEntry {
        val outputStream = ByteArrayOutputStream()
        val client = blobServiceClient.getBlobContainerClient(getBucketName()).getBlobClient(key)
        client.downloadStream(outputStream)
        return ObjectEntry(
            key,
            client.properties.contentType,
            outputStream.toByteArray().size.toLong(),
            outputStream.toByteArray()
        )
    }

    fun deleteByKey(key: String) {
        val client = blobServiceClient.getBlobContainerClient(getBucketName()).getBlobClient(key)
        client.delete()
    }

    fun put(objectEntry: ObjectEntry) {
        blobServiceClient.createBlobContainerIfNotExists(getBucketName())
        blobServiceClient.getBlobContainerClient(getBucketName())
        blobServiceClient.getBlobContainerClient(getBucketName()).getBlobClient(objectEntry.objectName)
            .upload(ByteArrayInputStream(objectEntry.data), true)
    }

    private fun getBucketName(): String {
        return schemaPrefix.replace("_".toRegex(), "-") + UserContext.tenantId
    }


    data class ObjectEntry(
        val objectName: String,
        val contentType: String,
        val objectSize: Long,
        val data: ByteArray
    )

}

