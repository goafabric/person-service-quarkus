package org.goafabric.personservice.logic

import com.azure.storage.blob.BlobClient
import com.azure.storage.blob.BlobContainerClient
import com.azure.storage.blob.BlobServiceClient
import com.azure.storage.blob.models.BlobProperties
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
    private val blobProperties = mock<BlobProperties>()

    private val objectStorageLogic = ObjectStorageLogic("test", blobServiceClient)

    @BeforeEach
    fun beforeEach() {
        whenever(blobServiceClient.getBlobContainerClient(eq("test"))).thenReturn(containerClient)
        whenever(containerClient.getBlobClient(any())).thenReturn(blobClient)
        whenever(blobClient.exists()).thenReturn(true)
        whenever(blobClient.properties).thenReturn(blobProperties)
        whenever(blobProperties.contentType).thenReturn("application/octet-stream")
    }

    @Test
    fun getByKey() {
        val objectEntry = objectStorageLogic.getByKey("key")
        assertThat(objectEntry).isNotNull
        verify(blobClient).downloadStream(any())
    }

    @Test
    fun deleteByKey() {
        objectStorageLogic.deleteByKey("test")
        verify(blobClient).delete()
    }

    @Test
    fun put() {
        objectStorageLogic.put(ObjectStorageLogic.ObjectEntry("", "", 0L, byteArrayOf()))
        verify(blobClient).upload(any<ByteArrayInputStream>(), eq(true))
    }

    @Test
    fun getPath() {
        assertThat(objectStorageLogic.getPath("key")).isEqualTo("0/key")
    }

}