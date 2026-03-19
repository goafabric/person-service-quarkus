package org.goafabric.personservice.controller

import io.quarkus.test.InjectMock
import io.quarkus.test.junit.QuarkusTest
import jakarta.inject.Inject
import org.assertj.core.api.Assertions.assertThat
import org.eclipse.microprofile.rest.client.inject.RestClient
import org.goafabric.personservice.adapter.Callee
import org.goafabric.personservice.adapter.CalleeServiceAdapter
import org.junit.jupiter.api.Test
import org.mockito.kotlin.eq
import org.mockito.kotlin.whenever

@QuarkusTest
class CalleeServiceAdapterMockedIt {
    @Inject
    lateinit var personController: PersonController

    @InjectMock
    @RestClient
    lateinit var calleeServiceAdapter: CalleeServiceAdapter

    @Test
    fun sayMyName() {
        whenever(calleeServiceAdapter.sayMyName(eq("Heisenberg"))).thenReturn(Callee("", "Heisenberg"))
        assertThat(personController.sayMyName(eq("Heisenberg"))).isNotNull
    }
}


