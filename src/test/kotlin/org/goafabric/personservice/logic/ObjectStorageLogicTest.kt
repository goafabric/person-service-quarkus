/*
package org.goafabric.personservice.logic

import com.azure.core.util.BinaryData
import com.azure.storage.blob.BlobClient
import com.azure.storage.blob.BlobClientBuilder
import com.azure.storage.blob.BlobContainerClient
import com.azure.storage.blob.BlobServiceClient
import com.azure.storage.blob.models.BlobProperties
import com.azure.storage.blob.specialized.BlobInputStream
import io.quarkus.test.junit.QuarkusTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.io.ByteArrayInputStream

class ObjectStorageLogicTest {
    private val blobServiceClient = mock<BlobServiceClient>()
    private val containerClient = mock<BlobContainerClient>()
    private val blobClient = mock<BlobClient>()
    private val blobClientBuilder = mock<BlobClientBuilder>()
    private val blobProperties = mock<BlobProperties>()

    private val objectStorageLogic = ObjectStorageLogic("test", blobServiceClient, blobClientBuilder)

    @BeforeEach
    fun beforeEach() {
        whenever(blobServiceClient.getBlobContainerClient(eq("test"))).thenReturn(containerClient)
        whenever(containerClient.getBlobClient(any())).thenReturn(blobClient)
        whenever(blobClient.exists()).thenReturn(true)
        whenever(blobClient.properties).thenReturn(blobProperties)
        whenever(blobProperties.contentType).thenReturn("text/plain")
    }

    @Test
    fun getByKey() {
        val content = "test content".toByteArray()
        val binaryData = mock<BinaryData>()
        whenever(binaryData.toStream()).thenReturn(ByteArrayInputStream(content))
        whenever(binaryData.length).thenReturn(content.size.toLong())

        whenever(blobClient.downloadContent()).thenReturn(binaryData)
        whenever(blobProperties.contentType).thenReturn("text/plain")

        val objectEntry = objectStorageLogic.getByKey("key")
        assertThat(objectEntry).isNotNull
        verify(blobClient).downloadContent()
    }

    @Test
    fun deleteByKey() {
        objectStorageLogic.deleteByKey("test")
        verify(blobClient).delete()
    }

    @Test
    fun put() {
        objectStorageLogic.put(ObjectStorageLogic.ObjectEntry("", 0L, "", "".toByteArray().inputStream()))
        verify(blobClient).upload(any<ByteArrayInputStream>(), eq(0L))
    }

    @Test
    fun getByUrl() {
        // given
        val presignedUrl = "https://example.blob.core.windows.net/container/blob?signature=xyz"
        val content = "test content".toByteArray()
        val contentSize = content.size.toLong()
        val contentType = "application/pdf"
        val presignedBlobClient = mock<BlobClient>()
        val presignedBlobProperties = mock<BlobProperties>()
        val blobInputStream = mock<BlobInputStream>()

        whenever(blobClientBuilder.endpoint(presignedUrl)).thenReturn(blobClientBuilder)
        whenever(blobClientBuilder.buildClient()).thenReturn(presignedBlobClient)
        whenever(presignedBlobClient.properties).thenReturn(presignedBlobProperties)
        whenever(presignedBlobProperties.blobSize).thenReturn(contentSize)
        whenever(presignedBlobProperties.contentType).thenReturn(contentType)
        whenever(presignedBlobClient.openInputStream()).thenReturn(blobInputStream)

        val result = objectStorageLogic.getByUrl("https://example.blob.core.windows.net/container/blob?signature=xyz")

        // then
        verify(blobClientBuilder).endpoint(presignedUrl)
        verify(blobClientBuilder).buildClient()
        verify(presignedBlobClient).properties
        verify(presignedBlobClient).openInputStream()

        assertThat(result.url).isEqualTo(presignedUrl)
        assertThat(result.contentType).isEqualTo(contentType)
        assertThat(result.sizeBytes).isEqualTo(contentSize)
        assertThat(result.data).isEqualTo(blobInputStream)

    }

    @Test
    fun getPath() {
        assertThat(objectStorageLogic.getPath("key")).isEqualTo("0/key")
    }

}*/
