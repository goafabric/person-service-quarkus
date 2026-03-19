package org.goafabric.personservice.controller

import io.quarkus.test.junit.QuarkusMock
import io.quarkus.test.junit.QuarkusTest
import jakarta.inject.Inject
import org.assertj.core.api.Assertions.assertThat
import org.eclipse.microprofile.rest.client.inject.RestClient
import org.goafabric.personservice.adapter.Callee
import org.goafabric.personservice.adapter.CalleeServiceAdapter
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.eq
import org.mockito.kotlin.whenever

@QuarkusTest
class CalleeServiceAdapterMockedIt {
    @Inject
    lateinit var personController: PersonController

    @Test
    fun sayMyName() {
        val calleeServiceAdapter = mock<CalleeServiceAdapter>()
        whenever(calleeServiceAdapter!!.sayMyName(eq("Heisenberg"))).thenReturn(Callee("", "Heisenberg"))

        QuarkusMock.installMockForType(calleeServiceAdapter, CalleeServiceAdapter::class.java, RestClient.LITERAL)
        assertThat(personController.sayMyName(eq("Heisenberg"))).isNotNull
    }
}


