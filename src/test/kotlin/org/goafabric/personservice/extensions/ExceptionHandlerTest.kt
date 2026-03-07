package org.goafabric.personservice.extensions

import jakarta.ws.rs.core.Response
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class ExceptionHandlerTest {
    private val exceptionHandler: ExceptionHandler = ExceptionHandler()

    @Test
    fun handleIllegalArgumentException() {
        assertThat(exceptionHandler.toResponse(IllegalArgumentException("illegal argument")).status)
            .isEqualTo(Response.Status.PRECONDITION_FAILED.statusCode)
    }

    @Test
    fun handleIllegalStateException() {
        assertThat(exceptionHandler.toResponse(IllegalStateException("illegal state")).status)
            .isEqualTo(Response.Status.PRECONDITION_FAILED.statusCode)
    }

    @Test
    fun handleGeneralException() {
        assertThat(exceptionHandler.toResponse(NullPointerException("null pointer")).status)
            .isEqualTo(Response.Status.BAD_REQUEST.statusCode)
    }
}