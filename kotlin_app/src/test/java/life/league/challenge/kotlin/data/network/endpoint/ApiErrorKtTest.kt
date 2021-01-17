package life.league.challenge.kotlin.data.network.endpoint

import life.league.challenge.kotlin.data.network.endpoint.base.ApiError
import life.league.challenge.kotlin.data.network.endpoint.base.StatusCode
import life.league.challenge.kotlin.data.network.endpoint.base.getApiError
import life.league.challenge.kotlin.data.network.endpoint.base.toApiError
import org.junit.Assert.assertTrue
import org.junit.Test
import java.net.ConnectException
import java.net.UnknownHostException


class ApiErrorKtTest {
    @Test
    fun `getApiError StatusCode_BADREQUEST returns BadRequest`() {
        val message = "ABC"
        val error = getApiError(StatusCode.BAD_REQUEST, message)
        assertTrue(error is ApiError.ClientError.BadRequest)
        assertTrue(error.message?.equals(message) == true)
    }

    @Test
    fun `getApiError StatusCode_UNAUTHORISED returns Unauthorised`() {
        val message = "ABC"
        val error = getApiError(StatusCode.UNAUTHORISED, message)
        assertTrue(error is ApiError.ClientError.Unauthorised)
        assertTrue(error.message?.equals(message) == true)
    }

    @Test
    fun `getApiError StatusCode_FORBIDDEN returns Forbidden`() {
        val message = "ABC"
        val error = getApiError(StatusCode.FORBIDDEN, message)
        assertTrue(error is ApiError.ClientError.Forbidden)
        assertTrue(error.message?.equals(message) == true)
    }

    @Test
    fun `getApiError StatusCode_NOT_FOUND returns NotFound`() {
        val message = "ABC"
        val error = getApiError(StatusCode.NOT_FOUND, message)
        assertTrue(error is ApiError.ClientError.NotFound)
        assertTrue(error.message?.equals(message) == true)
    }

    @Test
    fun `getApiError StatusCode_TIMEOUT returns Timeout`() {
        val message = "ABC"
        val error = getApiError(StatusCode.TIMEOUT, message)
        assertTrue(error is ApiError.ClientError.Timeout)
        assertTrue(error.message?.equals(message) == true)
    }

    @Test
    fun `getApiError StatusCode_I_AM_A_TEAPOT returns IAmATeapot`() {
        val message = "ABC"
        val error = getApiError(StatusCode.I_AM_A_TEAPOT, message)
        assertTrue(error is ApiError.ClientError.IAmATeapot)
        assertTrue(error.message?.equals(message) == true)
    }

    @Test
    fun `getApiError StatusCode_INTERNAL_SERVER_ERROR returns Unauthorised`() {
        val message = "ABC"
        val error = getApiError(StatusCode.INTERNAL_SERVER_ERROR, message)
        assertTrue(error is ApiError.ServerError.InternalServerError)
        assertTrue(error.message?.equals(message) == true)
    }

    @Test
    fun `getApiError StatusCode_SERVICE_UNAVAILABLE returns ServiceUnavailable`() {
        val message = "ABC"
        val error = getApiError(StatusCode.SERVICE_UNAVAILABLE, message)
        assertTrue(error is ApiError.ServerError.ServiceUnavailable)
        assertTrue(error.message?.equals(message) == true)
    }

    @Test
    fun `getApiError undefined StatusCode returns GenericError`() {
        val message = "ABC"
        val error1 = getApiError(444, message)
        assertTrue(error1 is ApiError.GenericError && error1.code == 444)
        assertTrue(error1.message?.equals(message) == true)

        val error2 = getApiError(450, null)
        assertTrue(error2 is ApiError.GenericError && error2.code == 450)
        assertTrue(error2.message == null)
    }

    @Test
    fun `throwable-toApiError returns Unreachable for ConnectException`() {
        val message = "A deep message"
        val throwable = ConnectException(message)
        val localError = throwable.toApiError()
        assertTrue(localError is ApiError.Unreachable && localError.message == message)
    }

    @Test
    fun `throwable-toApiError returns Unreachable for UnknownHostException`() {
        val message = "A deep message"
        val localError = UnknownHostException(message).toApiError()
        assertTrue(localError is ApiError.Unreachable && localError.message == message)
    }

}